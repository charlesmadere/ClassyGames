package edu.selu.android.classygames;


import android.content.Intent;
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


	public final static String LOG_TAG = "ClassyGames";


	private final String FACEBOOK_APP_ID = "324400870964487";
	private Facebook facebook;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		Button loginWithFacebook = (Button) findViewById(R.id.login_with_facebook);
		loginWithFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				facebook = new Facebook(FACEBOOK_APP_ID);
				facebook.authorize(MainActivity.this, new DialogListener()
				{
					@Override
					public void onComplete(final Bundle values)
					{
						startActivity(new Intent(MainActivity.this, GamesListActivity.class));
					}


					@Override
					public void onFacebookError(final FacebookError e)
					{
						final String msg = "Facebook has encountered an error. Please try again. \"" + e.toString() + "\"";
						Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
						toast.show();
						Log.d(LOG_TAG, msg);
					}


					@Override
					public void onError(final DialogError e)
					{
						final String msg = "An error has ocurred: \"" + e.toString() + "\"";
						Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
						toast.show();
						Log.d(LOG_TAG, msg);
					}


					@Override
					public void onCancel()
					{

					}
				});
			}
		});
	}


	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}


}
