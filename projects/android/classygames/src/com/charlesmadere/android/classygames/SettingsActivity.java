package com.charlesmadere.android.classygames;


import java.util.List;

import android.os.Build;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class SettingsActivity extends SherlockPreferenceActivity
{


	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		final String action = getIntent().getAction();

		// The addPreferencesFromResource methods below are causing some
		// deprecation warnings. In this case, the fact that they're here is
		// fine. They have to be used if the running version of Android is
		// below Honeycomb (v3.0).

		if (action != null &&
			action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_miscellaneous)))
		{
			addPreferencesFromResource(R.xml.settings_miscellaneous);
		}
		else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		// For Android devices running any version below Honeycomb. 
		{
			addPreferencesFromResource(R.xml.settings_headers_legacy);
		}
	}


	@Override
	public void onBuildHeaders(final List<Header> target)
	// Called only when this Android device is running Honeycomb and above.
	{
		super.onBuildHeaders(target);

		loadHeadersFromResource(R.xml.settings_headers, target);
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
