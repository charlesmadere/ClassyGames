package com.charlesmadere.android.classygames.utilities;


import java.io.IOException;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

	private final static int GCM_NOTIFICATION_ID = 0;
	private final static int GCM_NOTIFICATION_LIGHTS_ON = 1000; // milliseconds
	private final static int GCM_NOTIFICATION_LIGHTS_OFF = 16000; // milliseconds




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
				if (action.equals("com.google.android.c2dm.intent.RECEIVE"))
				{
					handleMessage(intent);
				}
				else if (action.equals("com.google.android.c2dm.intent.REGISTRATION"))
				{
					handleRegistration(intent);
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


	/**
	 * Processes a received push notification. Once this method has completed,
	 * a notification will be shown on the Android device's notification bar.
	 * If a notification is not showing, then that means that this method
	 * detected some sort of error with the received push notification's data.
	 * In that case, the fact that an error occurred will be Log.e'd.
	 *
	 * @param intent
	 * The Intent object as received from this class's onHandleIntent() method.
	 */
	private void handleMessage(final Intent intent)
	{
		// Retrieve input parameters for easier access. These input parameters
		// determine what type of push notification has been received.
		final String parameter_gameId = intent.getStringExtra(ServerUtilities.POST_DATA_GAME_ID);
		final String parameter_gameType = intent.getStringExtra(ServerUtilities.POST_DATA_GAME_TYPE);
		final String parameter_personId = intent.getStringExtra(ServerUtilities.POST_DATA_ID);
		final String parameter_messageType = intent.getStringExtra(ServerUtilities.POST_DATA_MESSAGE_TYPE);
		final String parameter_personName = intent.getStringExtra(ServerUtilities.POST_DATA_NAME);

		if (Utilities.verifyValidStrings(parameter_gameId, parameter_gameType, parameter_personId, parameter_messageType, parameter_personName))
		// Verify that all of these Strings are both not null and that their
		// length is greater than or equal to 1. This way we ensure that all of
		// this input data is not corrupt.
		{
			final Byte whichGame = Byte.valueOf(parameter_gameType);
			final Byte messageType = Byte.valueOf(parameter_messageType);
			final Long personId = Long.valueOf(parameter_personId);

			if (Person.isIdValid(personId.longValue()) && Person.isNameValid(parameter_personName) &&
					(ServerUtilities.validGameTypeValue(whichGame.byteValue()) || ServerUtilities.validMessageTypeValue(messageType.byteValue())))
			{
				handleVerifiedMessage(parameter_gameId, whichGame, messageType, personId, parameter_personName);
			}
			else
			{
				Log.e(LOG_TAG, "Received partially malformed GCM message!");
			}
		}
		else
		{
			Log.e(LOG_TAG, "Received completely malformed GCM message!");
		}
	}


	/**
	 * Attempts to register the user with the Classy Games server. This means
	 * that a server API request will have to be made. Note that because all
	 * HTTP requests have the capability of failing, this registration attempt
	 * may not work. But in all honesty it should work.
	 *
	 * @param intent
	 * The Intent object as received from this class's onHandleIntent() method.
	 */
	private void handleRegistration(final Intent intent)
	{
		final String regId = intent.getStringExtra("registration_id");
		if (Utilities.verifyValidString(regId))
		// registration succeeded
		{
			final String oldRegId = Utilities.getRegId(this);

			if (!Utilities.verifyValidString(oldRegId) || !oldRegId.equals(regId))
			// the two regIds are not the the same. replace the existing stored regId
			// with this new regId
			{
				// store new regId
				Utilities.setRegId(this, regId);

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
			try
			{
				// notify 3rd party server about the unregistered ID
				ServerUtilities.gcmUnregister(this);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException during GCMUnregister!", e);
			}
		}

		final String error = intent.getStringExtra("error");
		if (Utilities.verifyValidString(error))
		// last operation (registration or unregistration) returned an error
		{
			if (error.equals("SERVICE_NOT_AVAILABLE"))
			{
				// optionally retry using exponential back-off
				// (see Advanced Topics in Android documentation)
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


	/**
	 * Checks to see if the <i>Show Notification Light</li> setting is enabled.
	 * This is a simple checkbox setting.
	 *
	 * @return
	 * Returns true if the <i>Show Notification Light</i> setting is enabled.
	 * Returns false if the <i>Show Notification Light</i> setting is disabled.
	 */
	private boolean checkIfNotificationLightIsEnabled()
	{
		final SharedPreferences sPreferences = Utilities.getPreferences(this);
		final String key = getString(R.string.settings_key_show_notification_light);
		final boolean lightIsEnabled = sPreferences.getBoolean(key, true);

		return lightIsEnabled;
	}


	/**
	 * Further acts upon a push notification message as received from the
	 * Classy Games server. This method should only be used once all of the
	 * data extracted from the push notification message has been validated and
	 * verified. For a String, this means that it is both not null and not
	 * empty.
	 *
	 * @param gameId
	 * All push notifications must have a particular game that they refer to.
	 * This is that game's ID.
	 *
	 * @param whichGame
	 * This is which game the push notification is referring to. It can be
	 * checkers, chess...
	 *
	 * @param messageType
	 * The type of message that this is.
	 *
	 * @param personId
	 *
	 *
	 * @param personName
	 *
	 */
	private void handleVerifiedMessage(final String gameId, final Byte whichGame, final Byte messageType, final Long personId, final String personName)
	{
		final Person person = new Person(personId.longValue(), personName);

		// build a notification to show to the user
		final Builder builder = new Builder(this)
				.setAutoCancel(true)
				.setContentTitle(getString(R.string.classy_games))
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_raw))
				.setOnlyAlertOnce(true)
				.setSmallIcon(R.drawable.notification_small);

		if (checkIfNotificationLightIsEnabled())
		{
			// only turn on the notification light if the user has
			// specified that he or she wants it on
			builder.setLights(Color.MAGENTA, GCM_NOTIFICATION_LIGHTS_ON, GCM_NOTIFICATION_LIGHTS_OFF);
		}

		final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME
				|| messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
		{
			final Intent gameIntent = new Intent(this, GameFragmentActivity.class)
					.putExtra(GameFragmentActivity.BUNDLE_DATA_GAME_ID, gameId)
					.putExtra(GameFragmentActivity.BUNDLE_DATA_WHICH_GAME, whichGame.byteValue())
					.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_ID, person.getId())
					.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_NAME, person.getName())
					.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			stackBuilder.addNextIntentWithParentStack(gameIntent);
			builder.setTicker(getString(R.string.ol_x_sent_you_some_class, person.getName()));

			if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME)
			{
				builder.setContentText(getString(R.string.new_game_from_x, person.getName()));
			}
			else if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
			{
				builder.setContentText(getString(R.string.new_move_from_x, person.getName()));
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
			builder.setTicker(getString(R.string.game_over, person.getName()));

			if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE)
			{
				builder.setContentText(getString(R.string.you_lost_the_game_with_x, person.getName()));
			}
			else if (messageType.byteValue() == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN)
			{
				builder.setContentText(getString(R.string.you_won_the_game_with_x, person.getName()));
			}
		}

		final PendingIntent gamePendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(gamePendingIntent);

		// show the notification
		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(GCM_NOTIFICATION_ID, builder.build());
	}


}
