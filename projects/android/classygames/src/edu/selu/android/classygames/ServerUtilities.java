package edu.selu.android.classygames;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import edu.selu.android.classygames.data.Person;


public class ServerUtilities
{


	private final static int REGISTER_MAX_ATTEMPTS = 5;
	private final static long REGISTER_BACKOFF_TIME = 2000; // milliseconds

	public final static String POST_DATA = "json";
	public final static String POST_DATA_BLANK = "";
	public final static String POST_DATA_BOARD = "board";
	public final static String POST_DATA_ID = "id";
	public final static String POST_DATA_NAME = "name";
	public final static String POST_DATA_REG_ID = "reg_id";
	public final static String POST_DATA_USER_CHALLENGED = "user_challenged";
	public final static String POST_DATA_USER_CREATOR = "user_creator";

	public final static String MIMETYPE_JSON = "application/json";

	public final static String SERVER_ADDRESS = "http://classygames.net/";

	public final static String SERVER_NEW_GAME = "NewGame";
	public final static String SERVER_NEW_GAME_ADDRESS = SERVER_ADDRESS + SERVER_NEW_GAME;

	public final static String SERVER_GAMES_LIST_REFRESH = "GamesListRefresh";
	public final static String SERVER_GAMES_LIST_REFRESH_ADDRESS = SERVER_ADDRESS + SERVER_GAMES_LIST_REFRESH;

	public final static String SERVER_NEW_MOVE = "NewMove";
	public final static String SERVER_NEW_MOVE_ADDRESS = SERVER_ADDRESS + SERVER_GAMES_LIST_REFRESH;

	public final static String SERVER_NEW_REG_ID = "NewRegId";
	public final static String SERVER_NEW_REG_ID_ADDRESS = SERVER_ADDRESS + SERVER_NEW_REG_ID;
	public final static String SERVER_REMOVE_REG_ID = "RemoveRegId";
	public final static String SERVER_REMOVE_REG_ID_ADDRESS = SERVER_ADDRESS + SERVER_REMOVE_REG_ID;

	private static Random random;


	public static void contextBroadcast(final Context context, final String message)
	{
		Intent intent = new Intent();
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}


	private static void postToServer(final String url, final ArrayList<NameValuePair> data) throws IOException
	{
		String jsonString = null;
		InputStream inputStream = null;

		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(data));

		try
		{
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);

			inputStream = httpResponse.getEntity().getContent();
		}
		catch (final Exception e)
		{
			Log.e(Utilities.LOG_TAG, "Error in HTTP connection.", e);
		}

		if (inputStream != null)
		{
			try
			{
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HTTP.ISO_8859_1));
				StringBuilder stringBuilder = new StringBuilder();

				for (String line = new String(); line != null; line = bufferedReader.readLine())
				{
					stringBuilder.append(line);
				}

				jsonString = new String(stringBuilder.toString());
				Log.d(Utilities.LOG_TAG, "\"" + jsonString + "\"");
			}
			catch (final Exception e)
			{
				Log.e(Utilities.LOG_TAG, "Error converting HTTP POST result from server.", e);
			}
		}
		else
		{

		}
	}


	public static boolean GCMRegister(final Context context, final Person person, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Registering device with reg_id of \"" + reg_id + "\" with GCM server.");

		// build the data to be sent to the server
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(POST_DATA_ID, new Long(person.getId()).toString()));
		nameValuePairs.add(new BasicNameValuePair(POST_DATA_NAME, person.getName()));
		nameValuePairs.add(new BasicNameValuePair(POST_DATA_REG_ID, reg_id));

		if (random == null)
		{
			random = new Random();
		}

		long backoffTime = REGISTER_BACKOFF_TIME + random.nextInt(1000);

		for (int i = 1; i <= REGISTER_MAX_ATTEMPTS; ++i)
		{
			Log.i(Utilities.LOG_TAG, "GCM register attempt #" + i);

			try
			{
				contextBroadcast(context, context.getString(R.string.server_registration_attempt, i));
				postToServer(SERVER_NEW_REG_ID_ADDRESS, nameValuePairs);
				GCMRegistrar.setRegisteredOnServer(context, true);
				contextBroadcast(context, context.getString(R.string.server_registration_success));

				return true;
			}
			catch (final IOException ioe)
			{
				Log.e(Utilities.LOG_TAG, "GCM register failure on attempt #" + i, ioe);

				if (i <= REGISTER_MAX_ATTEMPTS)
				{
					try
					{
						Log.d(Utilities.LOG_TAG, "Sleeping for " + backoffTime + "ms before next GCM register attempt.");
						Thread.sleep(backoffTime);
					}
					catch (final InterruptedException ie)
					{
						Log.e(Utilities.LOG_TAG, "GCM register thread interrupted! Aborting registration attempt");
						Thread.currentThread().interrupt();

						return false;
					}

					// exponentially increase backoffTime so that we wait a bit longer before our
					// next GCM registration attempt
					backoffTime = (backoffTime * 2) + backoffTime;
				}
			}
		}

		contextBroadcast(context, context.getString(R.string.server_registration_fail, REGISTER_MAX_ATTEMPTS));

		return false;
	}


	public static void GCMUnregister(final Context context, final long id, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Unregistering device with reg_id of \"" + reg_id + "\" from GCM server.");

		// build the data to be sent to the server
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(POST_DATA_ID, new Long(id).toString()));
		nameValuePairs.add(new BasicNameValuePair(POST_DATA_REG_ID, reg_id));

		try
		{
			postToServer(SERVER_REMOVE_REG_ID_ADDRESS, nameValuePairs);
			GCMRegistrar.setRegisteredOnServer(context, false);
			contextBroadcast(context, context.getString(R.string.server_unregistration_success));
		}
		catch (IOException e)
		{
			contextBroadcast(context, context.getString(R.string.server_unregistration_fail));
		}
	}


}
