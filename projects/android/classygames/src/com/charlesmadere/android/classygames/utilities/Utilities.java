package com.charlesmadere.android.classygames.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Class filled with a bunch of miscellaneous utility methods and constants.
 */
public final class Utilities
{


	public final static String LOG_TAG = "Classy Games";


	// Stores the reg id of the current Android device. More information can be
	// found here: https://developer.android.com/google/gcm/index.html
	private static String regId;
	private final static String KEY_REG_ID = "KEY_REG_ID";


	// stores the Facebook user id and name of the current user of the Classy
	// Games application
	private static Person whoAmI;
	private final static String KEY_WHO_AM_I_ID = "KEY_WHO_AM_I_ID";
	private final static String KEY_WHO_AM_I_NAME = "KEY_WHO_AM_I_NAME";




	/**
	 * Checks to see if this Android device currently has network connectivity.
	 *
	 * @param context
	 * The context of the Activity or Fragment that you're calling this method
	 * from.
	 *
	 * @return
	 * Returns true if this Android device is currently connected to a network.
	 */
	public static boolean checkForNetworkConnectivity(final Context context)
	{
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}


	/**
	 * Prints a Toast message to the screen.
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.easyToast(MainActivity.this, "Hello!");<br />
	 * Utilities.easyToast(getApplicationContext(), "Another message huh?");</p>
	 * 
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 * 
	 * @param message
	 * The String that you want to be shown as a toast message.
	 */
	public static void easyToast(final Context context, final String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}


	/**
	 * Prints a Toast message to the screen with a String taken from the
	 * strings.xml file.
	 * 
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 * 
	 * @param stringId
	 * The int ID of the resource that you want to print.
	 */
	public static void easyToast(final Context context, final int stringId)
	{
		easyToast(context, context.getString(stringId));
	}


	/**
	 * Prints a Toast message to the screen and prints that same message to the
	 * Log.e console.
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
		easyToast(context, message);
		Log.e(LOG_TAG, message);
	}


	/**
	 * Prints a Toast message to the screen and prints that same message to the
	 * Log.e console.
	 *
	 * <p><strong>Examples</strong><br />
	 * Utilities.easyToastAndLogError(MainActivity.this, "Hello!");<br />
	 * Utilities.easyToastAndLogError(getApplicationContext(), "Another message huh?");</p>
	 *
	 * @param context
	 * Just put the name of your class.this, or you can use getApplicationContext().
	 *
	 * @param stringId
	 * The int ID of the resource that you want to print.
	 */
	public static void easyToastAndLogError(final Context context, final int stringId)
	{
		easyToast(context, stringId);
		Log.e(LOG_TAG, context.getString(stringId));
	}


	/**
	 * Makes and then returns a styled String. This is useful for obtaining a
	 * String that makes use of a custom typeface. As of right now, this should
	 * probably only be used to customize the Android Action Bar.
	 *
	 * @param string
	 * The String to apply the custom typeface to.
	 *
	 * @param typeface
	 * The custom typeface that you want to use. This needs to be one of the
	 * public bytes as found in the TypefaceUtilities class. If an invalid
	 * value is passed in here then there will definitely be a problem.
	 *
	 * @return
	 * Returns the styled String as created with your specifications.
	 */
	public static SpannableString makeStyledString(final CharSequence string,
		final Typeface typeface)
	{
		final SpannableString styledString = new SpannableString(string);
		styledString.setSpan(new StyledString(typeface), 0, styledString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return styledString;
	}


	/**
	 * Stylizes the Android Action Bar, sets its title, and enables or disables
	 * its back arrow.
	 *
	 * @param activity
	 * The activity that you're currently working in. This can usually be
	 * obtained by just using the this keyword or getSherlockActivity().
	 *
	 * @param actionBarTitle
	 * The R.string.* title to be shown on the Android Action Bar.
	 *
	 * @param showBackArrow
	 * Want to show the back arrow on the Action Bar? Pass in true to show it.
	 */
	public static void setActionBar(final SherlockActivity activity, final int actionBarTitle,
		final boolean showBackArrow)
	{
		setAndStyleActionBar
		(
			activity.getAssets(),
			activity.getSupportActionBar(),
			activity.getString(actionBarTitle),
			activity.getResources(),
			showBackArrow
		);
	}


	/**
	 * Stylizes the Android Action Bar, sets its title, and enables or disables
	 * its back arrow.
	 *
	 * @param activity
	 * The activity that you're currently working in. This can usually be
	 * obtained by just using the this keyword or getSherlockActivity().
	 *
	 * @param actionBarTitle
	 * The title to be shown on the Android Action Bar.
	 *
	 * @param showBackArrow
	 * Want to show the back arrow on the Action Bar? Pass in true to show it.
	 */
	public static void setActionBar(final SherlockFragmentActivity activity, final CharSequence actionBarTitle, final boolean showBackArrow)
	{
		setAndStyleActionBar
		(
			activity.getAssets(),
			activity.getSupportActionBar(),
			actionBarTitle,
			activity.getResources(),
			showBackArrow
		);
	}


	/**
	 * Stylizes the Android Action Bar, sets its title, and enables or disables
	 * its back arrow.
	 *
	 * @param activity
	 * The activity that you're currently working in. This can usually be
	 * obtained by just using the this keyword or getSherlockActivity().
	 *
	 * @param actionBarTitle
	 * The R.string.* title to be shown on the Android Action Bar.
	 *
	 * @param showBackArrow
	 * Want to show the back arrow on the Action Bar? Pass in true to show it.
	 */
	public static void setActionBar(final SherlockFragmentActivity activity, final int actionBarTitle, final boolean showBackArrow)
	{
		setAndStyleActionBar
		(
			activity.getAssets(),
			activity.getSupportActionBar(),
			activity.getString(actionBarTitle),
			activity.getResources(),
			showBackArrow
		);
	}


	/**
	 * Stylizes the Android Action Bar, sets its title, and enables or disables
	 * its back arrow.
	 *
	 * @param activity
	 * The activity that you're currently working in. This can usually be
	 * obtained by just using the this keyword or getSherlockActivity().
	 *
	 * @param actionBarTitle
	 * The R.string.* title to be shown on the Android Action Bar.
	 *
	 * @param showBackArrow
	 * Want to show the back arrow on the Action Bar? Pass in true to show it.
	 */
	public static void setActionBar(final SherlockPreferenceActivity activity, final int actionBarTitle, final boolean showBackArrow)
	{
		setAndStyleActionBar
		(
			activity.getAssets(),
			activity.getSupportActionBar(),
			activity.getString(actionBarTitle),
			activity.getResources(),
			showBackArrow
		);
	}


	/**
	 * Performs final setting and stylizing on the Android Action Bar.
	 *
	 * @param assetManager
	 * A handle to the Activity's AssetManager. This can usually be obtained by
	 * using getAssets().
	 *
	 * @param actionBar
	 * A handle to the Activity's Sherlock Action Bar. This can usually be
	 * obtained by using getSupportActionBar().
	 *
	 * @param actionBarTitle
	 * The actual string to be shown as the title of the Action Bar.
	 *
	 * @param resources
	 * A handle to the Activity's resources. This can usually be obtained by
	 * using getResources().
	 *
	 * @param showBackArrow
	 * Want to show the back arrow on the Action Bar? Pass in true to show it.
	 */
	private static void setAndStyleActionBar(final AssetManager assetManager, final ActionBar actionBar,
		final CharSequence actionBarTitle, final Resources resources, final boolean showBackArrow)
	{
		final SpannableString styledActionBarTitle = makeStyledString(actionBarTitle, TypefaceUtilities.getBlueHighwayTypeface(assetManager));

		actionBar.setDisplayHomeAsUpEnabled(showBackArrow);
		actionBar.setTitle(styledActionBarTitle);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		// if the running version of Android is lower than API Level 14 (below Ice Cream Sandwich 4.0)
		// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
		{
			final BitmapDrawable background = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
			background.setAntiAlias(true);
			background.setDither(true);
			background.setFilterBitmap(true);
			background.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);

			actionBar.setBackgroundDrawable(background);
		}
	}


	/**
	 * Styles the background of a preference activity so that it's not just the
	 * plain ol' white. This method should only be called from a class that
	 * extends from either SherlockPreferenceActivity or PreferenceFragment.
	 *
	 * @param context
	 * The context of the Activity or Fragment that is calling this method.
	 *
	 * @param view
	 * The View that you want the background applied to.
	 */
	public static void setBackground(final Context context, final View view)
	{
		final Drawable background = context.getResources().getDrawable(R.drawable.bg_bright);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
		{
			view.setBackgroundDrawable(background);
		}
		else
		{
			view.setBackground(background);
		}
	}


	/**
	 * Initializes the ImageLoader library with some specific configuration
	 * settings (if it has not already been initialized) and returns only what
	 * you need - the portion that will actually load an image for ya!
	 * https://github.com/nostra13/Android-Universal-Image-Loader
	 *
	 * @param context
	 * The context of the Activity that is calling this method.
	 *
	 * @return
	 * Returns an instance of the ImageLoader class that can load an image from
	 * a URL for you.
	 */
	public static ImageLoader getImageLoader(final Context context)
	{
		final DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();

		final ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(context)
			.defaultDisplayImageOptions(displayOptions)
			.build();

		final ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(loaderConfiguration);

		return imageLoader;
	}


	/**
	 * Gives you a handle to the Classy Games default SharedPreferences object.
	 * 
	 * @param context
	 * The Context of the class that you're calling this from. If you're
	 * calling this method from an Activity then you can usually just use the
	 * this keyword, otherwise you may need to use something like
	 * getSherlockActivity().
	 * 
	 * @return
	 * Returns a handle to the Classy Games default SharedPreferences object.
	 */
	public static SharedPreferences getPreferences(final Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}


	/**
	 * Checks to see if a given user preference is enabled or disabled. This
	 * method should only be used to check on preferences that must be either
	 * on or off (true or false).
	 *
	 * @param context
	 * The context of the Activity or Fragment that you're calling this method
	 * from.
	 *
	 * @param key
	 * The R.string.* value for the settings key that you're trying to
	 * retrieve.
	 *
	 * @param defaultValue
	 * The default value that you want returned in case the setting that you
	 * searched for does not exist.
	 *
	 * @return
	 * Returns the value for the given user preference from the universal
	 * Android default shared preferences cache if it can be found. If it can't
	 * be found, then the value that will instead be returned is the value of
	 * the defaultValue variable that you passed in.
	 */
	public static boolean checkIfSettingIsEnabled(final Context context, final int key, final boolean defaultValue)
	{
		final String string = context.getString(key);
		return getPreferences(context).getBoolean(string, defaultValue);
	}


	/**
	 * Gives you this Android device's GCM registration ID.
	 * 
	 * @param context
	 * The context of the Activity that is calling this method.
	 * 
	 * @return
	 * Returns this Android device's GCM registration ID. This is typically a
	 * somewhat long String filled with random characters. <strong>Note that
	 * this method has a slim possibility of returning null.</strong>
	 */
	public static String getRegId(final Context context)
	{
		if (!verifyValidString(regId))
		{
			final SharedPreferences sPreferences = getPreferences(context);

			// Grab the user's GCM registration ID from shared preferences if
			// it already exists. Returns null if it doesn't already exist.
			regId = sPreferences.getString(KEY_REG_ID, null);
		}

		return regId;
	}


	/**
	 * Sets this Android device's GCM registration ID.
	 * 
	 * @param context
	 * The context of the Activity that is calling this method.
	 * 
	 * @param regId
	 * The new GCM registration ID.
	 */
	public static void setRegId(final Context context, final String regId)
	{
		final SharedPreferences sPreferences = getPreferences(context);
		final SharedPreferences.Editor editor = sPreferences.edit();
		editor.putString(KEY_REG_ID, regId);
		editor.commit();

		Utilities.regId = regId;
	}


	/**
	 * If the user's Facebook identity is already stored in this class's static
	 * whoAmI Person variable then that variable will be instantly returned. If
	 * the whoAmI Person variable is currently null or is not valid, then we
	 * will search the Android SharedPreferences data for the user's Facebook
	 * identity.
	 * 
	 * @param context
	 * The context of the Activity that is calling this method.
	 * 
	 * @return
	 * A Person object that represents the user's Facebook identity.
	 */
	public static Person getWhoAmI(final Context context)
	{
		if (whoAmI == null || !whoAmI.isValid())
		// check to see if the whoAmI variable is null or if it's not valid. If
		// it is either of these two conditions then we will pull the user's
		// Facebook identity from the Android SharedPreferences data.
		{
			final SharedPreferences sPreferences = getPreferences(context);

			// find the user's Facebook ID. If the ID can't be found then the
			// id variable will be set to 0.
			final long id = sPreferences.getLong(KEY_WHO_AM_I_ID, 0);

			// find the user's Facebook name. If the name can't be found then
			// the name variable will be set to null.
			final String name = sPreferences.getString(KEY_WHO_AM_I_NAME, null);

			if (Person.isIdAndNameValid(id, name))
			// check to see that we were actually able to find the user's
			// Facebook ID and Facebook name. If we were able to find both
			// then we will create a new Person object out of that data. That
			// Person object will then be returned.
			{
				whoAmI = new Person(id, name);
			}
		}

		return whoAmI;
	}


	/**
	 * Stores the current user's Facebook identity into the Android
	 * SharedPreferences storage system. The current user's Facebook identity
	 * is frequently used throughout the app, and so doing this allows future
	 * needings of this data to not require making a Facebook API call.
	 * 
	 * @param context
	 * The context of the Activity that is calling this method.
	 * 
	 * @param facebookIdentity
	 * A Person object representing the current user's Facebook identity.
	 */
	public static void setWhoAmI(final Context context, final Person facebookIdentity)
	{
		final SharedPreferences sPreferences = getPreferences(context);
		final SharedPreferences.Editor editor = sPreferences.edit();
		editor.putLong(KEY_WHO_AM_I_ID, facebookIdentity.getId());
		editor.putString(KEY_WHO_AM_I_NAME, facebookIdentity.getName());
		editor.commit();

		whoAmI = facebookIdentity;
	}


	/**
	 * Verifies a String object for validity.
	 * 
	 * @param string
	 * The String to check.
	 * 
	 * @return
	 * Returns true if the given String is valid.
	 */
	public static boolean verifyValidString(final String string)
	{
		return string != null && string.length() >= 1;
	}


	/**
	 * Verifies a set of String objects for validity.
	 * 
	 * @param strings
	 * The Strings to check.
	 * 
	 * @return
	 * Returns true if all of the given Strings are valid.
	 */
	public static boolean verifyValidStrings(final String... strings)
	{
		for (final String string : strings)
		{
			if (!verifyValidString(string))
			{
				return false;
			}
		}

		return true;
	}


}
