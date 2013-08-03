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
	 *
	 *
	 * @param assetManager
	 *
	 */
	public static void initTypefaces(final AssetManager assetManager)
	{
		blueHighway = Typeface.createFromAsset(assetManager, BLUE_HIGHWAY_PATH);
		snellRoundhand = Typeface.createFromAsset(assetManager, SNELL_ROUNDHAND_PATH);
	}


	/**
	 * Applies the BlueHighway typeface to the supplied TextView object.
	 *
	 * @param view
	 * The TextView object to apply the typeface to.
	 */
	public static void applyBlueHighway(final TextView view)
	{
		view.setTypeface(getBlueHighway());
	}


	/**
	 * Applies the Snell Roundhand typeface to the supplied TextView object.
	 *
	 * @param view
	 * The TextView object to apply the typeface to.
	 */
	public static void applySnellRoundhand(final TextView view)
	{
		view.setTypeface(getSnellRoundhand());
	}


	/**
	 * @return
	 * Returns the Blue Highway typeface.
	 */
	public static Typeface getBlueHighway()
	{
		return blueHighway;
	}


	/**
	 * @return
	 * Returns the Snell Roundhand typeface.
	 */
	public static Typeface getSnellRoundhand()
	{
		return snellRoundhand;
	}


}
