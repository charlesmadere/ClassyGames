package edu.selu.android.classygames.utilities;


/**
 * Class for constants and methods relating to Facebook.
 */
public final class FacebookUtilities
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
