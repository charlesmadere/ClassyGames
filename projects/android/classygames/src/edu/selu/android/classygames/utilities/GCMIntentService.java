package edu.selu.android.classygames.utilities;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;


/**
 * Much of this was taken from the official Android documentation.
 * https://developer.android.com/guide/google/gcm/gcm.html#receiving
 */
public class GCMIntentService extends IntentService
{


	private static PowerManager.WakeLock wakeLock;
	private static final Object LOCK = GCMIntentService.class;


	public GCMIntentService(final String name)
	{
		super(name);

	}


	@Override
	protected void onHandleIntent(final Intent intent)
	{
		try
		{
			final String action = intent.getAction();

			if (action != null && !action.isEmpty())
			{
				if (action.equals("com.google.android.c2dm.intent.REGISTRATION"))
				{
					handleRegistration(intent);
				}
				else if (action.equals("com.google.android.c2dm.intent.RECEIVE"))
				{
					handleMessage(intent);
				}
			}
		}
		finally
		{
			synchronized (LOCK)
			{
				wakeLock.release();
			}
		}
	}


	private void handleMessage(final Intent intent)
	{

	}


	private void handleRegistration(final Intent intent)
	{
		final String regId = intent.getStringExtra("registration_id");
		final String error = intent.getStringExtra("error");
		final String unregistered = intent.getStringExtra("unregistered");

		if (regId != null && !regId.isEmpty())
		// registration succeeded
		{
			// store registration ID in shared preferences
			// notify 3rd party server about the registered ID
		}

		if (unregistered != null && !unregistered.isEmpty())
		// unregistration succeeded
		{
			// get old registration ID from shared preferences
			// notify 3rd party server about the unregistered ID
		}

		if (error != null && !error.isEmpty())
		// last operation (registration or unregistration) returned an error
		{
			if (error.equals("SERVICE_NOT_AVAILABLE"))
			{
				// optionally retry using exponential back-off
				// (see Advanced Topics)
			}
			else
			{
				// unrecoverable error, log it
				Log.d(Utilities.LOG_TAG, "Received error: " + error);
			}
		}
	}


	static void runIntentInService(final Context context, final Intent intent)
	{
		synchronized (LOCK)
		{
			if (wakeLock == null)
			{
				PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "classy_wakelock");
			}
		}

		wakeLock.acquire();
		intent.setClassName(context, GCMIntentService.class.getName());
		context.startService(intent);
	}


}
