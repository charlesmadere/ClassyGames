package com.charlesmadere.android.classygames.utilities;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Much of this was taken from the official Android documentation.
 * https://developer.android.com/guide/google/gcm/gcm.html#receiving
 */
public final class GCMBroadcastReceiver extends BroadcastReceiver
{


	@Override
	public final void onReceive(final Context context, final Intent intent)
	{
		GCMIntentService.runIntentInService(context, intent);
		setResult(Activity.RESULT_OK, null, null);
	}


}
