package edu.selu.android.classygames.utilities;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import edu.selu.android.classygames.CheckersGameActivity;
import edu.selu.android.classygames.GamesListActivity;
import edu.selu.android.classygames.R;
import edu.selu.android.classygames.SecretConstants;
import edu.selu.android.classygames.data.Person;


/**
 * Much of this was taken from the official Android documentation.
 * https://developer.android.com/guide/google/gcm/gcm.html#receiving
 */
public class GCMIntentService extends IntentService
{


	private final static Object LOCK = GCMIntentService.class;
	private static PowerManager.WakeLock wakeLock;

	private final static String PREFERENCES = "PREFERENCES";
	private final static String PREFERENCES_FILE = PREFERENCES + "_GCMIntentService";
	private final static String PREFERENCES_REG_ID = PREFERENCES + "_REG_ID";


	public GCMIntentService()
	{
		super(SecretConstants.GOOGLE_API_KEY);
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
		final String gameId = intent.getStringExtra(ServerUtilities.POST_DATA_GAME_ID);
		final Long personId = Long.valueOf(intent.getStringExtra(ServerUtilities.POST_DATA_ID));
		final String personName = intent.getStringExtra(ServerUtilities.POST_DATA_NAME);

		if (personId.longValue() >= 0 && personName != null && !personName.isEmpty())
		{
			Person person = new Person(personId, personName);

			// build a notification to show to the user
			NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMIntentService.this);
			builder.setSmallIcon(R.drawable.notification);
			builder.setContentTitle(GCMIntentService.this.getString(R.string.notification_title));
			builder.setLargeIcon(BitmapFactory.decodeResource(GCMIntentService.this.getResources(), R.drawable.notification));

			switch (Byte.valueOf(intent.getStringExtra(ServerUtilities.GCM_TYPE)))
			{
				case ServerUtilities.GCM_TYPE_NEW_GAME:
					builder.setContentText(GCMIntentService.this.getString(R.string.notification_new_game_text, person.getName()));
					break;

				case ServerUtilities.GCM_TYPE_NEW_MOVE:
					builder.setContentText(GCMIntentService.this.getString(R.string.notification_new_move_text, person.getName()));
					break;
			}

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(GCMIntentService.this);
			stackBuilder.addParentStack(GamesListActivity.class);

			Intent gameIntent = new Intent(this, CheckersGameActivity.class);
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_GAME_ID, gameId);
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID, person.getId());
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME, person.getName());
			stackBuilder.addNextIntent(gameIntent);

			final PendingIntent gamePendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
			builder.setContentIntent(gamePendingIntent);

			// show the notification
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(ServerUtilities.GCM_NOTIFICATION_ID, builder.build());
		}
		else
		{
			Log.e(Utilities.LOG_TAG, "Received malformed GCM message!");
		}
	}


	private void handleRegistration(final Intent intent)
	{
		final String regId = intent.getStringExtra("registration_id");
		if (regId != null && !regId.isEmpty())
		// registration succeeded
		{
			SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);

			// get old registration ID from shared preferences
			final String preferencesRegId = sharedPreferences.getString(PREFERENCES_REG_ID, null);

			if (preferencesRegId == null || !preferencesRegId.equals(regId))
			// the two regIds are not the the same. replace the existing stored regId
			// with this new regId
			{
				// store new regId
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(PREFERENCES_REG_ID, regId);
				editor.commit();

				// notify 3rd party server about the new regId
				ServerUtilities.GCMRegister(regId);
			}
		}

		final String unregistered = intent.getStringExtra("unregistered");
		if (unregistered != null && !unregistered.isEmpty())
		// unregistration succeeded
		{
			SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES_FILE, MODE_PRIVATE);

			// get old registration ID from shared preferences
			final String preferencesRegId = sharedPreferences.getString(PREFERENCES_REG_ID, null);

			if (preferencesRegId != null && !preferencesRegId.isEmpty())
			// ensure that the String we obtained from shared preferences contains text
			{
				// notify 3rd party server about the unregistered ID
				ServerUtilities.GCMUnregister(preferencesRegId);
			}
		}

		final String error = intent.getStringExtra("error");
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
				Log.e(Utilities.LOG_TAG, "Received error: " + error);
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
