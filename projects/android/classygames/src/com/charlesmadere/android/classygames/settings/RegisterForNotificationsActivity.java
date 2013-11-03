package com.charlesmadere.android.classygames.settings;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.server.ServerApiRegister;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class RegisterForNotificationsActivity extends SherlockActivity
{


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_for_notifications_activity);
		Utilities.setActionBar(this, R.string.register_for_notifications, true);

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


	private void register()
	{
		final Context context = this;

		final ServerApiRegister serverApiTask = new ServerApiRegister(this, new ServerApiRegister.RegisterListeners()
		{
			@Override
			public void onRegistrationFail()
			{
				Toast.makeText(context, R.string.sorry_but_your_device_is_not_compatible_with_push_notifications, Toast.LENGTH_LONG).show();
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
