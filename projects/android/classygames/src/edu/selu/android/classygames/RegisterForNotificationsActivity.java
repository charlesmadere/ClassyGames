package edu.selu.android.classygames;


import android.app.ProgressDialog;
import android.content.Context;
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
				new AsyncRegisterForNotifications(RegisterForNotificationsActivity.this).execute();
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
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}




	private final class AsyncRegisterForNotifications extends AsyncTask<Void, Void, Void>
	{


		private Context context;
		private ProgressDialog progressDialog;


		AsyncRegisterForNotifications(final Context context)
		{
			this.context = context;
		}


		@Override
		protected Void doInBackground(final Void... params)
		{
			if (!isCancelled())
			{
				ServerUtilities.gcmPerformRegister(context);
			}

			return null;
		}


		private void cancelled()
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			Utilities.easyToastAndLog(context, context.getString(R.string.register_for_notifications_activity_registration_cancelled));
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final Void result)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final Void result)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			Utilities.easyToastAndLog(context, context.getString(R.string.register_for_notifications_activity_registration_complete));
			finish();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(context.getString(R.string.register_for_notifications_activity_progressdialog_message));

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
