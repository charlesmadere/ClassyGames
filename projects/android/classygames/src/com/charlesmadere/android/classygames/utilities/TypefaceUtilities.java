package com.charlesmadere.android.classygames.utilities;


import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;


/**
 * Class for constants and methods relating to Typefaces.
 */
public final class TypefaceUtilities
{


	private static Typeface blueHighway;
	private static Typeface snellRoundhand;


	private final static String TYPEFACES_PATH = "typefaces/";
	private final static String BLUE_HIGHWAY_PATH = TYPEFACES_PATH + "blue_highway.ttf";
	private final static String SNELL_ROUNDHAND_PATH = TYPEFACES_PATH + "snell_roundhand.otf";




	/**
	 * Applies the BlueHighway typeface to the supplied TextView object.
	 *
	 * @param assetManager
	 * The AssetManager from the calling Activity or Fragment class. Usually
	 * this can be obtained from an Activity by just directly using the
	 * getAssets() method. From a Fragment you'll need to use the
	 * getResources().getAssets() method chain.
	 *
	 * @param view
	 * The TextView object to apply the typeface to.
	 */
	public static void applyTypefaceBlueHighway(final AssetManager assetManager, final TextView view)
	{
		view.setTypeface(getBlueHighwayTypeface(assetManager));
	}


	/**
	 * Applies the Snell Roundhand typeface to the supplied TextView object.
	 *
	 * @param assetManager
	 * The AssetManager from the calling Activity or Fragment class. Usually
	 * this can be obtained from an Activity by just directly using the
	 * getAssets() method. From a Fragment you'll need to use the
	 * getResources().getAssets() method chain.
	 *
	 * @param view
	 * The TextView object to apply the typeface to.
	 */
	public static void applyTypefaceSnellRoundhand(final AssetManager assetManager, final TextView view)
	{
		view.setTypeface(getSnellRoundhandTypeface(assetManager));
	}


	/**
	 * Retrieves a usable Blue Highway typeface object.
	 *
	 * @param assetManager
	 * The AssetManager from the Activity or Fragment class that is calling
	 * this method.
	 *
	 * @return
	 * Returns a ready-to-use typeface object.
	 */
	public static Typeface getBlueHighwayTypeface(final AssetManager assetManager)
	{
		if (blueHighway == null)
		{
			blueHighway = Typeface.createFromAsset(assetManager, BLUE_HIGHWAY_PATH);
		}

		return blueHighway;
	}


	/**
	 * Retrieves a usable Snell Roundhand typeface object.
	 *
	 * @param assetManager
	 * The AssetManager from the Activity or Fragment class that is calling
	 * this method.
	 *
	 * @return
	 * Returns a ready-to-use typeface object.
	 */
	public static Typeface getSnellRoundhandTypeface(final AssetManager assetManager)
	{
		if (snellRoundhand == null)
		{
			snellRoundhand = Typeface.createFromAsset(assetManager, SNELL_ROUNDHAND_PATH);
		}

		return snellRoundhand;
	}


}
