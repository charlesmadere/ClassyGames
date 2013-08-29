package com.charlesmadere.android.classygames.utilities;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.charlesmadere.android.classygames.GameFragmentActivity;
import com.charlesmadere.android.classygames.GameOverActivity;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Notification;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


/**
 * Much of the code in this class was taken from the official Android
 * documentation website.
 * https://developer.android.com/guide/google/gcm/gcm.html#receiving
 */
public final class GCMIntentService extends IntentService implements
	Comparator<Notification>
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GCMIntentService";
	private final static String PREFERENCES_NAME = "GCMIntentService_Preferences";

	private final static Object LOCK = GCMIntentService.class;
	private static PowerManager.WakeLock wakeLock;

	private final static int GCM_MAX_SIMULTANEOUS_NOTIFICATIONS = 4;
	private final static int GCM_NOTIFICATION_ID = 0;
	private final static int GCM_NOTIFICATION_LIGHTS_DURATION_ON = 1024; // milliseconds
	private final static int GCM_NOTIFICATION_LIGHTS_DURATION_OFF = 15360; // milliseconds
	private final static int GCM_NOTIFICATION_VIBRATION_DURATION = 160; // milliseconds




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
				if (action.equalsIgnoreCase("com.google.android.c2dm.intent.RECEIVE"))
				{
					handleMessage(intent);
				}
				else if (action.equalsIgnoreCase("com.google.android.c2dm.intent.REGISTRATION"))
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
					Server.gcmRegister(regId, this);
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
				Server.gcmUnregister(this);
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
		// Retrieve input parameters. These input parameters determine what
		// type of push notification has been received, as well as who sent us
		// this notification.

		// All push notifications must have a particular game that they refer to.
		// This is that game's ID.
		final String parameter_gameId = intent.getStringExtra(Server.POST_DATA_GAME_ID);

		// This is which game the push notification is referring to. It can be
		// checkers, chess...
		final String parameter_gameType = intent.getStringExtra(Server.POST_DATA_GAME_TYPE);

		// The type of message that this is. Could be new game, new move, game over
		// lose, or game over win.
		final String parameter_messageType = intent.getStringExtra(Server.POST_DATA_MESSAGE_TYPE);

		// The Facebook friend information of the guy that trigger this push
		// notification.
		final String parameter_personId = intent.getStringExtra(Server.POST_DATA_ID);
		final String parameter_personName = intent.getStringExtra(Server.POST_DATA_NAME);

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
				(Server.validGameTypeValue(whichGame) || Server.validMessageTypeValue(messageType)))
			{
				final Person person = new Person(personId, parameter_personName);
				final Notification notification = new Notification(parameter_gameId, whichGame, messageType, person);
				handleVerifiedMessage(notification);
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
	 * @param notification
	 * The bundled up notification data as received from this class's incoming
	 * Intent object.
	 */
	private void handleVerifiedMessage(final Notification notification)
	{
		final LinkedList<Notification> existingNotifications = saveCurrentNotificationAndGrabExisting(notification);

		// begin building a notification to show to the user
		final Builder builder = new Builder(this)
			.setAutoCancel(true)
			.setContentTitle(getString(R.string.classy_games))
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_large))
			.setSmallIcon(R.drawable.notification_small);

		if (Utilities.checkIfSettingIsEnabled(this, R.string.settings_key_show_notification_light, true))
		{
			// only turn on the notification light if the user has
			// specified that he or she wants it on
			builder.setLights(Color.MAGENTA, GCM_NOTIFICATION_LIGHTS_DURATION_ON, GCM_NOTIFICATION_LIGHTS_DURATION_OFF);
		}

		if (existingNotifications == null || existingNotifications.isEmpty())
		{
			if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_GAME
				|| notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
			// Check to see if the type of the received push notification is either
			// a new game or a new move.
			{
				handleNewGameOrNewMoveMessage(builder, notification);
			}
			else if (Server.validWinOrLoseValue(notification.getMessageType()))
			// Check to see if the type of the received push notification is either
			// a game loss or a game won.
			{
				handleWinOrLoseMessage(builder, notification);
			}
			else
			// The received message was of a type that doesn't make any sense. Log
			// it as an error.
			{
				Log.e(LOG_TAG, "Received GCM message that contained an unknown message type of \"" + notification.getMessageType() + "\".");
			}
		}
		else
		{
			builder.setContentText(getString(R.string.class_received));

			final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
				.setBigContentTitle(getString(R.string.top_hats_all_the_way_down));

			for (final Notification existingNotification : existingNotifications)
			{
				inboxStyle.addLine(existingNotification.getPerson() + " " + existingNotification.getMessageType());
			}

			builder.setStyle(inboxStyle);

			// COPYPASTA, 100% PLACEHOLDER
			final Bundle extras = new Bundle();
			extras.putSerializable(GameFragmentActivity.KEY_NOTIFICATION, notification);

			final Intent gameIntent = new Intent(this, GameFragmentActivity.class)
				.putExtras(extras)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			stackBuilder.addNextIntentWithParentStack(gameIntent);
			builder.setTicker("blah " + getString(R.string.ol_x_sent_you_some_class, notification.getPerson().getName()));

			if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_GAME)
			{
				builder.setContentText("game blah " + getString(R.string.new_game_from_x, notification.getPerson().getName()));
			}
			else if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
			{
				builder.setContentText("new blah " + getString(R.string.new_move_from_x, notification.getPerson().getName()));
			}

			buildAndShowNotification(builder, stackBuilder);
		}
	}


	/**
	 * Handles building an Android notification that represents a new game or
	 * a new move.
	 *
	 * @param builder
	 * A partially built notification Builder object.
	 *
	 * @param notification
	 * The bundled up notification data as received from this class's incoming
	 * Intent object.
	 */
	private void handleNewGameOrNewMoveMessage(final Builder builder, final Notification notification)
	{
		final Bundle extras = new Bundle();
		extras.putSerializable(GameFragmentActivity.KEY_NOTIFICATION, notification);

		final Intent gameIntent = new Intent(this, GameFragmentActivity.class)
			.putExtras(extras)
			.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addNextIntentWithParentStack(gameIntent);
		builder.setTicker(getString(R.string.ol_x_sent_you_some_class, notification.getPerson().getName()));

		if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_GAME)
		{
			builder.setContentText(getString(R.string.new_game_from_x, notification.getPerson().getName()));
		}
		else if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_NEW_MOVE)
		{
			builder.setContentText(getString(R.string.new_move_from_x, notification.getPerson().getName()));
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
	 * @param notification
	 * The bundled up notification data as received from this class's incoming
	 * Intent object.
	 */
	private void handleWinOrLoseMessage(final Builder builder, final Notification notification)
	{
		final Bundle extras = new Bundle();
		extras.putSerializable(GameOverActivity.KEY_NOTIFICATION, notification);

		final Intent gameOverIntent = new Intent(this, GameOverActivity.class)
			.putExtras(extras);

		final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addNextIntentWithParentStack(gameOverIntent);
		builder.setTicker(getString(R.string.game_over, notification.getPerson().getName()));

		if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE)
		{
			builder.setContentText(getString(R.string.you_lost_the_game_with_x, notification.getPerson().getName()));
		}
		else if (notification.getMessageType() == Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN)
		{
			builder.setContentText(getString(R.string.you_won_the_game_with_x, notification.getPerson().getName()));
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

		if (Utilities.checkIfSettingIsEnabled(this, R.string.settings_key_vibrate, false))
		{
			final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(GCM_NOTIFICATION_VIBRATION_DURATION);
		}
	}


	private LinkedList<Notification> saveCurrentNotificationAndGrabExisting(final Notification notification)
	{
		final SharedPreferences sPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

		try
		{
			final JSONObject notificationJSON = notification.makeJSON();
			final String notificationString = notificationJSON.toString();
			final String notificationKey = String.valueOf(System.nanoTime());

			final SharedPreferences.Editor editor = sPreferences.edit();
			editor.putString(notificationKey, notificationString);
			editor.commit();
		}
		catch (final JSONException e)
		{
			Log.w(LOG_TAG, "JSONException when trying to cache this notification's data!", e);
		}

		final LinkedList<Notification> notifications = new LinkedList<Notification>();

		try
		{
			@SuppressWarnings("unchecked")
			final Map<String, String> map = (Map<String, String>) sPreferences.getAll();

			if (map != null && !map.isEmpty())
			{
				final Set<String> set = map.keySet();

				for (final String id : set)
				{
					final String notificationString = map.get(id);
					final JSONObject notificationJSON = new JSONObject(notificationString);
					final Notification newNotification = new Notification(id, notificationJSON);
					notifications.add(newNotification);
				}

				Collections.sort(notifications, this);

				while (notifications.size() > GCM_MAX_SIMULTANEOUS_NOTIFICATIONS)
				{
					notifications.removeLast();
				}
			}
		}
		catch (final JSONException e)
		{
			Log.w(LOG_TAG, "JSONException occurred when grabbing existing notifications!", e);
			notifications.clear();
		}

		return notifications;
	}




	/**
	 * Clears all cached notification data and rids the Android status bar of
	 * any currently showing notifications.
	 *
	 * @param context
	 * The Context of the Activity or Fragment that you're calling this method
	 * from.
	 */
	public static void clearNotifications(final Context context)
	{
		((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).cancelAll();
		final SharedPreferences sPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sPreferences.edit();
		editor.clear();
		editor.commit();
	}




	@Override
	public int compare(final Notification curly, final Notification larry)
	{
		return (int) (curly.getTime() - larry.getTime());
	}




}
