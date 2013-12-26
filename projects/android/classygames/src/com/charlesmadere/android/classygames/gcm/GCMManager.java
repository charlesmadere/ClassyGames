package com.charlesmadere.android.classygames.gcm;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public final class GCMManager
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GCMManager";

	// The Google API Console can be found at this website:
	// https://code.google.com/apis/console/b/0/
	private final static String GOOGLE_PROJECT_ID = "246279743841";


	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private final static String PREFERENCES_NAME = "GCMManager_Preferences";
	private final static String KEY_REGISTRATION_ID = "KEY_REGISTRATION_ID";
	private final static String KEY_REGISTRATION_VERSION_CODE = "KEY_REGISTRATION_VERSION_CODE";

	private static int registrationVersionCode;
	private static String registrationId;


	/**
	 * Checks the device to make sure that it has a compatible and up-to-date
	 * Google Play services installation. Read more about what it means to have
	 * a compatible and up-to-date Google Play services installation here:
	 * https://developer.android.com/google/play-services/setup.html#ensure
	 *
	 * @param activity
	 * The Activity that you're calling this method from.
	 *
	 * @param showErrorDialog
	 * This method has the capability of showing the user an error dialog if
	 * their Google Play Services installation is not ready to be used. Set
	 * this to true if you want that error dialog to show up when necessary.
	 *
	 * @return
	 * Returns true if this device is ready to go with Google Play services.
	 */
	public static boolean checkGooglePlayServices(final Activity activity, final boolean showErrorDialog)
	{
		final int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());
		boolean googlePlayServicesAreAvailable;

		if (resultCode == ConnectionResult.SUCCESS)
		{
			googlePlayServicesAreAvailable = true;
		}
		else
		{
			googlePlayServicesAreAvailable = false;

			if (showErrorDialog)
			{
				if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
				{
					GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
				}
				else
				{
					Log.i(LOG_TAG, "This device doesn't support Google Play Services at all!");
				}
			}
		}

		return googlePlayServicesAreAvailable;
	}


	private static SharedPreferences getPreferences(final Context context)
	{
		return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}


	public static String getRegistrationId(final Context context)
	{
		if (!Utilities.validString(registrationId))
		{
			registrationId = getPreferences(context).getString(KEY_REGISTRATION_ID, null);

			if ((!Utilities.validString(registrationId) || getRegistrationVersionCode(context) == 0)
				&& Looper.myLooper() != Looper.getMainLooper())
			// Only attempt to perform GCM registration if we're NOT on the
			// Android UI thread. More on this topic here:
			// http://stackoverflow.com/questions/1845678/android-ui-thread
			{
				performGCMRegistration(context);
			}
		}

		return registrationId;
	}


	private static int getRegistrationVersionCode(final Context context)
	{
		if (registrationVersionCode == 0)
		{
			registrationVersionCode = getPreferences(context).getInt(KEY_REGISTRATION_VERSION_CODE, 0);
		}

		return registrationVersionCode;
	}


	private static void performGCMRegistration(final Context context)
	{
		Log.i(LOG_TAG, "About to attempt GCM registration.\n" +
			"User is running Android API version \"" + Build.VERSION.SDK_INT + "\".\n" +
			"Device manufacturer is \"" + Build.MANUFACTURER + "\" and device model is \"" + Build.MODEL + "\".");

		try
		{
			final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
			registrationId = gcm.register(GOOGLE_PROJECT_ID);
			registrationVersionCode = Utilities.getAppVersionCode(context);
		}
		catch (final IOException e)
		{
			// In the event of an IOException, let's just discard all of
			// the user's GCM data that we've grabbed.
			Log.e(LOG_TAG, "IOException during ServerApiRegister's postToServer()!", e);
			registrationId = null;
			registrationVersionCode = 0;
		}

		final SharedPreferences.Editor editor = getPreferences(context).edit();

		if (Utilities.validString(registrationId) && registrationVersionCode >= 1)
		{
			editor.putString(KEY_REGISTRATION_ID, registrationId)
				.putInt(KEY_REGISTRATION_VERSION_CODE, registrationVersionCode);

			Log.i(LOG_TAG, "GCM registration completed successfully.\n" +
				"registrationId: \"" + registrationId + "\"\n" +
				"registrationVersionCode: \"" + registrationVersionCode + "\"");
		}
		else
		{
			editor.clear();
			Log.i(LOG_TAG, "GCM registration failed!");
		}

		// In FriendsListFragment I have a big comment that very simply
		// discusses the differences in the apply() and commit() methods
		// below. Go check that comment out if you want more info!

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			editor.apply();
		}
		else
		{
			editor.commit();
		}
	}


}
