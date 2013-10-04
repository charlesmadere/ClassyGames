package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.server.ServerApi;
import com.charlesmadere.android.classygames.server.ServerApiUnregisterFromNotifications;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class UnregisterFromNotificationsActivity extends SherlockActivity
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
		TypefaceUtilities.applyBlueHighway(unregister);

		unregister.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				if (serverApiTask == null)
				{
					serverApiTask = new ServerApiUnregisterFromNotifications(UnregisterFromNotificationsActivity.this,
						new ServerApi.Listeners()
						{
							@Override
							public void onCancel()
							{
								serverApiTask = null;
								Toast.makeText(UnregisterFromNotificationsActivity.this, R.string.unregistration_cancelled, Toast.LENGTH_SHORT).show();
								finish();
							}


							@Override
							public void onComplete(final String serverResponse)
							{
								serverApiTask = null;
								Toast.makeText(UnregisterFromNotificationsActivity.this, R.string.unregistration_complete, Toast.LENGTH_SHORT).show();
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
