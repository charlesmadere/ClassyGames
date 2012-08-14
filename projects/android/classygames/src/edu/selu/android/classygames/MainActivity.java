package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;


public class MainActivity extends SherlockActivity
{


	public static final String LOG_TAG = "ClassyGames";
	private Button loginWithFacebook;


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
				Log.d(LOG_TAG, "Logged in with Facebook!");
				startActivity(new Intent(MainActivity.this, GamesListActivity.class));
			}
		});
	}


}
