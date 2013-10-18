package com.charlesmadere.android.classygames.utilities;


import android.graphics.Typeface;
import android.widget.TextView;
import com.charlesmadere.android.classygames.App;


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
	 * Returns to you a definitely-not-null and ready-to-use Typeface for Blue
	 * Highway.
	 */
	public static Typeface getBlueHighway()
	{
		if (blueHighway == null)
		{
			blueHighway = Typeface.createFromAsset(App.getContext().getAssets(), BLUE_HIGHWAY_PATH);
		}

		return blueHighway;
	}


	/**
	 * @return
	 * Returns to you a definitely-not-null and ready-to-use Typeface for Snell
	 * Roundhand.
	 */
	public static Typeface getSnellRoundhand()
	{
		if (snellRoundhand == null)
		{
			snellRoundhand = Typeface.createFromAsset(App.getContext().getAssets(), SNELL_ROUNDHAND_PATH);
		}

		return snellRoundhand;
	}


}
