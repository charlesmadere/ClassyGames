package com.charlesmadere.android.classygames.models;


import android.content.Context;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.server.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public final class Notification implements Serializable
{


	private final static String KEY_GAME_ID = "gameId";
	private final static String KEY_MESSAGE_TYPE = "messageType";
	private final static String KEY_WHICH_GAME = "whichGame";
	private final static String KEY_PERSON_ID = "personId";
	private final static String KEY_PERSON_NAME = "personName";


	private byte messageType;
	private byte whichGame;
	private long time;
	private Person person;
	private String gameId;


	public Notification(final String gameId, final byte whichGame, final byte messageType, final Person person)
	{
		this.gameId = gameId;
		this.whichGame = whichGame;
		this.messageType = messageType;
		this.person = person;
	}


	public Notification(final String id, final JSONObject notificationJSON) throws JSONException
	{
		time = Long.parseLong(id);
		gameId = notificationJSON.getString(KEY_GAME_ID);
		messageType = (byte) notificationJSON.getInt(KEY_MESSAGE_TYPE);
		whichGame = (byte) notificationJSON.getInt(KEY_WHICH_GAME);
		final long personId = notificationJSON.getLong(KEY_PERSON_ID);
		final String personName = notificationJSON.getString(KEY_PERSON_NAME);
		person = new Person(personId, personName);
	}


	public long getTime()
	{
		return time;
	}


	public String getGameId()
	{
		return gameId;
	}


	public byte getMessageType()
	{
		return messageType;
	}


	public String getReadableMessageType(final Context context)
	{
		final String readableMessageType;

		switch (messageType)
		{
			case Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
				readableMessageType = context.getString(R.string.you_lost);
				break;

			case Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
				readableMessageType = context.getString(R.string.you_won);
				break;

			case Server.POST_DATA_MESSAGE_TYPE_NEW_GAME:
				readableMessageType = context.getString(R.string.new_game);
				break;

			case Server.POST_DATA_MESSAGE_TYPE_NEW_MOVE:
				readableMessageType = context.getString(R.string.new_move);
				break;

			default:
				readableMessageType = context.getString(R.string.ol_x_sent_you_some_class, person.getName());
				break;
		}

		return readableMessageType;
	}


	public byte getWhichGame()
	{
		return whichGame;
	}


	public Person getPerson()
	{
		return person;
	}


	public boolean isMessageTypeGameOverLose()
	{
		return messageType == Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE;
	}


	public boolean isMessageTypeGameOverWin()
	{
		return messageType == Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN;
	}


	public boolean isMessageTypeNewGame()
	{
		return messageType == Server.POST_DATA_MESSAGE_TYPE_NEW_GAME;
	}


	public boolean isMessageTypeNewMove()
	{
		return messageType == Server.POST_DATA_MESSAGE_TYPE_NEW_MOVE;
	}


	public JSONObject makeJSON() throws JSONException
	{
		final JSONObject notificationJSON = new JSONObject();
		notificationJSON.put(KEY_GAME_ID, gameId);
		notificationJSON.put(KEY_WHICH_GAME, (int) whichGame);
		notificationJSON.put(KEY_MESSAGE_TYPE, (int) messageType);
		notificationJSON.put(KEY_PERSON_ID, person.getId());
		notificationJSON.put(KEY_PERSON_NAME, person.getName());

		return notificationJSON;
	}


}
