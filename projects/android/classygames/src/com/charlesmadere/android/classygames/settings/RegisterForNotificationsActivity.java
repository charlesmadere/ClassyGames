package com.charlesmadere.android.classygames.settings;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.BaseActivity;
import com.charlesmadere.android.classygames.R;
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
		if (serverApiTask == null)
		{
			final Context context = this;

			serverApiTask = new ServerApiRegister(this, new ServerApiRegister.RegisterListeners()
			{
				@Override
				public void onRegistrationFail()
				{
					Toast.makeText(context, R.string.sorry_but_your_device_is_not_compatible_with_push_notifications, Toast.LENGTH_LONG).show();
					finish();
				}


				@Override
				public void onRegistrationSuccess()
				{
					Toast.makeText(context, R.string.registration_complete, Toast.LENGTH_SHORT).show();
					finish();
				}


				@Override
				public void onCancel()
				{
					finish();
				}


				@Override
				public void onComplete(final String serverResponse)
				{}


				@Override
				public void onDismiss()
				{}
			});

			serverApiTask.execute(false);
		}
	}


}
