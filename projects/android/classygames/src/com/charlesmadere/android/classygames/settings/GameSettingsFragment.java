package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.charlesmadere.android.classygames.R;


public class GameSettingsFragment extends PreferenceFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings_game);
	}


}
