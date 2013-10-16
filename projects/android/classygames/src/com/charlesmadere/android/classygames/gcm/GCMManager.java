package com.charlesmadere.android.classygames.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.ApiData;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public final class GCMManager
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GCMManager";

	// The Google API Console can be found at this website:
	// https://code.google.com/apis/console/b/0/
	public final static String GOOGLE_PROJECT_ID = "246279743841";

	private final static String KEY_REGISTRATION_ID = "KEY_REGISTRATION_ID";
	private final static String KEY_REGISTRATION_VERSION_CODE = "KEY_REGISTRATION_VERSION_CODE";
	private final static String PREFERENCES_NAME = "GCMManager_Preferences";

	private static AsyncGCMRegister asyncGCMRegister;


	public interface Listener
	{
		/**
		 * Called right before the registration process begins.
		 */
		public void onRegistrationBegin();


		/**
		 * Called at the end of the registration process if there was an error.
		 */
		public void onRegistrationFailure();


		/**
		 * Called at the end of the registration process if there was no error.
		 */
		public void onRegistrationSuccess();
	}


	private static String getRegistrationId(final Context context)
	{
		final SharedPreferences sPreferences = getPreferences(context);
		return sPreferences.getString(KEY_REGISTRATION_ID, null);
	}


	private static int getRegistrationVersionCode(final Context context)
	{
		final SharedPreferences sPreferences = getPreferences(context);
		return sPreferences.getInt(KEY_REGISTRATION_VERSION_CODE, 0);
	}


	private static SharedPreferences getPreferences(final Context context)
	{
		return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}


	/**
	 * If a Google Cloud Messaging registration attempt is not already running,
	 * then this will begin that registration process.
	 */
	private static void register(final Context context, final Listener listener)
	{
		if (asyncGCMRegister == null)
		{
			asyncGCMRegister = new AsyncGCMRegister(context, listener);
			asyncGCMRegister.execute();
		}
	}


	/**
	 * ALERT! ALERT! Please wrap calls to this method inside an if statement
	 * that checks the output of the checkGooglePlayServices() method. This
	 * start() method should only be run in the case that the
	 * checkGooglePlayServices() method returns true.
	 *
	 * So now that you know that, let's go over what this method does...
	 *
	 * Runs any code necessary to start the Google Cloud Messaging service.
	 * It's possible that this method will do practically nothing (like if the
	 * user has already logged in to the app before). But if this is the user's
	 * maiden voyage, then this method will cause the device to register with
	 * Google's GCM servers. Or, the device may have already registered, but
	 * the app has since updated, and so now the user has an old registration
	 * ID that needs to be renewed (which will also cause the device to
	 * register with Google's GCM servers.).
	 *
	 * @param context
	 * Please pass in the Application Context for this parameter. Do not pass
	 * in just an Activity's Context! This service will be long running, so
	 * it's best to use the Application Context.
	 *
	 * @param listener
	 * Create a new instance of this class's Listener interface and pass it in
	 * here. Or you can use null if you don't need to take advantage of the
	 * registration lifecycle.
	 */
	public static void start(final Context context, final Listener listener)
	{
		final String registrationId = getRegistrationId(context);
		final int registrationVersionCode = getRegistrationVersionCode(context);
		final int appVersionCode = Utilities.getAppVersionCode(context);

		if (!Utilities.validString(registrationId) || registrationVersionCode == 0 ||
			appVersionCode != registrationVersionCode)
		// If any single one of these if statements validate as true, then we
		// absolutely must register this device with Google's GCM servers.
		{
			register(context, listener);
		}
	}




	private static final class AsyncGCMRegister extends AsyncTask<Void, Void, String>
	{


		private Context context;
		private Listener listener;


		private AsyncGCMRegister(final Context context, final Listener listener)
		{
			this.context = context;
			this.listener = listener;
		}


		@Override
		protected String doInBackground(final Void... v)
		{
			final SharedPreferences.Editor editor = getPreferences(context).edit();
			String registrationId;

			try
			{
				final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
				registrationId = gcm.register(GOOGLE_PROJECT_ID);
				final int registrationVersionCode = Utilities.getAppVersionCode(context);

				editor.putString(KEY_REGISTRATION_ID, registrationId)
					.putInt(KEY_REGISTRATION_VERSION_CODE, registrationVersionCode);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException during GCMManager's register()!", e);
				registrationId = null;

				editor.remove(KEY_REGISTRATION_ID)
					.remove(KEY_REGISTRATION_VERSION_CODE);
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			{
				editor.apply();
			}
			else
			{
				editor.commit();
			}

			try
			{
				final Person whoAmI = Utilities.getWhoAmI(context);

				final ApiData data = new ApiData()
					.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId())
					.addKeyValuePair(Server.POST_DATA_NAME, whoAmI.getName());

				if (Utilities.validString(registrationId))
				{
					data.addKeyValuePair(Server.POST_DATA_REG_ID, registrationId);
				}

				Server.postToServerNewRegId(data);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException during GCMManager's postToServer()!", e);
			}

			return registrationId;
		}


		@Override
		protected void onPreExecute()
		{
			Log.d(LOG_TAG, "GCM registration starting now...");

			if (listener != null)
			{
				listener.onRegistrationBegin();
			}
		}


		@Override
		protected void onPostExecute(final String registrationId)
		{
			if (listener != null)
			{
				if (Utilities.validString(registrationId))
				{
					listener.onRegistrationSuccess();
				}
				else
				{
					listener.onRegistrationFailure();
				}
			}

			asyncGCMRegister = null;
			Log.d(LOG_TAG, "GCM registration finished!");
		}


	}




}
