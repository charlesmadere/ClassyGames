package edu.selu.android.classygames;


import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

import com.actionbarsherlock.app.ActionBar;


public class Utilities
{


	public final static String LOG_TAG = "ClassyGames";


	public static void styleActionBar(final Resources resources, final ActionBar actionBar)
	// This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047
	// This ensures that pre ice cream sandwich devices properly render our customized actionbar
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			BitmapDrawable bg = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			actionBar.setBackgroundDrawable(bg);
		}
	}


}
