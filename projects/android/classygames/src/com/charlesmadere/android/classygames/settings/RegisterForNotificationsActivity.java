package com.charlesmadere.android.classygames.settings;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.gcm.GCMManager;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
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
		TypefaceUtilities.applyBlueHighway(register);

		register.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				final SherlockActivity activity = RegisterForNotificationsActivity.this;

				if (GCMManager.checkGooglePlayServices(activity))
				{
					final ProgressDialog progressDialog = new ProgressDialog(activity);
					progressDialog.setCancelable(true);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.setMessage(getString(R.string.thanks_youre_device_is_now_being_registered_for_notifications));
					progressDialog.setTitle(R.string.register_for_notifications);
					progressDialog.show();

					GCMManager.start(getApplicationContext(), new GCMManager.Listener()
					{
						@Override
						public void onRegistrationBegin()
						{}


						@Override
						public void onRegistrationFailure()
						{
							new AlertDialog.Builder(activity)
								.setMessage(R.string.an_error_occurred_when_trying_to_register_for_notifications)
								.setNeutralButton(R.string.okay, new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(final DialogInterface dialog, final int which)
									{
										dialog.dismiss();
									}
								})
								.setTitle(R.string.register_for_notifications)
								.show();
						}


						@Override
						public void onRegistrationSuccess()
						{
							progressDialog.dismiss();
							Toast.makeText(activity, R.string.your_device_is_now_registered, Toast.LENGTH_LONG).show();
							finish();
						}
					});
				}
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


}
