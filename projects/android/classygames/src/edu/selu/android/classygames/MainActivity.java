package edu.selu.android.classygames;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

import edu.selu.android.classygames.utilities.Utilities;


public class MainActivity extends SherlockActivity
{


	SharedPreferences sharedPreferences;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		sharedPreferences = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		final String access_token = sharedPreferences.getString(Utilities.FACEBOOK_ACCESS_TOKEN, null);
		final long expires = sharedPreferences.getLong(Utilities.FACEBOOK_EXPIRES, 0);

		if (access_token != null && !access_token.isEmpty())
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
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString(Utilities.FACEBOOK_ACCESS_TOKEN, Utilities.getFacebook().getAccessToken());
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
	public void onResume()
	{
		super.onResume();
		Utilities.getFacebook().extendAccessTokenIfNeeded(MainActivity.this, null);
	}


	private void goToGamesList()
	{
		Utilities.getFacebook().extendAccessTokenIfNeeded(MainActivity.this, null);
		startActivity(new Intent(MainActivity.this, GamesListActivity.class));
		finish();
	}


}
