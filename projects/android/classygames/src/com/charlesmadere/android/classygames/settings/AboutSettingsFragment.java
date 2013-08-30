package com.charlesmadere.android.classygames.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.SpannableString;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class AboutSettingsFragment extends PreferenceFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		final String actionBarTitle = getString(R.string.about);
		final SpannableString styledActionBarTitle = Utilities.makeStyledString(actionBarTitle,
			TypefaceUtilities.getBlueHighway());
		getActivity().getActionBar().setTitle(styledActionBarTitle);

		addPreferencesFromResource(R.xml.settings_about);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getView().setBackgroundResource(R.drawable.bg_bright);
	}
}
