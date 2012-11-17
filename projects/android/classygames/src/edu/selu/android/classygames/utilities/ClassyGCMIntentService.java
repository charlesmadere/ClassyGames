package edu.selu.android.classygames.utilities;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
public class ClassyGCMIntentService extends IntentService
{


	private static final Object LOCK = ClassyGCMIntentService.class;
	private static PowerManager.WakeLock wakeLock;


	public ClassyGCMIntentService()
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
		final long personId = intent.getLongExtra(ServerUtilities.POST_DATA_ID, -1);
		final String personName = intent.getStringExtra(ServerUtilities.POST_DATA_NAME);

		if (personId >= 0 && personName != null && !personName.isEmpty())
		{
			Person person = new Person(personId, personName);

			// build a notification to show to the user
			NotificationCompat.Builder builder = new NotificationCompat.Builder(ClassyGCMIntentService.this);
			builder.setSmallIcon(R.drawable.notification);
			builder.setContentTitle(ClassyGCMIntentService.this.getString(R.string.notification_title));
			builder.setLargeIcon(BitmapFactory.decodeResource(ClassyGCMIntentService.this.getResources(), R.drawable.notification));

			switch (intent.getIntExtra(ServerUtilities.GCM_TYPE, ServerUtilities.GCM_TYPE_NEW_GAME))
			{
				case ServerUtilities.GCM_TYPE_NEW_GAME:
					builder.setContentText(ClassyGCMIntentService.this.getString(R.string.notification_new_game_text, person.getName()));
					break;

				case ServerUtilities.GCM_TYPE_NEW_MOVE:
					builder.setContentText(ClassyGCMIntentService.this.getString(R.string.notification_new_move_text, person.getName()));
					break;
			}

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(ClassyGCMIntentService.this);
			stackBuilder.addParentStack(GamesListActivity.class);

			Intent gameIntent = new Intent(this, CheckersGameActivity.class);
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_GAME_ID, gameId);
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID, person.getId());
			gameIntent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME, person.getName());

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
		final String reg_id = intent.getStringExtra("registration_id");
		final String error = intent.getStringExtra("error");
		final String unregistered = intent.getStringExtra("unregistered");

		if (reg_id != null && !reg_id.isEmpty())
		// registration succeeded
		{
			// store registration ID in shared preferences
			// notify 3rd party server about the registered ID
			ServerUtilities.GCMRegister(reg_id);
		}

		if (unregistered != null && !unregistered.isEmpty())
		// unregistration succeeded
		{
			// get old registration ID from shared preferences
			// notify 3rd party server about the unregistered ID
			ServerUtilities.GCMUnregister(reg_id);
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
		intent.setClassName(context, ClassyGCMIntentService.class.getName());
		context.startService(intent);
	}


}
