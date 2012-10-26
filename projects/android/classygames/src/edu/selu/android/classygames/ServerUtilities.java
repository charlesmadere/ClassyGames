package edu.selu.android.classygames;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import edu.selu.android.classygames.data.Person;


public class ServerUtilities
{


	private final static int REGISTER_MAX_ATTEMPTS = 5;
	private final static long REGISTER_BACKOFF_TIME = 2000; // milliseconds

	public final static String JSON_DATA = "json";
	public final static String JSON_DATA_BLANK = "";
	public final static String JSON_DATA_BOARD = "board";
	public final static String JSON_DATA_ID = "id";
	public final static String JSON_DATA_NAME = "name";
	public final static String JSON_DATA_REG_ID = "reg_id";
	public final static String JSON_DATA_USER_CHALLENGED = "user_challenged";
	public final static String JSON_DATA_USER_CREATOR = "user_creator";

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


	private static void postToServer(final String endpoint, final Map<String, String> params) throws IOException
	{
		URL url = null;

		try
		{
			url = new URL(endpoint);
		}
		catch (final MalformedURLException e)
		{
			throw new IllegalArgumentException("Invalid url: \"" + endpoint + "\"");
		}

		if (url != null)
		{
			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

			while (iterator.hasNext())
			// constructs the POST body using the parameters
			{
				Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
	
				if (iterator.hasNext())
				{
					bodyBuilder.append('&');
				}
			}

			final String body = bodyBuilder.toString();
			Log.d(Utilities.LOG_TAG, "Posting \"" + body + "\" to \"" + url + "\"");

			byte[] bytes = body.getBytes();

			HttpURLConnection connection = null;

			try
			{
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setFixedLengthStreamingMode(bytes.length);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

				// send the request via POST
				OutputStream output = connection.getOutputStream();
				output.write(bytes);
				output.close();

				// handle the response
				final int status = connection.getResponseCode();

				if (status != 200)
				{
					throw new IOException("POST failed with error code \"" + status + "\"");
				}
			}
			finally
			{
				if (connection != null)
				{
					connection.disconnect();
				}
			}
		}
	}


	/**
	 * @param context
	 * @param id
	 * @param name
	 * @param reg_id
	 * @return
	 */
	public static boolean GCMRegister(final Context context, final Person person, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Registering device with reg_id of \"" + reg_id + "\" with GCM server.");

		// build the JSON data to be sent to the server
		Map<String, String> params = new HashMap<String, String>();
		params.put(JSON_DATA_ID, new Long(person.getId()).toString());
		params.put(JSON_DATA_NAME, person.getName());
		params.put(JSON_DATA_REG_ID, reg_id);

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
				postToServer(SERVER_NEW_REG_ID_ADDRESS, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				contextBroadcast(context, context.getString(R.string.server_registration_success));
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


	/**
	 * @param context
	 * @param reg_id
	 */
	public static void GCMUnregister(final Context context, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Unregistering device with reg_id of \"" + reg_id + "\" from GCM server.");

		Map<String, String> params = new HashMap<String, String>();
		params.put(JSON_DATA_REG_ID, reg_id);

		try
		{
			postToServer(SERVER_REMOVE_REG_ID_ADDRESS, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			contextBroadcast(context, context.getString(R.string.server_unregistration_success));
		}
		catch (final IOException e)
		// At this point the device has been unregistered from GCM but is still registered on
		// the server. We could try to unregister again but it is not necessary. If the server
		// tries to send a message to the device, it will get a "NotRegistered" error message
		// and should unregister the device
		{
			contextBroadcast(context, context.getString(R.string.server_unregistration_fail, e.getMessage()));
		}
	}


}
