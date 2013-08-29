package com.charlesmadere.android.classygames.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public final class Notification implements Serializable
{


	private final static String KEY_GAME_ID = "gameId";
	private final static String KEY_WHICH_GAME = "whichGame";
	private final static String KEY_MESSAGE_TYPE = "messageType";
	private final static String KEY_PERSON_ID = "personId";
	private final static String KEY_PERSON_NAME = "personName";


	private long time;
	private String gameId;
	private byte whichGame;
	private byte messageType;
	private Person person;


	public Notification(final String gameId, final byte whichGame, final byte messageType, final Person person)
	{
		this.gameId = gameId;
		this.whichGame = whichGame;
		this.messageType = messageType;
		this.person = person;
	}


	public Notification(final String id, final JSONObject notificationJSON) throws JSONException
	{
		time = Long.valueOf(id);
		gameId = notificationJSON.getString(KEY_GAME_ID);
		whichGame = Integer.valueOf(notificationJSON.getInt(KEY_WHICH_GAME)).byteValue();
		messageType = Integer.valueOf(notificationJSON.getInt(KEY_MESSAGE_TYPE)).byteValue();
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


	public byte getWhichGame()
	{
		return whichGame;
	}


	public byte getMessageType()
	{
		return messageType;
	}


	public Person getPerson()
	{
		return person;
	}


	public JSONObject makeJSON() throws JSONException
	{
		final JSONObject notificationJSON = new JSONObject();
		notificationJSON.put(KEY_GAME_ID, gameId);
		notificationJSON.put(KEY_WHICH_GAME, Integer.valueOf(whichGame));
		notificationJSON.put(KEY_MESSAGE_TYPE, Integer.valueOf(messageType));
		notificationJSON.put(KEY_PERSON_ID, person.getId());
		notificationJSON.put(KEY_PERSON_NAME, person.getName());

		return notificationJSON;
	}


}
