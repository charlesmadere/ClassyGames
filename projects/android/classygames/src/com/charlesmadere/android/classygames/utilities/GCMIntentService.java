package com.charlesmadere.android.classygames.utilities;


import java.io.IOException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.charlesmadere.android.classygames.GameFragmentActivity;
import com.charlesmadere.android.classygames.GameOverActivity;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;


/**
 * Much of this was taken from the official Android documentation.
 * https://developer.android.com/guide/google/gcm/gcm.html#receiving
 */
public class GCMIntentService extends IntentService
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GCMIntentService";

	private final static Object LOCK = GCMIntentService.class;
	private static PowerManager.WakeLock wakeLock;

	public final static int GCM_NOTIFICATION_ID = 0;
	public final static int GCM_NOTIFICATION_LIGHTS = 0xEDB3C900;
	public final static int GCM_NOTIFICATION_LIGHTS_ON = 1000; // milliseconds
	public final static int GCM_NOTIFICATION_LIGHTS_OFF = 16000; // milliseconds

	public final static String PREFERENCES = "PREFERENCES";
	public final static String PREFERENCES_FILE = PREFERENCES + "_GCMIntentService";
	public final static String PREFERENCES_REG_ID = PREFERENCES + "_REG_ID";




	public GCMIntentService()
	{
		super(KeysAndConstants.GOOGLE_API_KEY);
	}


	@Override
	protected void onHandleIntent(final Intent intent)
	{
		try
		{
			final String action = intent.getAction();

			if (Utilities.verifyValidString(action))
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
		final String parameter_gameId = intent.getStringExtra(ServerUtilities.POST_DATA_GAME_ID);
		final String parameter_gameType = intent.getStringExtra(ServerUtilities.POST_DATA_GAME_TYPE);
		final String parameter_personId = intent.getStringExtra(ServerUtilities.POST_DATA_ID);
		final String parameter_messageType = intent.getStringExtra(ServerUtilities.POST_DATA_MESSAGE_TYPE);
		final String parameter_personName = intent.getStringExtra(ServerUtilities.POST_DATA_NAME);

		if (Utilities.verifyValidStrings(parameter_gameId, parameter_gameType, parameter_personId, parameter_messageType, parameter_personName))
		{
			final Byte gameType = Byte.valueOf(parameter_gameType);
			final Byte messageType = Byte.valueOf(parameter_messageType);
			final Long personId = Long.valueOf(parameter_personId);

			if (Person.isIdValid(personId.longValue()) && Person.isNameValid(parameter_personName) &&
				(ServerUtilities.validGameTypeValue(gameType.byteValue()) || ServerUtilities.validMessageTypeValue(messageType.byteValue())))
			{
				final Person person = new Person(personId, parameter_personName);

				// build a notification to show to the user
				final Builder builder = new Builder(this)
					.setAutoCancel(true)
					.setContentTitle(getString(R.string.notification_title))
					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_raw))
					.setLights(GCM_NOTIFICATION_LIGHTS, GCM_NOTIFICATION_LIGHTS_ON, GCM_NOTIFICATION_LIGHTS_OFF)
					.setOnlyAlertOnce(false)
					.setSmallIcon(R.drawable.notification_small);

				final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

				if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME
					|| messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
				{
					final Intent gameIntent = new Intent(this, GameFragmentActivity.class)
						.putExtra(GameFragmentActivity.BUNDLE_DATA_GAME_ID, parameter_gameId)
						.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_ID, person.getId())
						.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_NAME, person.getName())
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

					stackBuilder.addNextIntentWithParentStack(gameIntent);
					builder.setTicker(getString(R.string.notification_sent_some_class, person.getName()));

					if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME)
					{
						builder.setContentText(getString(R.string.notification_new_game_text, person.getName()));
					}
					else if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
					{
						builder.setContentText(getString(R.string.notification_new_move_text, person.getName()));
					}
				}
				else if (ServerUtilities.validWinOrLoseValue(messageType.byteValue()))
				// it's a GAME_OVER byte
				{
					final Intent gameOverIntent = new Intent(this, GameOverActivity.class)
						.putExtra(GameOverActivity.BUNDLE_DATA_MESSAGE_TYPE, messageType.byteValue())
						.putExtra(GameOverActivity.BUNDLE_DATA_PERSON_OPPONENT_ID, person.getId())
						.putExtra(GameOverActivity.BUNDLE_DATA_PERSON_OPPONENT_NAME, person.getName());

					stackBuilder.addNextIntentWithParentStack(gameOverIntent);
					builder.setTicker(getString(R.string.notification_game_over_text, person.getName()));

					if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE)
					{
						builder.setContentText(getString(R.string.notification_game_over_lose_text, person.getName()));
					}
					else if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN)
					{
						builder.setContentText(getString(R.string.notification_game_over_win_text, person.getName()));
					}
				}

				final PendingIntent gamePendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				builder.setContentIntent(gamePendingIntent);

				// show the notification
				final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(GCM_NOTIFICATION_ID, builder.build());
			}
		}
		else
		{
			Log.e(LOG_TAG, "Received malformed GCM message!");
		}
	}


	private void handleRegistration(final Intent intent)
	{
		final String regId = intent.getStringExtra("registration_id");
		if (Utilities.verifyValidString(regId))
		// registration succeeded
		{
			final SharedPreferences sPreferences = Utilities.getDefaultSharedPreferences(this);

			// get old registration ID from shared preferences
			final String preferencesRegId = sPreferences.getString(PREFERENCES_REG_ID, null);

			if (!Utilities.verifyValidString(preferencesRegId) || !preferencesRegId.equals(regId))
			// the two regIds are not the the same. replace the existing stored regId
			// with this new regId
			{
				// store new regId
				final SharedPreferences.Editor editor = sPreferences.edit();
				editor.putString(PREFERENCES_REG_ID, regId);
				editor.commit();

				try
				{
					// notify 3rd party server about the new regId
					ServerUtilities.gcmRegister(regId, this);
				}
				catch (final IOException e)
				{
					Log.e(LOG_TAG, "IOException during GCMUnregister!", e);
				}
			}
		}

		final String unregistered = intent.getStringExtra("unregistered");
		if (Utilities.verifyValidString(unregistered))
		// unregistration succeeded
		{
			final SharedPreferences sharedPreferences = Utilities.getDefaultSharedPreferences(this);

			// get old registration ID from shared preferences
			final String preferencesRegId = sharedPreferences.getString(PREFERENCES_REG_ID, null);

			if (Utilities.verifyValidString(preferencesRegId))
			// ensure that the String we obtained from shared preferences contains text
			{
				try
				{
					// notify 3rd party server about the unregistered ID
					ServerUtilities.gcmUnregister(preferencesRegId, this);
				}
				catch (final IOException e)
				{
					Log.e(LOG_TAG, "IOException during GCMUnregister!", e);
				}
			}
		}

		final String error = intent.getStringExtra("error");
		if (Utilities.verifyValidString(error))
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
				Log.e(LOG_TAG, "Received error: " + error);
			}
		}
	}


	static void runIntentInService(final Context context, final Intent intent)
	{
		synchronized (LOCK)
		{
			if (wakeLock == null)
			{
				final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "classy_wakelock");
			}
		}

		wakeLock.acquire();
		intent.setClassName(context, GCMIntentService.class.getName());
		context.startService(intent);
	}


}
