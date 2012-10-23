package edu.selu.android.classygames;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.android.gcm.GCMRegistrar;


public class MainActivity extends SherlockActivity
{


	private AsyncTask<Void, Void, Void> registerTask;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		/* set up Google Cloud Messaging (GCM) Stuff */

		// make sure that the device ahs the proper dependencies
		GCMRegistrar.checkDevice(MainActivity.this);

		// make sure that the manifest was properly set
		GCMRegistrar.checkManifest(MainActivity.this);

		// grab current registration ID. we may not already have one
		final String regId = GCMRegistrar.getRegistrationId(MainActivity.this);

		if (regId.equals(""))
		// if we do not have a registration ID then we'll need to register with the server with
		// the below code
		{
			// automatically registers application with GCM on startup
			GCMRegistrar.register(this, SecretConstants.GOOGLE_PROJECT_ID);
		}
		else
		// if we're here then that means that this device has already been registered with the GCM
		// server
		{
			if (GCMRegistrar.isRegisteredOnServer(MainActivity.this))
			// check to see if this device is registed on the classy games server
			{

			}
			else
			{
				registerTask = new AsyncTask<Void, Void, Void>()
				{
					@Override
					protected Void doInBackground(final Void... params)
					{
						if (!Utilities.GCMRegister(MainActivity.this, regId))
						{
							GCMRegistrar.unregister(MainActivity.this);
						}

						return null;
					}

					@Override
					protected void onPostExecute(final Void result)
					{
						registerTask = null;
					}
				};

				registerTask.execute();
			}
		}

		/* end GCM stuff */

		Utilities.sharedPreferences = getPreferences(MODE_PRIVATE);
		final String access_token = Utilities.sharedPreferences.getString(Utilities.FACEBOOK_TOKEN, null);
		final long expires = Utilities.sharedPreferences.getLong(Utilities.FACEBOOK_EXPIRES, 0);

		if (access_token != null)
		{
			Utilities.getFacebook().setAccessToken(access_token);
		}

		if (expires != 0)
		{
			Utilities.getFacebook().setAccessExpires(expires);
		}

		if (Utilities.getFacebook().isSessionValid())
		// user has already authorized our app. this will skip the login screen and bring them
		// straight to the games list
		{
			goToGamesList();
		}
		else
		{
			Button loginWithFacebook = (Button) findViewById(R.id.main_activity_button_login_with_facebook);
			loginWithFacebook.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					Utilities.getFacebook().authorize(MainActivity.this, new DialogListener()
					{
						@Override
						public void onComplete(final Bundle values)
						{
							SharedPreferences.Editor editor = Utilities.sharedPreferences.edit();
							editor.putString(Utilities.FACEBOOK_TOKEN, Utilities.getFacebook().getAccessToken());
							editor.putLong(Utilities.FACEBOOK_EXPIRES, Utilities.getFacebook().getAccessExpires());
							editor.commit();
							goToGamesList();
						}


						@Override
						public void onFacebookError(final FacebookError e)
						{
							Utilities.easyToastAndLog(MainActivity.this, "Facebook has encountered an error. Please try again. \"" + e.toString() + "\"");
						}


						@Override
						public void onError(final DialogError e)
						{
							Utilities.easyToastAndLog(MainActivity.this, "An error has ocurred: \"" + e.toString() + "\"");
						}


						@Override
						public void onCancel()
						{

						}
					});
				}
			});
		}
	}


	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Utilities.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}


	@Override
	public void onDestroy()
	{
		if (registerTask != null)
		{
			registerTask.cancel(true);
		}

		unregisterReceiver(messageReceiver);
		GCMRegistrar.onDestroy(MainActivity.this);
		super.onDestroy();
	}


	@Override
	public void onResume()
	{
		super.onResume();
		Utilities.getFacebook().extendAccessTokenIfNeeded(MainActivity.this, null);
	}


	private final BroadcastReceiver messageReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(final Context context, final Intent intent)
		{
			
		}
	};


	private void goToGamesList()
	{
		startActivity(new Intent(MainActivity.this, GamesListActivity.class));
		finish();
	}


}
