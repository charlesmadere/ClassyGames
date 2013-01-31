package edu.selu.android.classygames.utilities;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.selu.android.classygames.R;
import edu.selu.android.classygames.data.Person;


public class Utilities
{


	public final static String LOG_TAG = "Classy Games";
	public final static String SHARED_PREFERENCES_NAME = "CLASSY_PREFERENCES";

	private static Person whoAmI;
	private final static String WHO_AM_I_ID = "WHO_AM_I_ID";
	private final static String WHO_AM_I_NAME = "WHO_AM_I_NAME";

	private static ImageLoader imageLoader;


	// typeface data below
	public final static byte TYPEFACE_BLUE_HIGHWAY_D = 0;
	public final static byte TYPEFACE_BLUE_HIGHWAY_RG = 1;
	public final static byte TYPEFACE_SNELL_ROUNDHAND_BDSCR = 10;
	public final static byte TYPEFACE_SNELL_ROUNDHAND_BLKSCR = 11;
	private final static String TYPEFACE_PATH = "fonts/";
	private final static String TYPEFACE_BLUE_HIGHWAY_D_PATH = TYPEFACE_PATH + "blue_highway_d.ttf";
	private final static String TYPEFACE_BLUE_HIGHWAY_RD_PATH = TYPEFACE_PATH + "blue_highway_rg.ttf";
	private final static String TYPEFACE_SNELL_ROUNDHAND_BDSCR_PATH = TYPEFACE_PATH + "snell_roundhand_bdscr.otf";
	private final static String TYPEFACE_SNELL_ROUNDHAND_BLKSCR_PATH = TYPEFACE_PATH + "snell_roundhand_blkscr.otf";
	private static Typeface typefaceBlueHighwayD;
	private static Typeface typefaceBlueHighwayRG;
	private static Typeface typefaceSnellRoundHandBDSCR;
	private static Typeface typefaceSnellRoundHandBLKSCR;
	// end typeface data


	// facebook data below
	public final static String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE = "/picture";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_SSL = "return_ssl_resources=1";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE = "?type=";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE = FACEBOOK_GRAPH_API_URL_PICTURE + FACEBOOK_GRAPH_API_URL_PICTURE_TYPE + "large";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL = FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE + "&" + FACEBOOK_GRAPH_API_URL_PICTURE_SSL;
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_NORMAL = FACEBOOK_GRAPH_API_URL_PICTURE + FACEBOOK_GRAPH_API_URL_PICTURE_TYPE + "normal";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_NORMAL_SSL = FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_NORMAL + "&" + FACEBOOK_GRAPH_API_URL_PICTURE_SSL;
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SMALL = FACEBOOK_GRAPH_API_URL_PICTURE + FACEBOOK_GRAPH_API_URL_PICTURE_TYPE + "small";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL = FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SMALL + "&" + FACEBOOK_GRAPH_API_URL_PICTURE_SSL;
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SQUARE = FACEBOOK_GRAPH_API_URL_PICTURE + FACEBOOK_GRAPH_API_URL_PICTURE_TYPE + "square";
	public final static String FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SQUARE_SSL = FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SQUARE + "&" + FACEBOOK_GRAPH_API_URL_PICTURE_SSL;
	// end facebook data


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
	 * Prints a Toast message to the screen and prints that same message to the Log.d
	 * console.
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
		easyToast(context, message);
		Log.d(LOG_TAG, message);
	}


	/**
	 * Prints a Toast message to the screen and prints that same message to each and
	 * every log console.
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
		easyToast(context, message);
		Log.d(LOG_TAG, message);
		Log.e(LOG_TAG, message);
		Log.i(LOG_TAG, message);
		Log.v(LOG_TAG, message);
		Log.wtf(LOG_TAG, message);
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
	 * Initializes the ImageLoader library with some specific configuration
	 * settings (if it has not already been initialized) and returns only what
	 * you need - the portion that will actually load an image for ya!
	 * 
	 * @param context
	 * The Context of the class you're currently working in.
	 * 
	 * @return
	 * Returns an instance of the ImageLoader class that can load an image from
	 * a website for ya!
	 */
	public static ImageLoader getImageLoader(final Context context)
	{
		if (imageLoader == null)
		{
			final DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.build();

			final ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(context)
				.defaultDisplayImageOptions(displayOptions)
				.build();

			imageLoader = ImageLoader.getInstance();
			imageLoader.init(loaderConfiguration);
		}

		return imageLoader;
	}


	/**
	 * Returns to you a Typeface that you request.
	 * 
	 * <p><strong>Examples</strong><br />
	 * Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D);<br />
	 * Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_SNELL_ROUND_HAND_BDSCR);</p>
	 * <p><strong>An invalid example</strong><br />
	 * Utilities.getTypeface(getAssets(), -1);</p>
	 * 
	 * @param assetManager
	 * The getAssets() method. Just pass that sucker in here.
	 * 
	 * @param typeface
	 * The int ID of the Typeface object that you want. This must be a valid int ID, as if
	 * it's not, this method will return null.
	 * 
	 * @return
	 * Returns the Typeface object that you requested. If an invalid int ID was passed into
	 * this method, this method will then return null.
	 */
	public static Typeface getTypeface(final AssetManager assetManager, final byte typeface)
	{
		switch (typeface)
		{
			case TYPEFACE_BLUE_HIGHWAY_D:
				if (typefaceBlueHighwayD == null)
				{
					typefaceBlueHighwayD = Typeface.createFromAsset(assetManager, TYPEFACE_BLUE_HIGHWAY_D_PATH);
				}

				return typefaceBlueHighwayD;

			case TYPEFACE_BLUE_HIGHWAY_RG:
				if (typefaceBlueHighwayRG == null)
				{
					typefaceBlueHighwayRG = Typeface.createFromAsset(assetManager, TYPEFACE_BLUE_HIGHWAY_RD_PATH);
				}

				return typefaceBlueHighwayRG;

			case TYPEFACE_SNELL_ROUNDHAND_BDSCR:
				if (typefaceSnellRoundHandBDSCR == null)
				{
					typefaceSnellRoundHandBDSCR = Typeface.createFromAsset(assetManager, TYPEFACE_SNELL_ROUNDHAND_BDSCR_PATH);
				}

				return typefaceSnellRoundHandBDSCR;

			case TYPEFACE_SNELL_ROUNDHAND_BLKSCR:
				if (typefaceSnellRoundHandBLKSCR == null)
				{
					typefaceSnellRoundHandBLKSCR = Typeface.createFromAsset(assetManager, TYPEFACE_SNELL_ROUNDHAND_BLKSCR_PATH);
				}

				return typefaceSnellRoundHandBLKSCR;

			default:
				return null;
		}
	}


	/**
	 * If the user's Facebook identity is already stored in this class's static
	 * whoAmI Person variable then that variable will be instantly returned. If
	 * the whoAmI Person variable is currently null or is not valid, then we
	 * will search the Android SharedPreferences data for the user's Facebook
	 * identity.
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
			final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

			// find the user's Facebook ID. If the ID can't be found then the
			// id variable will be set to 0.
			final long id = sharedPreferences.getLong(WHO_AM_I_ID, 0);

			// find the user's Facebook name. If the name can't be found then
			// the name variable will be set to null.
			final String name = sharedPreferences.getString(WHO_AM_I_NAME, null);

			if (Person.isIdValid(id) && name != null && !name.isEmpty())
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
	* Simple method that handles pulling an image from a URL.
	* 
	* <p><strong>Examples</strong><br />
	* final String myURL = "http://www.google.com/image.png";<br />
	* Utilities.loadImageFromWebOperations(myURL);</p>
	* 
	* @param url
	* The URL to the image that you want to download.
	*/
	public static Drawable loadImageFromWebOperations(final String url)
	{
		Drawable drawable = null;

		try
		{
			final InputStream is = (InputStream) new URL(url).getContent();
			drawable = Drawable.createFromStream(is, "src");
		}
		catch (final MalformedURLException e)
		{
			Log.e(LOG_TAG, "MalformedURLException while trying to create drawable from url: " + url, e);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException while trying to create drawable from url: " + url, e);
		}

		return drawable;
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
		final SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
		editor.putLong(WHO_AM_I_ID, facebookIdentity.getId());
		editor.putString(WHO_AM_I_NAME, facebookIdentity.getName());
		editor.commit();

		whoAmI = facebookIdentity;
	}


	/**
	 * This is a workaround for http://b.android.com/15340 from http://stackoverflow.com/a/5852198/132047.
	 * This ensures that pre ice cream sandwich devices properly render our customized actionbar.
	 * This method should always be run immediately after the setContentView() method is run.
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
		// if the running version of Android is lower than API Level 14 (below Ice Cream Sandwich 4.0)
		// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
		{
			final BitmapDrawable bg = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
			bg.setAntiAlias(true);
			bg.setDither(true);
			bg.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			actionBar.setBackgroundDrawable(bg);
		}

		if (backArrow)
		{
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}


}
