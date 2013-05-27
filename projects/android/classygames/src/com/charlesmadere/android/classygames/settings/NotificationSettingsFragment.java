package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.Spannable;
import android.text.SpannableString;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.ActionBarTypeface;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;


public class NotificationSettingsFragment extends PreferenceFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final String actionBarTitle = getString(R.string.notification_settings);
		final SpannableString styledActionBarTitle = new SpannableString(actionBarTitle);
		styledActionBarTitle.setSpan(new ActionBarTypeface
			(getActivity().getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D), 0, styledActionBarTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActivity().getActionBar().setTitle(styledActionBarTitle);

		addPreferencesFromResource(R.xml.settings_notification);
	}


}
