package com.charlesmadere.android.classygames;


import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;
import com.charlesmadere.android.classygames.preferences.AboutPreferenceFragment;
import com.charlesmadere.android.classygames.preferences.BasePreferenceFragment;
import com.charlesmadere.android.classygames.preferences.GamePreferenceFragment;
import com.charlesmadere.android.classygames.preferences.NotificationPreferenceFragment;
import com.charlesmadere.android.classygames.utilities.Utilities;


public abstract class BasePreferenceActivity extends SherlockPreferenceActivity
{


	protected CharSequence getActionBarTitle()
	{
		final TextView titleView = (TextView) findViewById(R.id.action_bar_title);
		return titleView.getText();
	}


	protected void onCreate(final Bundle savedInstanceState, final int title, final boolean showBackArrow)
	{
		onCreate(savedInstanceState, getString(title), showBackArrow);
	}


	protected void onCreate(final Bundle savedInstanceState, final CharSequence title, final boolean showBackArrow)
	{
		super.onCreate(savedInstanceState);
		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null)
		{
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			{
				final BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_action_bar);
				background.setAntiAlias(true);
				background.setDither(true);
				background.setFilterBitmap(true);
				background.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
				actionBar.setBackgroundDrawable(background);
			}

			updateActionBar(title, showBackArrow);
		}
	}


	protected void updateActionBar(final int title, final boolean showBackArrow)
	{
		updateActionBar(getString(title), showBackArrow);
	}


	protected void updateActionBar(final CharSequence title, final boolean showBackArrow)
	{
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(showBackArrow);

		final TextView titleView = (TextView) findViewById(R.id.action_bar_title);
		titleView.setText(title);
	}


	@Override
	protected boolean isValidFragment(final String fragmentName)
	{
		final boolean isValidFragment;

		// http://stackoverflow.com/questions/19973034/isvalidfragment-android-api-19

		if (Utilities.validString(fragmentName) && (AboutPreferenceFragment.class.getName().equals(fragmentName)
			|| BasePreferenceFragment.class.getName().equals(fragmentName)
			|| GamePreferenceFragment.class.getName().equals(fragmentName)
			|| NotificationPreferenceFragment.class.getName().equals(fragmentName)))
		{
			isValidFragment = true;
		}
		else
		{
			isValidFragment = false;
		}

		return isValidFragment;
	}


}
