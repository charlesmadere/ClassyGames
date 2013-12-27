package com.charlesmadere.android.classygames.utilities;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import com.charlesmadere.android.classygames.App;


/**
 * Class for constants and methods relating to Typefaces.
 */
public final class Typefaces
{


	private final static String LOG_TAG = App.BASE_TAG + Typefaces.class.getSimpleName();

	private final static String TYPEFACES_PATH = "typefaces/";
	private final static String BLUE_HIGHWAY_PATH = TYPEFACES_PATH + "blue_highway.ttf";
	private final static String SNELL_ROUNDHAND_PATH = TYPEFACES_PATH + "snell_roundhand.otf";

	private static Typeface blueHighway;
	private static Typeface snellRoundhand;


	private static Typeface createFromAsset(final String path)
	{
		Log.d(LOG_TAG, "Creating asset from path: \"" + path + "\".");
		final AssetManager assets = App.getContext().getAssets();
		return Typeface.createFromAsset(assets, path);
	}


	/**
	 * @return
	 * Returns to you a ready-to-use Typeface instance of Blue Highway.
	 */
	public static Typeface getBlueHighway()
	{
		if (blueHighway == null)
		{
			blueHighway = createFromAsset(BLUE_HIGHWAY_PATH);
		}

		return blueHighway;
	}


	/**
	 * @return
	 * Returns to you a ready-to-use Typeface instance of Snell Roundhand.
	 */
	public static Typeface getSnellRoundhand()
	{
		if (snellRoundhand == null)
		{
			snellRoundhand = createFromAsset(SNELL_ROUNDHAND_PATH);
		}

		return snellRoundhand;
	}


}
