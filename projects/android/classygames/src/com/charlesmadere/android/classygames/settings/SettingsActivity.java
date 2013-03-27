package com.charlesmadere.android.classygames.settings;


import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class SettingsActivity extends SherlockPreferenceActivity
{


	// Some of the code used in making this class and it's corresponding XML
	// files was taken from the official Android Documentation.
	// https://developer.android.com/guide/topics/ui/settings.html


	@Override
	@SuppressWarnings("deprecation")
	protected void onCreate(final Bundle savedInstanceState)
	// The addPreferencesFromResource methods below are causing some
	// deprecation warnings. In this case, the fact that they're here is fine.
	// They have to be used if the running version of Android is below
	// Honeycomb (v3.0). See more information about API levels here:
	// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
	{
		super.onCreate(savedInstanceState);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		// Check to see if the running version of Android is below Honeycomb.
		{
			// get the intent's action
			final String action = getIntent().getAction();

			if (Utilities.verifyValidString(action))
			{
				if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_game)))
				// the intent's action is saying that we need to show the game
				// settings preference file
				{
					addPreferencesFromResource(R.xml.settings_game);
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_miscellaneous)))
				// the intent's action is saying that we need to show the
				// miscellaneous settings preference file
				{
					addPreferencesFromResource(R.xml.settings_miscellaneous);
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_register)))
					// the intent's action is saying that we need to show the
					// RegisterForNotificationsActivity
				{
					startActivity(new Intent(this, RegisterForNotificationsActivity.class));
				}
				else if (action.equals(getString(R.string.com_charlesmadere_android_classygames_settings_unregister)))
					// the intent's action is saying that we need to show the
					// UnregisterFromNotificationsActivity
				{
					startActivity(new Intent(this, UnregisterFromNotificationsActivity.class));
				}
				else
				// The intent's action was something strange. We'll show the
				// default preference file. This should (hopefully) never
				// happen.
				{
					addPreferencesFromResource(R.xml.settings_headers_legacy);
				}
			}
			else
			// For Android devices running any version below Honeycomb. 
			{
				addPreferencesFromResource(R.xml.settings_headers_legacy);
			}
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
