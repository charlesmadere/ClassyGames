package edu.selu.android.classygames;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class MainActivity extends SherlockActivity
{


	public static final String LOG_TAG = "ClassyGames";
	private Button loginWithFacebook;
	AlertDialog.Builder alertDialogBuilder;

	// Facebook variables and data here
	public final String FACEBOOK_APP_ID = "324400870964487";
	public Facebook facebook;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		loginWithFacebook = (Button) findViewById(R.id.loginWithFacebook);
		loginWithFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				startActivity(new Intent(MainActivity.this, GamesListActivity.class));
			}
		});

		alertDialogBuilder = new AlertDialog.Builder(this);

		facebook = new Facebook(FACEBOOK_APP_ID);
		facebook.authorize(this, new DialogListener()
		{
			@Override
			public void onComplete(final Bundle values)
			{
				alertDialogBuilder.setMessage("onComplete");
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
			}


			@Override
			public void onFacebookError(final FacebookError e)
			{
				alertDialogBuilder.setMessage("onFacebookError");
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
			}


			@Override
			public void onError(final DialogError e)
			{
				alertDialogBuilder.setMessage("onError");
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
			}


			@Override
			public void onCancel()
			{
				alertDialogBuilder.setMessage("onCancel");
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();
			}
		});
	}


	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);

		alertDialogBuilder.setMessage("onActivityResult");
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}


}
