package com.charlesmadere.android.classygames.settings;


import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.charlesmadere.android.classygames.BaseActivity;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.gcm.GCMManager;
import com.charlesmadere.android.classygames.server.ServerApi;
import com.charlesmadere.android.classygames.server.ServerApiRegister;


public final class RegisterForNotificationsActivity extends BaseActivity
{


	private ServerApiRegister serverApiTask;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, R.string.register_for_notifications, true);
		setContentView(R.layout.register_for_notifications_activity);

		final Button register = (Button) findViewById(R.id.register_for_notifications_activity_register);
		register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				register();
			}
		});
	}


	@Override
	public void onBackPressed()
	{
		cancelRunningAnyAsyncTask();
		super.onBackPressed();
	}


	@Override
	protected void onDestroy()
	{
		cancelRunningAnyAsyncTask();
		super.onDestroy();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	private void cancelRunningAnyAsyncTask()
	{
		if (serverApiTask != null)
		{
			serverApiTask.cancel();
		}
	}


	private void register()
	{
		final Activity activity = this;

		if (GCMManager.checkGooglePlayServices(activity, true) && serverApiTask == null)
		{
			serverApiTask = new ServerApiRegister(this, new ServerApi.Listeners()
			{
				@Override
				public void onCancel()
				{
					finish();
				}


				@Override
				public void onComplete(final String serverResponse)
				{
					Toast.makeText(activity, R.string.registration_complete, Toast.LENGTH_SHORT).show();
					finish();
				}


				@Override
				public void onDismiss()
				{}
			});

			serverApiTask.execute(false);
		}
	}


}
