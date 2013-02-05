package edu.selu.android.classygames.utilities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.selu.android.classygames.R;
import edu.selu.android.classygames.models.Person;


/**
 * Class filled with a bunch of miscellaneous utility methods and constants.
 */
public class Utilities
{


	public final static String LOG_TAG = "Classy Games";
	public final static String SHARED_PREFERENCES_NAME = "CLASSY_PREFERENCES";

	private static ImageLoader imageLoader;




	/**
	 * Class for constants and methods relating to Facebook.
	 */
	public final static class FacebookUtilities
	{


		public final static String GRAPH_API_URL = "https://graph.facebook.com/";
		public final static String GRAPH_API_URL_PICTURE = "/picture";
		public final static String GRAPH_API_URL_PICTURE_SSL = "return_ssl_resources=1";
		public final static String GRAPH_API_URL_PICTURE_TYPE = "?type=";
		public final static String GRAPH_API_URL_PICTURE_TYPE_LARGE = GRAPH_API_URL_PICTURE + GRAPH_API_URL_PICTURE_TYPE + "large";
		public final static String GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL = GRAPH_API_URL_PICTURE_TYPE_LARGE + "&" + GRAPH_API_URL_PICTURE_SSL;
		public final static String GRAPH_API_URL_PICTURE_TYPE_NORMAL = GRAPH_API_URL_PICTURE + GRAPH_API_URL_PICTURE_TYPE + "normal";
		public final static String GRAPH_API_URL_PICTURE_TYPE_NORMAL_SSL = GRAPH_API_URL_PICTURE_TYPE_NORMAL + "&" + GRAPH_API_URL_PICTURE_SSL;
		public final static String GRAPH_API_URL_PICTURE_TYPE_SMALL = GRAPH_API_URL_PICTURE + GRAPH_API_URL_PICTURE_TYPE + "small";
		public final static String GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL = GRAPH_API_URL_PICTURE_TYPE_SMALL + "&" + GRAPH_API_URL_PICTURE_SSL;
		public final static String GRAPH_API_URL_PICTURE_TYPE_SQUARE = GRAPH_API_URL_PICTURE + GRAPH_API_URL_PICTURE_TYPE + "square";
		public final static String GRAPH_API_URL_PICTURE_TYPE_SQUARE_SSL = GRAPH_API_URL_PICTURE_TYPE_SQUARE + "&" + GRAPH_API_URL_PICTURE_SSL;


	}




	/**
	 * Class for constants and methods relating to Typefaces.
	 */
	public final static class TypefaceUtilities
	{


		private static Typeface typefaceBlueHighwayD;
		private static Typeface typefaceBlueHighwayRG;
		private static Typeface typefaceSnellRoundHandBDSCR;
		private static Typeface typefaceSnellRoundHandBLKSCR;


		public final static byte BLUE_HIGHWAY_D = 0;
		public final static byte BLUE_HIGHWAY_RG = 1;
		public final static byte SNELL_ROUNDHAND_BDSCR = 10;
		public final static byte SNELL_ROUNDHAND_BLKSCR = 11;
		private final static String PATH = "fonts/";
		private final static String BLUE_HIGHWAY_D_PATH = PATH + "blue_highway_d.ttf";
		private final static String BLUE_HIGHWAY_RD_PATH = PATH + "blue_highway_rg.ttf";
		private final static String SNELL_ROUNDHAND_BDSCR_PATH = PATH + "snell_roundhand_bdscr.otf";
		private final static String SNELL_ROUNDHAND_BLKSCR_PATH = PATH + "snell_roundhand_blkscr.otf";


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
				case BLUE_HIGHWAY_D:
					if (typefaceBlueHighwayD == null)
					{
						typefaceBlueHighwayD = Typeface.createFromAsset(assetManager, BLUE_HIGHWAY_D_PATH);
					}

					return typefaceBlueHighwayD;

				case BLUE_HIGHWAY_RG:
					if (typefaceBlueHighwayRG == null)
					{
						typefaceBlueHighwayRG = Typeface.createFromAsset(assetManager, BLUE_HIGHWAY_RD_PATH);
					}

					return typefaceBlueHighwayRG;

				case SNELL_ROUNDHAND_BDSCR:
					if (typefaceSnellRoundHandBDSCR == null)
					{
						typefaceSnellRoundHandBDSCR = Typeface.createFromAsset(assetManager, SNELL_ROUNDHAND_BDSCR_PATH);
					}

					return typefaceSnellRoundHandBDSCR;

				case SNELL_ROUNDHAND_BLKSCR:
					if (typefaceSnellRoundHandBLKSCR == null)
					{
						typefaceSnellRoundHandBLKSCR = Typeface.createFromAsset(assetManager, SNELL_ROUNDHAND_BLKSCR_PATH);
					}

					return typefaceSnellRoundHandBLKSCR;

				default:
					return null;
			}
		}


	}




	/**
	 * Class for tons of stuff relating to communication with the Classy Games
	 * server.
	 */
	public final static class ServerUtilities
	{


		public final static String MIMETYPE_JSON = "application/json";




		/**
		 * Class for constants relating to Server address values.
		 */
		public final static class Addresses
		{


			public final static String MAIN_ADDRESS = "http://classygames.elasticbeanstalk.com/";
			public final static String GET_GAME = "GetGame";
			public final static String GET_GAME_ADDRESS = MAIN_ADDRESS + GET_GAME;
			public final static String GET_GAMES = "GetGames";
			public final static String GET_GAMES_ADDRESS = MAIN_ADDRESS + GET_GAMES;
			public final static String NEW_GAME = "NewGame";
			public final static String NEW_GAME_ADDRESS = MAIN_ADDRESS + NEW_GAME;
			public final static String NEW_MOVE = "NewMove";
			public final static String NEW_MOVE_ADDRESS = MAIN_ADDRESS + NEW_MOVE;
			public final static String NEW_REG_ID = "NewRegId";
			public final static String NEW_REG_ID_ADDRESS = MAIN_ADDRESS + NEW_REG_ID;
			public final static String REMOVE_REG_ID = "RemoveRegId";
			public final static String REMOVE_REG_ID_ADDRESS = MAIN_ADDRESS + REMOVE_REG_ID;


		}




		/**
		 * Class for constants and methods relating to Server Post Data values.
		 */
		public final static class PostData
		{


			public final static String POST_DATA = "json";
			public final static String POST_DATA_BOARD = "board";
			public final static String POST_DATA_ERROR = "error";
			public final static String POST_DATA_FINISHED = "finished";
			public final static String POST_DATA_GAME_ID = "game_id";
			public final static String POST_DATA_ID = "id";
			public final static String POST_DATA_LAST_MOVE = "last_move";
			public final static String POST_DATA_NAME = "name";
			public final static String POST_DATA_REG_ID = "reg_id";
			public final static String POST_DATA_RESULT = "result";
			public final static String POST_DATA_TURN = "turn";
			public final static String POST_DATA_TURN_THEIRS = "turn_theirs";
			public final static String POST_DATA_TURN_YOURS = "turn_yours";
			public final static String POST_DATA_TYPE = "type";
			public final static byte POST_DATA_TYPE_NEW_GAME = 1;
			public final static byte POST_DATA_TYPE_NEW_MOVE = 2;
			public final static byte POST_DATA_TYPE_GAME_OVER_LOSE = 7;
			public final static byte POST_DATA_TYPE_GAME_OVER_WIN = 15;
			public final static String POST_DATA_SUCCESS = "success";
			public final static String POST_DATA_USER_CHALLENGED = "user_challenged";
			public final static String POST_DATA_USER_CREATOR = "user_creator";


			public static boolean validGameTypeValue(final byte gameType)
			{
				switch (gameType)
				{
					case POST_DATA_TYPE_NEW_GAME:
					case POST_DATA_TYPE_NEW_MOVE:
					case POST_DATA_TYPE_GAME_OVER_LOSE:
					case POST_DATA_TYPE_GAME_OVER_WIN:
						return true;

					default:
						return false;
				}
			}


			public static boolean validWinOrLoseValue(final byte winOrLose)
			{
				switch (winOrLose)
				{
					case POST_DATA_TYPE_GAME_OVER_LOSE:
					case POST_DATA_TYPE_GAME_OVER_WIN:
						return true;

					default:
						return false;
				}
			}


		}




		private static boolean GCMParseServerResults(final String jsonString)
		{
			boolean returnValue = false;

			try
			{
				Log.d(Utilities.LOG_TAG, "Parsing JSON data: " + jsonString);
				final JSONObject jsonData = new JSONObject(jsonString);
				final JSONObject jsonResult = jsonData.getJSONObject(PostData.POST_DATA_RESULT);

				try
				{
					final String successMessage = jsonResult.getString(PostData.POST_DATA_SUCCESS);
					Log.d(Utilities.LOG_TAG, "Server returned success with message: " + successMessage);

					returnValue = true;
				}
				catch (final JSONException e)
				{
					try
					{
						final String errorMessage = jsonResult.getString(PostData.POST_DATA_ERROR);
						Log.d(Utilities.LOG_TAG, "Server returned error with message: " + errorMessage);
					}
					catch (final JSONException e1)
					{
						Log.e(Utilities.LOG_TAG, "Data returned from server contained no error message.", e1);
					}
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Server returned message that was unable to be properly parsed.", e);
			}

			return returnValue;
		}


		public static void GCMRegister(final String reg_id, final Context context)
		{
			Log.d(Utilities.LOG_TAG, "Registering device with reg_id of \"" + reg_id + "\" from GCM server.");

			final Person whoAmI = Utilities.WhoAmIUtilities.getWhoAmI(context);
			if (whoAmI.isValid())
			{
				// build the data to be sent to the server
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(PostData.POST_DATA_ID, Long.valueOf(whoAmI.getId()).toString()));
				nameValuePairs.add(new BasicNameValuePair(PostData.POST_DATA_NAME, whoAmI.getName()));
				nameValuePairs.add(new BasicNameValuePair(PostData.POST_DATA_REG_ID, reg_id));

				try
				{
					if (GCMParseServerResults(postToServer(Addresses.NEW_REG_ID_ADDRESS, nameValuePairs)))
					{
						Log.d(Utilities.LOG_TAG, "Server successfully completed all the reg_id stuff.");
					}
				}
				catch (final IOException e)
				{
					Log.e(LOG_TAG, "IOException when trying to register GCM reg_id with server!");
				}
			}
		}


		public static void GCMUnregister(final String reg_id, final Context context)
		{
			Log.d(Utilities.LOG_TAG, "Unregistering device with reg_id of \"" + reg_id + "\" from GCM server.");

			// build the data to be sent to the server
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(PostData.POST_DATA_ID, Long.valueOf(Utilities.WhoAmIUtilities.getWhoAmI(context).getId()).toString()));
			nameValuePairs.add(new BasicNameValuePair(PostData.POST_DATA_REG_ID, reg_id));

			try
			{
				postToServer(Addresses.REMOVE_REG_ID_ADDRESS, nameValuePairs);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException when trying to unregister GCM reg_id from server!");
			}
		}


		/**
		 * Use this method to send data to and then receive a response from the server.
		 * 
		 * <p><strong>Examples</strong><br />
		 * ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_MOVE_ADDRESS, postData);<br />
		 * ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAMES_ADDRESS, postData);</p>
		 * 
		 * @param url
		 * The URL that you want to send your data to. This should be formulated using the URLs found
		 * in this class.
		 * 
		 * @param data
		 * Data to be sent to the server using HTTP POST. This ArrayList will need to be constructed
		 * outside of this method.
		 * <p><strong>Example of data creation</strong><br />
		 * ArrayList&#60;NameValuePair&#62; postData = new ArrayList&#60;NameValuePair&#62;();<br />
		 * postData.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, Long.valueOf(id).toString());<br />
		 * postData.add(new BasicNameValuePair(ServerUtilities.POST_DATA_REG_ID, reg_id);<br />
		 * Note that both values in the BasicNameValuePair <strong>must</strong> be a String.</p>
		 * 
		 * @return
		 * The server's response as a String. This will need to be parsed as it is JSON data.
		 * <strong>There is a slight possibility that the data String returned from this method will be
		 * null.</strong> Please check for that <strong>as well as</strong> if the String is empty.
		 */
		public static String postToServer(final String url, final ArrayList<NameValuePair> data) throws IOException
		{
			Log.d(Utilities.LOG_TAG, "Posting data to server at " + url);
			String jsonString = null;

			try
			{
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(data));

				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);

				InputStream inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null)
				{
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));
					StringBuilder stringBuilder = new StringBuilder();

					for (String line = new String(); line != null; line = bufferedReader.readLine())
					{
						stringBuilder.append(line);
					}

					jsonString = new String(stringBuilder.toString());
					Log.d(Utilities.LOG_TAG, "Parsed result from server: " + jsonString);
				}
			}
			catch (final IllegalArgumentException e)
			{
				Log.e(Utilities.LOG_TAG, "Error in HTTP connection.", e);
			}

			return jsonString;
		}


	}




	/**
	 * Class for constants and methods relating to the current user of the app.
	 */
	public final static class WhoAmIUtilities
	{


		private static Person whoAmI;
		private final static String WHO_AM_I_ID = "WHO_AM_I_ID";
		private final static String WHO_AM_I_NAME = "WHO_AM_I_NAME";


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


	}




	/**
	 * Invalidates the options menu using the Android compatibility library.
	 * 
	 * @param sherlockActivity
	 * getSherlockActivity()
	 */
	public static void compatInvalidateOptionsMenu(final SherlockActivity sherlockActivity)
	{
		ActivityCompat.invalidateOptionsMenu(sherlockActivity);
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
