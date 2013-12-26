package com.charlesmadere.android.classygames.utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import com.charlesmadere.android.classygames.models.Person;


/**
 * Class filled with a bunch of miscellaneous utility methods and constants.
 */
public final class Utilities
{


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
