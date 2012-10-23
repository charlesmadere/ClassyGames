package edu.selu.android.classygames;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
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
import com.facebook.android.Facebook;
import com.google.android.gcm.GCMRegistrar;


public class Utilities
{


	public final static String LOG_TAG = "ClassyGames";
	public final static String DISPLAY_MESSAGE_ACTION = "edu.selu.android.classygames.CONTEXT_BROADCAST";

	public static SharedPreferences sharedPreferences;

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

	public final static String FACEBOOK_EXPIRES = "expires_in";
	public final static String FACEBOOK_TOKEN = "access_token";

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


	// server data below
	private final static int REGISTER_MAX_ATTEMPTS = 5;
	private final static long REGISTER_BACKOFF_TIME = 2000; // milliseconds

	public final static String JSON_DATA = "json";
	public final static String JSON_DATA_BLANK = "";
	public final static String JSON_DATA_ID = "id";
	public final static String JSON_DATA_NAME = "name";
	public final static String JSON_DATA_REG_ID = "reg_id";

	private static Random random;
	// end server data


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
	 * 
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
	* <p>Simple class that handles pulling an image from a URL.</p>
	* 
	* <p><strong>Examples</strong><br />
	* Utilities.LoadImageFRomWebOperations(myURL);<p>
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
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Image Load Failed" + e);
			return null;
		}
	}


	private static void contextBroadcast(final Context context, final String message)
	{
		Intent intent = new Intent();
		intent.putExtra("message", message);
		context.sendBroadcast(intent);
	}


	private static void postToServer(final String endpoint, final Map<String, String> params) throws IOException
	{
		URL url = null;

		try
		{
			url = new URL(endpoint);
		}
		catch (final MalformedURLException e)
		{
			throw new IllegalArgumentException("Invalid url: \"" + endpoint + "\"");
		}

		if (url != null)
		{
			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

			while (iterator.hasNext())
			// constructs the POST body using the parameters
			{
				Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
	
				if (iterator.hasNext())
				{
					bodyBuilder.append('&');
				}
			}

			final String body = bodyBuilder.toString();
			Log.d(LOG_TAG, "Posting \"" + body + "\" to \"" + url + "\"");

			byte[] bytes = body.getBytes();

			HttpURLConnection connection = null;

			try
			{
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setFixedLengthStreamingMode(bytes.length);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

				// send the request via POST
				OutputStream output = connection.getOutputStream();
				output.write(bytes);
				output.close();

				// handle the response
				final int status = connection.getResponseCode();

				if (status != 200)
				{
					throw new IOException("POST failed with error code \"" + status + "\"");
				}
			}
			finally
			{
				if (connection != null)
				{
					connection.disconnect();
				}
			}
		}
	}


	public static boolean GCMRegister(final Context context, long id, final String name, String reg_id)
	{
		Log.i(LOG_TAG, "Registering device with reg_id of \"" + reg_id + "\" with GCM server.");

		Map<String, String> params = new HashMap<String, String>();
		params.put(JSON_DATA_REG_ID, reg_id);

		if (random == null)
		{
			random = new Random();
		}

		long backoffTime = REGISTER_BACKOFF_TIME + random.nextInt(1000);

		for (int i = 1; i <= REGISTER_MAX_ATTEMPTS; ++i)
		{
			Log.i(LOG_TAG, "GCM register attempt #" + i);

			try
			{
				contextBroadcast(context, context.getString(R.string.server_registration_attempt, i));
				postToServer(SecretConstants.SERVER_ADD_REG_ID_ADDRESS, params);
				GCMRegistrar.setRegisteredOnServer(context, true);
				contextBroadcast(context, context.getString(R.string.server_registration_success));
			}
			catch (final IOException ioe)
			{
				Log.e(LOG_TAG, "GCM register failure on attempt #" + i, ioe);

				if (i <= REGISTER_MAX_ATTEMPTS)
				{
					try
					{
						Log.d(LOG_TAG, "Sleeping for " + backoffTime + "ms before next GCM register attempt.");
						Thread.sleep(backoffTime);
					}
					catch (final InterruptedException ie)
					{
						Log.e(LOG_TAG, "GCM register thread interrupted! Aborting registration attempt");
						Thread.currentThread().interrupt();

						return false;
					}

					// exponentially increase backoffTime so that we wait a bit longer before our
					// next GCM registration attempt
					backoffTime = (backoffTime * 2) + backoffTime;
				}
			}
		}

		contextBroadcast(context, context.getString(R.string.server_registration_fail, REGISTER_MAX_ATTEMPTS));

		return false;
	}


	public static void GCMUnregister(final Context context, final String reg_id)
	{
		Log.i(LOG_TAG, "Unregistering device with reg_id of \"" + reg_id + "\" from GCM server.");

		Map<String, String> params = new HashMap<String, String>();
		params.put(JSON_DATA_REG_ID, reg_id);

		try
		{
			postToServer(SecretConstants.SERVER_REMOVE_REG_ID_ADDRESS, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
			contextBroadcast(context, context.getString(R.string.server_unregistration_success));
		}
		catch (final IOException e)
		// At this point the device has been unregistered from GCM but is still registered on
		// the server. We could try to unregister again but it is not necessary. If the server
		// tries to send a message to the device, it will get a "NotRegistered" error message
		// and should unregister the device
		{
			contextBroadcast(context, context.getString(R.string.server_unregistration_fail, e.getMessage()));
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
