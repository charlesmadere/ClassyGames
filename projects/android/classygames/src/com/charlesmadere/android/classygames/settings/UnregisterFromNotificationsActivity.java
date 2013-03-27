package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class UnregisterFromNotificationsActivity extends SherlockActivity
{


	




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unregister_from_notifications_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);
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
