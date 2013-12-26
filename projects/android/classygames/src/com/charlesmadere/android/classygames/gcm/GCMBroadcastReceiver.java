package com.charlesmadere.android.classygames.gcm;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;


/**
 * This code is largely taken from the Android GCM documentation:
 * https://developer.android.com/google/gcm/client.html#sample-receive
 */
public final class GCMBroadcastReceiver extends WakefulBroadcastReceiver
{


	@Override
	public final void onReceive(final Context context, final Intent intent)
	{
		final String packageName = context.getPackageName();
		final String className = GCMIntentService.class.getName();

		// Explicitly specify that the GCMIntentService will handle the Intent.
		final ComponentName component = new ComponentName(packageName, className);
		intent.setComponent(component);

		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, intent);
		setResultCode(Activity.RESULT_OK);
	}


}
