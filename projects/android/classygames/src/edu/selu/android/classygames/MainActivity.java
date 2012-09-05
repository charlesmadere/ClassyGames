package edu.selu.android.classygames;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class MainActivity extends SherlockActivity
{


	private SharedPreferences sharedPreferences;


	// facebook data
	private Facebook facebook;

	public final static String FACEBOOK_APP_ID = "324400870964487";
	public final static String FACEBOOK_TOKEN = "access_token";
	public final static String FACEBOOK_EXPIRES = "expires_in";


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		facebook = new Facebook(FACEBOOK_APP_ID);

		sharedPreferences = getPreferences(MODE_PRIVATE);
		final String access_token = sharedPreferences.getString(FACEBOOK_TOKEN, null);
		final long expires = sharedPreferences.getLong(FACEBOOK_EXPIRES, 0);

		if (access_token != null)
		{
			facebook.setAccessToken(access_token);
		}

		if (expires != 0)
		{
			facebook.setAccessExpires(expires);
		}

		if (facebook.isSessionValid())
		// user has already authorized our app. this will skip the login screen and bring them
		// straight to the games list
		{
			goToGamesList();
		}
		else
		{
			Button loginWithFacebook = (Button) findViewById(R.id.login_with_facebook);
			loginWithFacebook.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					facebook.authorize(MainActivity.this, new DialogListener()
					{
						@Override
						public void onComplete(final Bundle values)
						{
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString(FACEBOOK_TOKEN, facebook.getAccessToken());
							editor.putLong(FACEBOOK_EXPIRES, facebook.getAccessExpires());
							editor.commit();
							goToGamesList();
						}


						@Override
						public void onFacebookError(final FacebookError e)
						{
							final String msg = "Facebook has encountered an error. Please try again. \"" + e.toString() + "\"";
							Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
							toast.show();
							Log.d(Utilities.LOG_TAG, msg);
						}


						@Override
						public void onError(final DialogError e)
						{
							final String msg = "An error has ocurred: \"" + e.toString() + "\"";
							Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
							toast.show();
							Log.d(Utilities.LOG_TAG, msg);
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
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


	private void goToGamesList()
	{
		startActivity(new Intent(MainActivity.this, GamesListActivity.class));
		finish();
	}


}
