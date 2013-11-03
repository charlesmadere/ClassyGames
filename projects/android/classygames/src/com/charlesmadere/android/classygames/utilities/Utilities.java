package com.charlesmadere.android.classygames.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.charlesmadere.android.classygames.App;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Class filled with a bunch of miscellaneous utility methods and constants.
 */
public final class Utilities
{


	public final static String LOG_TAG = "Classy Games";

	private static ImageLoader imageLoader;

	// stores the Facebook user id and name of the app's current user
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
	 * Checks to see if a given user preference is enabled or disabled. This
	 * method should only be used to check on preferences that must be either
	 * on or off (true or false).
	 *
	 * @param context
	 * The context of the Activity that you're calling this method from.
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
	 * Retrieves and then returns the app's version code. The returned value
	 * corresponds directly to the "versionCode" value that is found at the
	 * beginning of the AndroidManifest.xml file.
	 *
	 * @param context
	 * The Context of the Activity that you're calling this method from.
	 *
	 * @return
	 * Returns the app's version code (as seen in AndroidManifest.xml).
	 */
	public static int getAppVersionCode(final Context context)
	{
		int versionCode;

		try
		{
			final PackageManager packageManager = context.getPackageManager();
			final String packageName = context.getPackageName();
			final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionCode = packageInfo.versionCode;
		}
		catch (final PackageManager.NameNotFoundException e)
		{
			versionCode = 0;
		}

		return versionCode;
	}


	/**
	 * @return
	 * Returns an ImageLoader object. This can be used to download images from
	 * a web URL and then display them to a view.
	 */
	public static ImageLoader getImageLoader()
	{
		if (imageLoader == null)
		{
			final DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();

			final ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(App.getContext())
				.defaultDisplayImageOptions(displayOptions)
				.build();

			imageLoader = ImageLoader.getInstance();
			imageLoader.init(loaderConfiguration);
		}

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

			if (Person.isIdValid(id) && Person.isNameValid(name))
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
	 * Makes and then returns a styled String. This is useful for obtaining a
	 * String that makes use of a custom typeface. As of right now, this should
	 * probably only be used to customize the Android Action Bar.
	 *
	 * @param string
	 * The String to apply the custom typeface to.
	 *
	 * @param typeface
	 * The custom typeface that you want to use. This needs to be one of the
	 * public bytes as found in the Typefaces class. If an invalid value is
	 * passed in here then there will definitely be a problem.
	 *
	 * @return
	 * Returns the styled String as created with your specifications.
	 */
	public static SpannableString makeStyledString(final CharSequence string, final Typeface typeface)
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
	public static void setActionBar(final SherlockActivity activity, final int actionBarTitle, final boolean showBackArrow)
	{
		setActionBarStyle
		(
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
		setActionBarStyle
		(
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
		setActionBarStyle
		(
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
		setActionBarStyle
		(
			activity.getSupportActionBar(),
			activity.getString(actionBarTitle),
			activity.getResources(),
			showBackArrow
		);
	}


	/**
	 * Performs final setting and stylizing on the Android Action Bar.
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
	private static void setActionBarStyle(final ActionBar actionBar, final CharSequence actionBarTitle,
		final Resources resources, final boolean showBackArrow)
	{
		actionBar.setDisplayHomeAsUpEnabled(showBackArrow);
		actionBar.setTitle(makeStyledString(actionBarTitle, Typefaces.getBlueHighway()));

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
		getPreferences(context).edit()
			.putLong(KEY_WHO_AM_I_ID, facebookIdentity.getId())
			.putString(KEY_WHO_AM_I_NAME, facebookIdentity.getName())
			.commit();

		whoAmI = facebookIdentity;
	}


	/**
	 * Verifies a set of String objects for validity.
	 * 
	 * @param strings
	 * The set of String objects to check.
	 * 
	 * @return
	 * Returns true if <strong>all</strong> of the given Strings are valid.
	 */
	public static boolean validString(final String... strings)
	{
		if (strings == null || strings.length == 0)
		{
			return false;
		}
		else
		{
			for (final String string : strings)
			{
				if (string == null || string.length() == 0)
				{
					return false;
				}
			}
		}

		return true;
	}


}
