package com.charlesmadere.android.classygames.utilities;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.io.IOException;


/**
 * Much of the code in this class was taken from the official Android
 * documentation website.
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
			if (error.equalsIgnoreCase("SERVICE_NOT_AVAILABLE"))
			{
				// optionally retry using exponential back-off
				// (see Advanced Topics in Android documentation)
				Log.d(LOG_TAG, "Received error: " + error);
			}
			else
			{
				// unrecoverable error, log it
				Log.e(LOG_TAG, "Received error: " + error);
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

		if (Utilities.verifyValidStrings(parameter_gameId, parameter_gameType, parameter_personId,
			parameter_messageType, parameter_personName))
		// Verify that all of these Strings are both not null and that their
		// length is greater than or equal to 1. This way we ensure that all of
		// this input data is not corrupt.
		{
			final byte whichGame = Byte.parseByte(parameter_gameType);
			final byte messageType = Byte.parseByte(parameter_messageType);
			final long personId = Long.parseLong(parameter_personId);

			if (Person.isIdAndNameValid(personId, parameter_personName) &&
					(ServerUtilities.validGameTypeValue(whichGame) || ServerUtilities.validMessageTypeValue(messageType)))
			{
				final Person person = new Person(personId, parameter_personName);

				handleVerifiedMessage(parameter_gameId, whichGame, messageType, person);
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
	 * The type of message that this is. Could be new game, new move, game over
	 * lose, or game over win.
	 *
	 * @param person
	 * The Facebook friend that caused this push notification to be sent.
	 */
	private void handleVerifiedMessage(final String gameId, final byte whichGame, final byte messageType, final Person person)
	{
		// build a notification to show to the user
		final Builder builder = new Builder(this)
			.setAutoCancel(true)
			.setContentTitle(getString(R.string.classy_games))
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_raw))
			.setOnlyAlertOnce(true)
			.setSmallIcon(R.drawable.notification_small);

		if (Utilities.checkIfSettingIsEnabled(this, R.string.settings_key_show_notification_light, true))
		{
			// only turn on the notification light if the user has
			// specified that he or she wants it on
			builder.setLights(Color.MAGENTA, GCM_NOTIFICATION_LIGHTS_ON, GCM_NOTIFICATION_LIGHTS_OFF);
		}

		if (messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME
				|| messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
		// Check to see if the type of the received push notification is either
		// a new game or a new move.
		{
			handleNewGameOrNewMoveMessage(builder, gameId, whichGame, messageType, person);
		}
		else if (ServerUtilities.validWinOrLoseValue(messageType))
		// Check to see if the type of the received push notification is either
		// a game loss or a game won.
		{
			handleWinOrLoseMessage(builder, messageType, person);
		}
		else
		// The received message was of a type that doesn't make any sense. Log
		// it as an error.
		{
			Log.e(LOG_TAG, "Received GCM message that contained an unknown message type of \"" + messageType + "\".");
		}
	}


	/**
	 * Handles building an Android notification that represents a new game or
	 * a new move.
	 *
	 * @param builder
	 * A partially built notification Builder object.
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
	 * The type of message that this is. Could be new game, new move, game over
	 * lose, or game over win.
	 *
	 * @param person
	 * The Facebook friend that caused this push notification to be sent.
	 */
	private void handleNewGameOrNewMoveMessage(final Builder builder, final String gameId, final byte whichGame, final byte messageType, final Person person)
	{
		final Intent gameIntent = new Intent(this, GameFragmentActivity.class)
			.putExtra(GameFragmentActivity.BUNDLE_DATA_GAME_ID, gameId)
			.putExtra(GameFragmentActivity.BUNDLE_DATA_WHICH_GAME, whichGame)
			.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_ID, person.getId())
			.putExtra(GameFragmentActivity.BUNDLE_DATA_PERSON_OPPONENT_NAME, person.getName())
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addNextIntentWithParentStack(gameIntent);
		builder.setTicker(getString(R.string.ol_x_sent_you_some_class, person.getName()));

		if (messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_GAME)
		{
			builder.setContentText(getString(R.string.new_game_from_x, person.getName()));
		}
		else if (messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
		{
			builder.setContentText(getString(R.string.new_move_from_x, person.getName()));
		}

		buildAndShowNotification(builder, stackBuilder);
	}


	/**
	 * Handles building an Android notification that represents a won or a lost
	 * game.
	 *
	 * @param builder
	 * A partially built notification Builder object.
	 *
	 * @param messageType
	 * The type of message that this is. Could be new game, new move, game over
	 * lose, or game over win.
	 *
	 * @param person
	 * The Facebook friend that caused this push notification to be sent.
	 */
	private void handleWinOrLoseMessage(final Builder builder, final byte messageType, final Person person)
	{
		final Intent gameOverIntent = new Intent(this, GameOverActivity.class)
			.putExtra(GameOverActivity.BUNDLE_MESSAGE_TYPE, messageType)
			.putExtra(GameOverActivity.BUNDLE_PERSON_OPPONENT_ID, person.getId())
			.putExtra(GameOverActivity.BUNDLE_PERSON_OPPONENT_NAME, person.getName());

		final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addNextIntentWithParentStack(gameOverIntent);
		builder.setTicker(getString(R.string.game_over, person.getName()));

		if (messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE)
		{
			builder.setContentText(getString(R.string.you_lost_the_game_with_x, person.getName()));
		}
		else if (messageType == ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN)
		{
			builder.setContentText(getString(R.string.you_won_the_game_with_x, person.getName()));
		}

		buildAndShowNotification(builder, stackBuilder);
	}


	/**
	 * This method will finalize and, finally, show a notification in the
	 * Android device's notification bar.
	 *
	 * @param builder
	 * A completely built notification Builder object.
	 *
	 * @param stackBuilder
	 * A TaskStackBuilder object that is completely prepared.
	 */
	private void buildAndShowNotification(final Builder builder, final TaskStackBuilder stackBuilder)
	{
		final PendingIntent gamePendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(gamePendingIntent);

		// show the notification
		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(GCM_NOTIFICATION_ID, builder.build());
	}


}
