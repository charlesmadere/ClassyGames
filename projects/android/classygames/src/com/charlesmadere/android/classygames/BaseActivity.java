package com.charlesmadere.android.classygames;


import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public abstract class BaseActivity extends SherlockActivity
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


}
