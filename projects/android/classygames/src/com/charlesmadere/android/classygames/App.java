package com.charlesmadere.android.classygames;


import android.app.Application;
import android.content.Context;


/**
 * This class is run before everything else in the entire app. Read more about
 * it in the Android documentation here:
 * https://developer.android.com/reference/android/app/Application.html
 *
 * Currently, this class is only really used to hold an always-readily
 * available Context object.
 */
public final class App extends Application
{


	private static Context context;




	@Override
	public void onCreate()
	{
		super.onCreate();
		context = getApplicationContext();
	}


	/**
	 * The Context that this returns should really only be used for systems
	 * that need to stay alive for long periods of time and maintain state
	 * between the app's different screens.
	 *
	 * @return
	 * Returns a definitely-ready-to-use Context variable.
	 */
	public static Context getContext()
	{
		return context;
	}


}
