package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;


public final class ServerApiRegister extends ServerApi
{


	private final static String LOG_TAG = ServerApi.LOG_TAG + " - ServerApiRegister";

	// The Google API Console can be found at this website:
	// https://code.google.com/apis/console/b/0/
	public final static String GOOGLE_PROJECT_ID = "246279743841";

	private final static String KEY_REGISTRATION_ID = "KEY_REGISTRATION_ID";
	private final static String KEY_REGISTRATION_VERSION_CODE = "KEY_REGISTRATION_VERSION_CODE";
	private final static String PREFERENCES_NAME = "ServerApiRegister_Preferences";


	private SharedPreferences sPreferences;


	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * RegisterListeners interface.
	 */
	private RegisterListeners listeners;


	/**
	 * An interface that will be used throughout the lifecycle of this class.
	 */
	public interface RegisterListeners extends Listeners
	{


		/**
		 * If our attempt to register with the server fails then this method
		 * will be run. Note that this method <strong>will not run</strong> if
		 * the user's device just turns out to be incompatible with Google
		 * Cloud Messaging.
		 */
		public void onRegistrationFail();


		/**
		 * This method will be run once we have checked and verified that the
		 * user has successfully registered with the server.
		 */
		public void onRegistrationSuccess();


	}




	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	public ServerApiRegister(final Context context, final RegisterListeners listeners)
	{
		super(context, listeners);
	}


	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. This constructor also allows you to specify whether or not the
	 * user should see a ProgressDialog popup while this ServerApi object is
	 * running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param showProgressDialog
	 * Set this to true if you want the user to see a ProgressDialog while this
	 * ServerApi object is running.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	public ServerApiRegister(final Context context, final boolean showProgressDialog, final RegisterListeners listeners)
	{
		super(context, showProgressDialog, listeners);
	}


	private String getRegistrationId()
	{
		return getSharedPreferences().getString(KEY_REGISTRATION_ID, null);
	}


	public int getRegistrationVersionCode()
	{
		return getSharedPreferences().getInt(KEY_REGISTRATION_VERSION_CODE, 0);
	}


	@Override
	protected ServerApiTask getServerApiTask()
	{
		return new ServerApiRegisterTask();
	}


	private SharedPreferences getSharedPreferences()
	{
		if (sPreferences == null)
		{
			sPreferences = getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		}

		return sPreferences;
	}


	@Override
	protected String postToServer(final Person whoAmI)
	{
		String registrationId;
		int registrationVersionCode;

		try
		{
			final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getContext());
			registrationId = gcm.register(GOOGLE_PROJECT_ID);
			registrationVersionCode = Utilities.getAppVersionCode(getContext());
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException during ServerApiRegister's postToServer()!", e);
			registrationId = null;
			registrationVersionCode = 0;
		}

		final SharedPreferences.Editor editor = getSharedPreferences().edit();

		if (Utilities.validString(registrationId) && registrationVersionCode >= 1)
		{
			editor.putString(KEY_REGISTRATION_ID, registrationId)
				.putInt(KEY_REGISTRATION_VERSION_CODE, registrationVersionCode);
		}
		else
		{
			editor.clear();
		}

		// In FriendsListFragment I have a big comment that simply discusses
		// the difference in apply() and commit() methods below. Go check that
		// out for more info!

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




	/**
	 * An AsyncTask that will query the Classy Games server.
	 */
	protected class ServerApiRegisterTask extends ServerApiTask
	{


		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			super.onPostExecute(serverResponse);
		}


	}




}
