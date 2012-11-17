package edu.selu.android.classygames.utilities;


import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.facebook.android.Facebook;

import edu.selu.android.classygames.R;
import edu.selu.android.classygames.SecretConstants;
import edu.selu.android.classygames.data.Person;


public class Utilities
{


	public final static String LOG_TAG = "Classy Games";
	public final static String SHARED_PREFERENCES_NAME = "CLASSY_PREFERENCES";

	private static Person whoAmI;


	public final static CompressFormat COMPRESS_FORMAT = CompressFormat.PNG;
	public final static int COMPRESS_QUALITY = 70;
	public final static int APP_VERSION = 1;
	public final static int VALUE_COUNT = 1;
	public final static int IO_BUFFER_SIZE = 8 * 1024;
	public final static int DISK_CACHE_SIZE = 1024 * 1024 * 10;
	public final static String DISK_CACHE_SUBDIR = "thumbnails";


	// typeface data below
	public final static int TYPEFACE_BLUE_HIGHWAY_D = 0;
	public final static int TYPEFACE_BLUE_HIGHWAY_RG = 1;
	public final static int TYPEFACE_SNELL_ROUNDHAND_BDSCR = 10;
	public final static int TYPEFACE_SNELL_ROUNDHAND_BLKSCR = 11;
	private final static String TYPEFACE_BLUE_HIGHWAY_D_PATH = "fonts/blue_highway_d.ttf";
	private final static String TYPEFACE_BLUE_HIGHWAY_RD_PATH = "fonts/blue_highway_rg.ttf";
	private final static String TYPEFACE_SNELL_ROUNDHAND_BDSCR_PATH = "fonts/snell_roundhand_bdscr.otf";
	private final static String TYPEFACE_SNELL_ROUNDHAND_BLKSCR_PATH = "fonts/snell_roundhand_blkscr.otf";
	private static Typeface typefaceBlueHighwayD;
	private static Typeface typefaceBlueHighwayRG;
	private static Typeface typefaceSnellRoundHandBDSCR;
	private static Typeface typefaceSnellRoundHandBLKSCR;
	// end typeface data


	// facebook data below
	private static Facebook facebook;

	public final static String FACEBOOK_ACCESS_TOKEN = "facebook_access_token";
	public final static String FACEBOOK_EXPIRES = "facebook_expires_in";
	public final static String FACEBOOK_MY_ID = "facebook_my_id";
	public final static String FACEBOOK_MY_NAME = "facebook_my_name";

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
	 * <p>Prints a Toast message to the screen.</p>
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
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}


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
		easyToast(context, message);
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
		easyToast(context, message);
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
		easyToast(context, message);
		Log.e(LOG_TAG, message);
	}


	/**
	 * Use this method to retrieve the global Facebook variable that our app uses. This
	 * method is good to use because it ensures that our Facebook variable isn't null.
	 */
	public static Facebook getFacebook()
	{
		if (facebook == null)
		{
			facebook = new Facebook(SecretConstants.FACEBOOK_APP_ID);
		}

		return facebook;
	}


	/**
	 * <p>Returns to you a Typeface that you request.</p>
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
	public static Typeface getTypeface(final AssetManager assetManager, final int typeface)
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
	 * @return
	 * A Person object representing your Facebook identity.
	 */
	public static Person getWhoAmI()
	{
		if (whoAmI == null)
		{
			whoAmI = new Person();
		}

		return whoAmI;
	}


	public static void setWhoAmI(final Person facebookIdentity)
	{
		whoAmI = facebookIdentity;
	}


	/**
	* <p>Simple method that handles pulling an image from a URL.</p>
	* 
	* <p><strong>Examples</strong><br />
	* Utilities.LoadImageFRomWebOperations(myURL);</p>
	* 
	* @param url
	* The URL to the image that you want to download.
	*/
	public static Drawable loadImageFromWebOperations(final String url)
	{
		try
		{
			InputStream is = (InputStream) new URL(url).getContent();
			return Drawable.createFromStream(is, "src");
		}
		catch (final Exception e)
		{
			Log.e(LOG_TAG, "Image Load Failed" + e);
			return null;
		}
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
	 * Utilities.styleActionBar(getResources(), getSupportActionBar());</p>
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
