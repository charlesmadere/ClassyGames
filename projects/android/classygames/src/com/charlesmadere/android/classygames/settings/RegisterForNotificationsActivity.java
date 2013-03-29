package com.charlesmadere.android.classygames.settings;


import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class RegisterForNotificationsActivity extends SherlockActivity
{


	/**
	 * Used to send notification registration server API request.
	 */
	private AsyncRegisterForNotifications asyncRegisterForNotifications;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_for_notifications_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		final Button registerButton = (Button) findViewById(R.id.register_for_notifications_activity_register);
		registerButton.setTypeface(TypefaceUtilities.getTypeface(getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));
		registerButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (!isAnAsyncTaskRunning())
				{
					asyncRegisterForNotifications = new AsyncRegisterForNotifications(RegisterForNotificationsActivity.this);
					asyncRegisterForNotifications.execute();
				}
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




	/**
	 * Cancels the currently running AsyncTask (if any).
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			asyncRegisterForNotifications.cancel(true);
		}
	}


	/**
	 * @return
	 * Returns true if the asyncRegisterForNotifications AsyncTask is currently
	 * running.
	 */
	private boolean isAnAsyncTaskRunning()
	{
		return asyncRegisterForNotifications != null;
	}




	private final class AsyncRegisterForNotifications extends AsyncTask<Void, Void, Boolean>
	{


		private SherlockActivity activity;
		private ProgressDialog progressDialog;


		private AsyncRegisterForNotifications(final SherlockActivity activity)
		{
			this.activity = activity;
		}


		@Override
		protected Boolean doInBackground(final Void... params)
		{
			Boolean registrationSuccess = Boolean.valueOf(false);

			if (!isCancelled())
			{
				try
				{
					registrationSuccess = Boolean.valueOf(ServerUtilities.gcmRegister(activity));
				}
				catch (final IOException e)
				{

				}
			}

			return registrationSuccess;
		}


		private void cancelled()
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			asyncRegisterForNotifications = null;
			Utilities.easyToast(activity, R.string.registration_cancelled);
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final Boolean registrationSuccess)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final Boolean registrationSuccess)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			if (registrationSuccess.booleanValue())
			{
				Utilities.easyToast(activity, R.string.registration_complete);
				activity.finish();
			}
			else
			{
				final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
					.setMessage(R.string.register_for_notifications_activity_registration_failed_message)
					.setNeutralButton(R.string.okay, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(final DialogInterface dialog, final int which)
						{
							dialog.dismiss();
							activity.finish();
						}
					})
					.setTitle(R.string.registration_failed);

				builder.show();
			}

			asyncRegisterForNotifications = null;
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(activity);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(activity.getString(R.string.registering_your_device_for_notifications_with_our_server));

			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncRegisterForNotifications.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.register_for_notifications);
			progressDialog.show();
		}


	}




}
