package edu.selu.android.classygames;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class LogoutActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logout_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Button logoutOfFacebook = (Button) findViewById(R.id.logout_activity_button_logout_of_facebook);
		logoutOfFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Utilities.easyToastAndLog(LogoutActivity.this, "Logged out zzz");
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
