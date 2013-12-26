package com.charlesmadere.android.classygames;


import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * This class's onCreate() is run before everything else in the entire app. Read more about it in
 * the Android documentation here:
 * https://developer.android.com/reference/android/app/Application.html
 */
public final class App extends Application
{


	public static String BASE_TAG = "Classy Games: ";

	private static Context context;
	private static ImageLoader imageLoader;




	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
		initializeImageLoader();
	}


	/**
	 * The Context that this returns should really only be used for systems that need to stay
	 * alive that need to stay alive for long periods of time and maintain state between the
	 * app's between the app's different screens.
	 *
	 * @return
	 * Returns a definitely-ready-to-use Context object.
	 */
	public static Context getContext()
	{
		return context;
	}


	/**
	 * @return
	 * Returns a definitely-ready-to-use ImageLoader object. This should be used to download
	 * images from a web URL and display them to an ImageView.
	 */
	public static ImageLoader getImageLoader()
	{
		return imageLoader;
	}


	private void initializeImageLoader()
	{
		final DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.build();

		final ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(context)
			.defaultDisplayImageOptions(displayOptions)
			.build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(loaderConfiguration);
	}


}
