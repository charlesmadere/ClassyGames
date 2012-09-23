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


public class LogoutActivity extends SherlockActivity
{


	public final static int LOGGED_OUT = 1;



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
						SharedPreferences.Editor editor = Utilities.sharedPreferences.edit();
						editor.putString(Utilities.FACEBOOK_TOKEN, Utilities.getFacebook().getAccessToken());
						editor.putLong(Utilities.FACEBOOK_EXPIRES, Utilities.getFacebook().getAccessExpires());
						editor.commit();

						setResult(LOGGED_OUT);
						startActivity(new Intent(LogoutActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
						finish();
					}

					@Override
					public void onIOException(final IOException e, final Object state)
					{

					}

					@Override
					public void onFileNotFoundException(final FileNotFoundException e, final Object state)
					{

					}

					@Override
					public void onMalformedURLException(final MalformedURLException e, final Object state)
					{

					}

					@Override
					public void onFacebookError(final FacebookError e, final Object state)
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
