package com.charlesmadere.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class SettingsActivity extends SherlockPreferenceActivity
{


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		// https://developer.android.com/guide/topics/ui/settings.html
		addPreferencesFromResource(R.xml.settings);
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
