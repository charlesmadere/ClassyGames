package com.charlesmadere.android.classygames.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import com.actionbarsherlock.app.SherlockActivity;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.ApiData;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.utilities.KeysAndConstants;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public final class GCMManager
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GCMManager";

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private final static String KEY_REGISTRATION_ID = "KEY_REGISTRATION_ID";
	private final static String KEY_REGISTRATION_VERSION_CODE = "KEY_REGISTRATION_VERSION_CODE";
	private final static String PREFERENCES_NAME = "GCMManager_Preferences";

	private static AsyncGCMRegister asyncGCMRegister;


	/**
	 * Checks the device to make sure that it has a compatible and up-to-date
	 * Google Play services installation. If it doesn't display a dialog that
	 * allows users to download the API from the Google Play Store or enable it
	 * in the device's system settings.
	 *
	 * @param activity
	 * The Activity that you're calling this method from.
	 *
	 * @param showErrorDialog
	 * Set this to true if you want the user to see an error dialog in the
	 * event that their device is not fully compatible with Google Play
	 * services. Set this to false if you don't want this error dialog to
	 * appear.
	 *
	 * @return
	 * Returns true if this device is ready to go with Google Play services.
	 */
	public static boolean checkGooglePlayServices(final SherlockActivity activity, final boolean showErrorDialog)
	{
		final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());

		if (resultCode != ConnectionResult.SUCCESS)
		{
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
			{
				if (showErrorDialog)
				{
					try
					{
						GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
					}
					catch (final Exception e)
					{
						Log.e(LOG_TAG, "Exception encountered in GCMManager's checkGooglePlayServices()!", e);
					}
				}
			}
			else
			{
				Log.i(LOG_TAG, "This device does not support GCM.");
			}

			return false;
		}

		return true;
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
	private static void register(final Context context)
	{
		if (asyncGCMRegister == null)
		{
			asyncGCMRegister = new AsyncGCMRegister(context);
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
	 */
	public static void start(final Context context)
	{
		final String registrationId = getRegistrationId(context);
		final int registrationVersionCode = getRegistrationVersionCode(context);
		final int appVersionCode = Utilities.getAppVersionCode(context);

		if (!Utilities.verifyValidString(registrationId) || registrationVersionCode == 0 ||
			appVersionCode != registrationVersionCode)
		// If any single one of these if statements validate as true, then we
		// absolutely must register this device with Google's GCM servers.
		{
			register(context);
		}
	}




	private static final class AsyncGCMRegister extends AsyncTask<Void, Void, Void>
	{


		private Context context;


		private AsyncGCMRegister(final Context context)
		{
			this.context = context;
		}


		@Override
		protected Void doInBackground(final Void... v)
		{
			final SharedPreferences.Editor editor = getPreferences(context).edit();
			final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

			try
			{
				final String registrationId = gcm.register(KeysAndConstants.GOOGLE_PROJECT_ID);
				final int registrationVersionCode = Utilities.getAppVersionCode(context);
				final Person whoAmI = Utilities.getWhoAmI(context);

				final ApiData data = new ApiData()
					.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId())
					.addKeyValuePair(Server.POST_DATA_NAME, whoAmI.getName())
					.addKeyValuePair(Server.POST_DATA_REG_ID, registrationId);

				Server.postToServerNewRegId(data);

				editor.putString(KEY_REGISTRATION_ID, registrationId)
					.putInt(KEY_REGISTRATION_VERSION_CODE, registrationVersionCode);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException during GCMManager's register()!", e);

				editor.remove(KEY_REGISTRATION_ID)
					.remove(KEY_REGISTRATION_VERSION_CODE);
			}

			editor.commit();
			return null;
		}


		@Override
		protected void onPreExecute()
		{
			Log.d(LOG_TAG, "GCM registration starting now...");
		}


		@Override
		protected void onPostExecute(final Void v)
		{
			asyncGCMRegister = null;
			Log.d(LOG_TAG, "GCM registration finished!");
		}


	}




}
