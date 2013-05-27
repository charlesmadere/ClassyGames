package com.charlesmadere.android.classygames.utilities;


/**
 * Class for constants and methods relating to Facebook. Further documentation
 * on what this class is doing can be found on the Facebook website:
 * https://developers.facebook.com/docs/reference/api/using-pictures/
 */
public final class FacebookUtilities
{


	private final static String GRAPH_API_URL = "https://graph.facebook.com/";
	private final static String PICTURE = "/picture";
	private final static String TYPE = "?type=";
	private final static String RETURN_SSL_RESOURCES_1 = "&return_ssl_resources=1";

	private final static String PICTURE_LARGE = PICTURE + TYPE + "large" + RETURN_SSL_RESOURCES_1;
	private final static String PICTURE_NORMAL = PICTURE + TYPE + "normal" + RETURN_SSL_RESOURCES_1;
	private final static String PICTURE_SMALL = PICTURE + TYPE + "small" + RETURN_SSL_RESOURCES_1;
	private final static String PICTURE_SQUARE = PICTURE + TYPE + "square" + RETURN_SSL_RESOURCES_1;




	/**
	 *
	 *
	 * @param id
	 *
	 *
	 * @return
	 *
	 */
	public static String getFriendsPictureLarge(final long id)
	{
		return GRAPH_API_URL + id + PICTURE_LARGE;
	}


	/**
	 *
	 *
	 * @param id
	 *
	 *
	 * @return
	 *
	 */
	public static String getFriendsPictureNormal(final long id)
	{
		return GRAPH_API_URL + id + PICTURE_NORMAL;
	}


	/**
	 *
	 *
	 * @param id
	 *
	 *
	 * @return
	 *
	 */
	public static String getFriendsPictureSmall(final long id)
	{
		return GRAPH_API_URL + id + PICTURE_SMALL;
	}


	/**
	 *
	 *
	 * @param id
	 *
	 *
	 * @return
	 *
	 */
	public static String getFriendsPictureSquare(final long id)
	{
		return GRAPH_API_URL + id + PICTURE_SQUARE;
	}


}
