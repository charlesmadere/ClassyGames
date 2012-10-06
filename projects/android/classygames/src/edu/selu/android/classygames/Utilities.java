package edu.selu.android.classygames;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.facebook.android.Facebook;


public class Utilities
{


	public final static String LOG_TAG = "ClassyGames";
	public static SharedPreferences sharedPreferences;

	/**
	 * PLEASE USE THE Utilities.ensureFacebookIsNotNull() method BEFORE using ANY Facebook
	 * variables found in this class
	 */
	private static Facebook facebook;

	public final static String FACEBOOK_APP_ID = "324400870964487";
	public final static String FACEBOOK_TOKEN = "access_token";
	public final static String FACEBOOK_EXPIRES = "expires_in";


	/**
	 * <p>Prints a Toast message to the screen and prints that same message to the Log.d
	 * console.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.easyToastAndLog(MainActivity.this, "Hello!");<br />
	 * Utilities.easyToastAndLog(getApplicationContext(), "Another message huh?");</p>
	 * 
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 * 
	 * @param message
	 * The String that you want to be shown as a toast message. This exact String will also
	 * be printed to the Log.d console.
	 */
	public static void easyToastAndLog(final Context context, final String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		Log.d(LOG_TAG, message);
	}


	/**
	 * <p>Prints a Toast message to the screen and prints that same message to each and
	 * every log console.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.easyToastAndLogAll(MainActivity.this, "Hello!");<br />
	 * Utilities.easyToastAndLogAll(getApplicationContext(), "Another message huh?");</p>
	 * 
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 * 
	 * @param message
	 * The String that you want to be shown as a toast message. This exact String will also
	 * be printed to the Log.d console.
	 */
	public static void easyToastAndLogAll(final Context context, final String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		Log.d(LOG_TAG, message);
		Log.e(LOG_TAG, message);
		Log.i(LOG_TAG, message);
		Log.v(LOG_TAG, message);
		Log.wtf(LOG_TAG, message);
	}


	/**
	 * <p>Prints a Toast message to the screen and prints that same message to the Log.e
	 * console.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.easyToastAndLogError(MainActivity.this, "Hello!");<br />
	 * Utilities.easyToastAndLogError(getApplicationContext(), "Another message huh?");</p>
	 * 
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 * 
	 * @param message
	 * The String that you want to be shown as a toast message. This exact String will also
	 * be printed to the Log.e console.
	 */
	public static void easyToastAndLogError(final Context context, final String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		Log.e(LOG_TAG, message);
	}


	/**
	 * Use this method to retrieve the global Facebook variable that our app uses. 
	 */
	public static Facebook getFacebook()
	{
		if (facebook == null)
		{
			facebook = new Facebook(FACEBOOK_APP_ID);
		}

		return facebook;
	}


	/**
	 * <p>This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047.
	 * This ensures that pre ice cream sandwich devices properly render our customized actionbar.
	 * This method should always be run immediately after the setContentView() method is run.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.styleActionBar(getResources(), getSupportActionBar(), false);</p>
	 * 
	 * @param resources
	 * getResources()
	 * 
	 * @param actionBar
	 * getSupportActionBar()
	 * 
	 * @param backArrow
	 * Whether or not you want to have a back arrow drawn next to the app icon in the actionbar.
	 */
	public static void styleActionBar(final Resources resources, final ActionBar actionBar, final boolean backArrow)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			final BitmapDrawable bg = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
			bg.setDither(true);
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			actionBar.setBackgroundDrawable(bg);
		}

		if (backArrow)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * <p>This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047.
	 * This ensures that pre ice cream sandwich devices properly render our customized actionbar.
	 * This method should always be run immediately after the setContentView() method is run.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utiltiles.styleActionBar(getResources(), getSupportActionBar());</p>
	 * 
	 * @param resources
	 * getResources()
	 * 
	 * @param actionBar
	 * getSupportActionBar()
	 */
	public static void styleActionBar(final Resources resources, final ActionBar actionBar)
	{
		styleActionBar(resources, actionBar, true);
	}


}
