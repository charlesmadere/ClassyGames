package edu.selu.android.classygames;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

import edu.selu.android.classygames.utilities.Utilities;


public class LogoutActivity extends SherlockActivity
{


	public final static int LOGGED_OUT = 16;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		Button logoutOfFacebook = (Button) findViewById(R.id.logout_activity_button_logout_of_facebook);
		logoutOfFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				new AsyncFacebookRunner(Utilities.getFacebook()).logout(LogoutActivity.this, new RequestListener()
				{
					@Override
					public void onComplete(final String response, final Object state)
					{
						// token expiring code taken mainly from the facebook documentation
						// https://developers.facebook.com/docs/mobile/android/build/#logout
						SharedPreferences.Editor editor = getSharedPreferences(Utilities.SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit();
						editor.remove(Utilities.FACEBOOK_ACCESS_TOKEN);
						editor.remove(Utilities.FACEBOOK_EXPIRES);
						editor.commit();

						setResult(LOGGED_OUT);

						Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);

						finish();
					}


					@Override
					public void onFacebookError(final FacebookError e, final Object state)
					{

					}


					@Override
					public void onFileNotFoundException(final FileNotFoundException e, final Object state)
					{

					}


					@Override
					public void onIOException(final IOException e, final Object state)
					{

					}


					@Override
					public void onMalformedURLException(final MalformedURLException e, final Object state)
					{

					}
				});
			}
		});
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
