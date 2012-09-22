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


public class MainActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

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


	private void goToGamesList()
	{
		startActivity(new Intent(MainActivity.this, GamesListActivity.class));
		finish();
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


}
