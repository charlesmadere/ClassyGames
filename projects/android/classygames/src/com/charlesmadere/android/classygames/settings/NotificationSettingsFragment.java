package com.charlesmadere.android.classygames.settings;


import com.charlesmadere.android.classygames.R;


public final class NotificationSettingsFragment extends BasePreferenceFragment
{


	@Override
	protected CharSequence getActionBarTitle()
	{
		return getString(R.string.notification_settings);
	}


	@Override
	protected int getPreferencesResources()
	{
		return R.xml.settings_notification;
	}


}
