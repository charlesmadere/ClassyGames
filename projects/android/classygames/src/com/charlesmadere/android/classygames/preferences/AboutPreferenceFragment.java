package com.charlesmadere.android.classygames.preferences;


import com.charlesmadere.android.classygames.R;


public final class AboutPreferenceFragment extends BasePreferenceFragment
{


	@Override
	protected CharSequence getActionBarTitle()
	{
		return getString(R.string.about);
	}


	@Override
	protected int getPreferencesResources()
	{
		return R.xml.settings_about;
	}


}
