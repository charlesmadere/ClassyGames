package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.preference.PreferenceFragment;


public class MiscellaneousSettingsFragment extends PreferenceFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings_miscellaneous);
	}


}
