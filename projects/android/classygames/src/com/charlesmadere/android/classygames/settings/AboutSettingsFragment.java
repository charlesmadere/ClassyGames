package com.charlesmadere.android.classygames.settings;


import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.charlesmadere.android.classygames.R;


public final class AboutSettingsFragment extends PreferenceFragment
{


	private SettingsFragmentListeners settingsListeners;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		settingsListeners.updateActionBarTitle(R.string.about);
		addPreferencesFromResource(R.xml.settings_about);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getView().setBackgroundResource(R.drawable.bg_bright);
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);
		settingsListeners = (SettingsFragmentListeners) activity;
	}


}
