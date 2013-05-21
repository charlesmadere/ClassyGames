package com.charlesmadere.android.classygames.settings;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;

import java.io.IOException;


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
		Utilities.setActionBar(this, R.string.register_for_notifications, true);

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


		private Context context;
		private ProgressDialog progressDialog;


		private AsyncRegisterForNotifications(final Context context)
		{
			this.context = context;
		}


		@Override
		protected Boolean doInBackground(final Void... params)
		{
			Boolean registrationSuccess = Boolean.valueOf(false);

			if (!isCancelled())
			{
				try
				{
					registrationSuccess = Boolean.valueOf(ServerUtilities.gcmRegister(context));
				}
				catch (final IOException e)
				{
					Log.e(Utilities.LOG_TAG, "IOException in doInBackground() of AsyncRegisterForNotifications class!", e);
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
			Utilities.easyToast(context, R.string.registration_cancelled);
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
				Utilities.easyToast(context, R.string.registration_complete);
				finish();
			}
			else
			{
				final AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setMessage(R.string.register_for_notifications_activity_registration_failed_message)
					.setNeutralButton(R.string.okay, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(final DialogInterface dialog, final int which)
						{
							dialog.dismiss();
							finish();
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
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(getString(R.string.registering_your_device_for_notifications_with_our_server));

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
