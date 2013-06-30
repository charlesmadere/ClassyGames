package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.server.ServerApi;
import com.charlesmadere.android.classygames.server.ServerApiUnregisterFromNotifications;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class UnregisterFromNotificationsActivity extends SherlockActivity
{


	/**
	 * Holds a handle to a currently running (if it's currently running)
	 * ServerApi object.
	 */
	private ServerApiUnregisterFromNotifications serverApiTask;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unregister_from_notifications_activity);
		Utilities.setActionBar(this, R.string.unregister_from_notifications, true);

		final Button unregister = (Button) findViewById(R.id.unregister_from_notifications_activity_button_unregister);
		TypefaceUtilities.applyTypefaceBlueHighway(getAssets(), unregister);

		unregister.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (serverApiTask == null)
				{
					serverApiTask = new ServerApiUnregisterFromNotifications(UnregisterFromNotificationsActivity.this,
						new ServerApi.ServerApiListeners()
						{
							@Override
							public void onCancel()
							{
								serverApiTask = null;
								Utilities.easyToast(UnregisterFromNotificationsActivity.this, R.string.unregistration_cancelled);
								finish();
							}


							@Override
							public void onComplete()
							{
								serverApiTask = null;
								Utilities.easyToast(UnregisterFromNotificationsActivity.this, R.string.unregistration_complete);
								finish();
							}


							@Override
							public void onDismiss()
							{
								serverApiTask = null;
							}
						});

					serverApiTask.execute();
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
	 * Cancels the currently running ServerApiTask (if any).
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (serverApiTask != null)
		{
			serverApiTask.cancel();
		}
	}


}
