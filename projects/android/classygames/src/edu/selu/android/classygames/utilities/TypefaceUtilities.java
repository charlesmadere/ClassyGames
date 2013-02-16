package edu.selu.android.classygames.utilities;


import android.content.res.AssetManager;
import android.graphics.Typeface;


/**
 * Class for constants and methods relating to Typefaces.
 */
public final class TypefaceUtilities
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
