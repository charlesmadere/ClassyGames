package com.charlesmadere.android.classygames.server;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.KeysAndConstants;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Class for tons of stuff relating to communication with the Classy Games
 * server.
 */
public final class Server
{


	public final static String LOG_TAG = Utilities.LOG_TAG + " - Server";


	private final static String ADDRESS_MAIN = "http://classygames.elasticbeanstalk.com/";
	private final static String ADDRESS_FORFEIT_GAME = ADDRESS_MAIN + "ForfeitGame";
	private final static String ADDRESS_GET_GAME = ADDRESS_MAIN + "GetGame";
	private final static String ADDRESS_GET_GAMES = ADDRESS_MAIN + "GetGames";
	private final static String ADDRESS_GET_STATS = ADDRESS_MAIN + "GetStats";
	private final static String ADDRESS_NEW_GAME = ADDRESS_MAIN + "NewGame";
	private final static String ADDRESS_NEW_MOVE = ADDRESS_MAIN + "NewMove";
	private final static String ADDRESS_NEW_REG_ID = ADDRESS_MAIN + "NewRegId";
	private final static String ADDRESS_REMOVE_REG_ID = ADDRESS_MAIN + "RemoveRegId";
	private final static String ADDRESS_SKIP_MOVE = ADDRESS_MAIN + "SkipMove";


	public final static String POST_DATA = "json";
	public final static String POST_DATA_BOARD = "board";
	public final static String POST_DATA_CHECKERS = "checkers";
	public final static String POST_DATA_CHESS = "chess";
	public final static String POST_DATA_ERROR = "error";
	public final static String POST_DATA_FINISHED = "finished";
	public final static String POST_DATA_GAME_ID = "game_id";
	public final static String POST_DATA_GAME_TYPE = "game_type";
	public final static byte POST_DATA_GAME_TYPE_CHECKERS = 1;
	public final static byte POST_DATA_GAME_TYPE_CHESS = 2;
	public final static String POST_DATA_ID = "id";
	public final static String POST_DATA_LAST_MOVE = "last_move";
	public final static String POST_DATA_LOSES = "loses";
	public final static String POST_DATA_MESSAGE_TYPE = "message_type";
	public final static byte POST_DATA_MESSAGE_TYPE_NEW_GAME = 1;
	public final static byte POST_DATA_MESSAGE_TYPE_NEW_MOVE = 2;
	public final static byte POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE = 7;
	public final static byte POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN = 15;
	public final static String POST_DATA_NAME = "name";
	public final static String POST_DATA_REG_ID = "reg_id";
	public final static String POST_DATA_RESULT = "result";
	public final static String POST_DATA_TURN = "turn";
	public final static String POST_DATA_TURN_THEIRS = "turn_theirs";
	public final static String POST_DATA_TURN_YOURS = "turn_yours";
	public final static String POST_DATA_SUCCESS = "success";
	public final static String POST_DATA_USER_CHALLENGED = "user_challenged";
	public final static String POST_DATA_USER_CREATOR = "user_creator";
	public final static String POST_DATA_WINS = "wins";




	/**
	 * Attempts to parse out the meaningful portion of the server's HTTP JSON
	 * response.
	 * 
	 * @param serverResponse
	 * The response as received from the server (a String).
	 * 
	 * @return
	 * Returns true if a successful message was able to be parsed out of the
	 * given server response.
	 */
	private static boolean gcmParseServerResults(final String serverResponse)
	{
		boolean wasSuccessMessageFound = false;

		try
		{
			Log.d(LOG_TAG, "Parsing JSON data: " + serverResponse);
			final JSONObject jsonData = new JSONObject(serverResponse);
			final JSONObject jsonResult = jsonData.getJSONObject(POST_DATA_RESULT);

			try
			{
				final String successMessage = jsonResult.getString(POST_DATA_SUCCESS);
				Log.d(LOG_TAG, "Server returned success message: " + successMessage);

				wasSuccessMessageFound = true;
			}
			catch (final JSONException e)
			{
				final String errorMessage = jsonResult.getString(POST_DATA_ERROR);
				Log.d(LOG_TAG, "Server returned error message: " + errorMessage);
			}
		}
		catch (final JSONException e)
		{
			Log.e(Utilities.LOG_TAG, "Server returned message that was unable to be properly parsed.", e);
		}

		return wasSuccessMessageFound;
	}


	/**
	 * Attempts to register the current Android user's registration ID with the
	 * server for GCM notifications.
	 * 
	 * @param regId
	 * The Android regId to be sent to the server and used for GCM
	 * notifications.
	 * 
	 * @param context
	 * The Context of the class you're calling this method from.
	 * 
	 * @return
	 * Returns true if the GCM registration attempt was successful.
	 * 
	 * @throws IOException
	 * If something weird happens when trying to post the given data to the
	 * server then this exception will be thrown.
	 */
	public static boolean gcmRegister(final String regId, final Context context) throws IOException
	{
		Log.d(LOG_TAG, "Registering device with regId of \"" + regId + "\" with the Classy Games server.");
		final Person whoAmI = Utilities.getWhoAmI(context);

		// build the data to be sent to the server
		final ApiData data = new ApiData();
		data.addKeyValuePair(POST_DATA_ID, whoAmI.getId());
		data.addKeyValuePair(POST_DATA_NAME, whoAmI.getName());
		data.addKeyValuePair(POST_DATA_REG_ID, regId);

		if (gcmParseServerResults(postToServerNewRegId(data)))
		{
			Log.d(LOG_TAG, "Server successfully completed all the regId stuff.");
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Attempts to register the current Android user's registration ID with the
	 * server for GCM notifications.
	 * 
	 * @param context
	 * The Context of the class that you're calling this method from.
	 * 
	 * @return
	 * Returns true if the GCM registration attempt was successful.
	 * 
	 * @throws IOException
	 * If something weird happens when trying to post the given data to the
	 * server then this exception will be thrown.
	 */
	public static boolean gcmRegister(final Context context) throws IOException
	{
		final String regId = Utilities.getRegId(context);

		if (Utilities.verifyValidString(regId))
		{
			return gcmRegister(regId, context);
		}
		else
		{
			gcmPerformRegister(context);

			return false;
		}
	}


	/**
	 * Performs the GCM registration process.
	 * 
	 * @param context
	 * The Context of the class that you're calling this method from.
	 */
	public static void gcmPerformRegister(final Context context)
	{
		final Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", KeysAndConstants.GOOGLE_PROJECT_ID);
		context.startService(registrationIntent);
	}


	/**
	 * Attempts to unregister the current Android user's registration ID from
	 * the server for GCM notifications.
	 * 
	 * @param context
	 * The Context of the class you're calling this method from.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static void gcmUnregister(final Context context) throws IOException
	{
		Log.d(LOG_TAG, "Unregistering device with from GCM server.");

		// build the data to be sent to the server
		final ApiData data = new ApiData();
		data.addKeyValuePair(POST_DATA_ID, Utilities.getWhoAmI(context).getId());

		postToServer(ADDRESS_REMOVE_REG_ID, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the ForfeitGame
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerForfeitGame(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_FORFEIT_GAME, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the GetGame end
	 * point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerGetGame(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_GET_GAME, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the GetGames
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerGetGames(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_GET_GAMES, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the GetStats
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerGetStats(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_GET_STATS, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the NewGame end
	 * point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerNewGame(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_NEW_GAME, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the NewMove end
	 * point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerNewMove(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_NEW_MOVE, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the NewRegId
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerNewRegId(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_NEW_REG_ID, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the RemoveRegId
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerRemoveRegId(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_REMOVE_REG_ID, data);
	}


	/**
	 * Makes an HTTP POST request to the Classy Games server on the SkipMove
	 * end point.
	 *
	 * @param data
	 * A list of key-value pairs to be sent to the server.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 *
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	public static String postToServerSkipMove(final ApiData data) throws IOException
	{
		return postToServer(ADDRESS_SKIP_MOVE, data);
	}


	/**
	 * Use this method to send data to and receive a response from the server.
	 * The String that this method returns is the server's response.
	 * 
	 * @param url
	 * The URL that you want to send your data to. This should be formulated
	 * using the URLs found in this class.
	 * 
	 * @param data
	 * Data to be sent to the server using HTTP POST.
	 *
	 * @return
	 * The server's response as a String. This will need to be parsed as it is
	 * JSON data. <strong>There is a slight possibility that the data String
	 * returned from this method will be null.</strong> Please check for that
	 * <strong>as well as</strong> if the String is empty.
	 * 
	 * @throws IOException
	 * If something weird happens when trying to POST to the server then this
	 * exception will be thrown.
	 */
	private static String postToServer(final String url, final ApiData data) throws IOException
	{
		String serverResponse = null;

		Log.d(LOG_TAG, "Posting data to server at " + url);

		final HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(data.getKeyValuePairs()));

		final HttpClient httpClient = new DefaultHttpClient();
		final HttpResponse httpResponse = httpClient.execute(httpPost);
		final InputStream inputStream = httpResponse.getEntity().getContent();

		if (inputStream != null)
		{
			final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, HTTP.UTF_8);
			final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			final StringBuilder stringBuilder = new StringBuilder();

			String line = "";

			while (line != null)
			{
				stringBuilder.append(line);
				line = bufferedReader.readLine();
			}

			serverResponse = stringBuilder.toString();
		}

		return serverResponse;
	}


	/**
	 * Ensures that a gameType byte received from the server is a valid
	 * gameType byte.
	 * 
	 * @param gameType
	 * The gameType byte to check for validity.
	 * 
	 * @return
	 * Returns true if the given gameType byte is valid.
	 */
	public static boolean validGameTypeValue(final byte gameType)
	{
		switch (gameType)
		{
			case POST_DATA_GAME_TYPE_CHECKERS:
			case POST_DATA_GAME_TYPE_CHESS:
				return true;

			default:
				return false;
		}
	}


	/**
	 * Ensures that a messageType byte received from the server is a valid
	 * messageType byte.
	 * 
	 * @param messageType
	 * The messageType byte to check for validity.
	 * 
	 * @return
	 * Returns true if the given messageType byte is valid.
	 */
	public static boolean validMessageTypeValue(final byte messageType)
	{
		switch (messageType)
		{
			case POST_DATA_MESSAGE_TYPE_NEW_GAME:
			case POST_DATA_MESSAGE_TYPE_NEW_MOVE:
			case POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
			case POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
				return true;

			default:
				return false;
		}
	}


	/**
	 * Ensures that a winOrLose byte received from the server is a valid
	 * winOrLose byte.
	 * 
	 * @param winOrLose
	 * The winOrLose byte to check for validity.
	 * 
	 * @return
	 * Returns true if the given winOrLose byte is valid.
	 */
	public static boolean validWinOrLoseValue(final byte winOrLose)
	{
		switch (winOrLose)
		{
			case POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
			case POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
				return true;

			default:
				return false;
		}
	}


}
