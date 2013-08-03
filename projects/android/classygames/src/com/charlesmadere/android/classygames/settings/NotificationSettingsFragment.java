package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.SpannableString;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class NotificationSettingsFragment extends PreferenceFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final String actionBarTitle = getString(R.string.notification_settings);
		final SpannableString styledActionBarTitle = Utilities.makeStyledString(actionBarTitle,
			TypefaceUtilities.getBlueHighway());
		getActivity().getActionBar().setTitle(styledActionBarTitle);

		addPreferencesFromResource(R.xml.settings_notification);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Utilities.setBackground(getActivity(), getView());
	}


}
