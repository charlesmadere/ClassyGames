package edu.selu.android.classygames;


import java.io.IOException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.TypefaceUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class RegisterForNotificationsActivity extends SherlockActivity
{


	private boolean isAsyncRegisterForNotificationsRunning = false;
	private AsyncRegisterForNotifications asyncRegisterForNotifications;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_for_notifications_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		final Button registerButton = (Button) findViewById(R.id.register_for_notifications_activity_register);
		registerButton.setTypeface(TypefaceUtilities.getTypeface(getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));
		registerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				asyncRegisterForNotifications = new AsyncRegisterForNotifications(RegisterForNotificationsActivity.this);
				asyncRegisterForNotifications.execute();
			}
		});
	}


	@Override
	protected void onDestroy()
	{
		if (isAsyncRegisterForNotificationsRunning)
		{
			asyncRegisterForNotifications.cancel(true);
		}

		super.onDestroy();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}




	private final class AsyncRegisterForNotifications extends AsyncTask<Void, Void, Boolean>
	{


		private SherlockActivity activity;
		private ProgressDialog progressDialog;


		AsyncRegisterForNotifications(final SherlockActivity activity)
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

			isAsyncRegisterForNotificationsRunning = false;
			Utilities.easyToastAndLog(activity, activity.getString(R.string.register_for_notifications_activity_registration_cancelled));
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
				Utilities.easyToastAndLog(activity, activity.getString(R.string.register_for_notifications_activity_registration_complete));
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
					.setTitle(R.string.register_for_notifications_activity_registration_failed_title);

				builder.show();
			}

			isAsyncRegisterForNotificationsRunning = false;
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncRegisterForNotificationsRunning = true;

			progressDialog = new ProgressDialog(activity);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(activity.getString(R.string.register_for_notifications_activity_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncRegisterForNotifications.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.register_for_notifications_activity_progressdialog_title);
			progressDialog.show();
		}


	}


}
