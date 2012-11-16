package edu.selu.android.classygames.games;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.selu.android.classygames.data.Person;


public class Game
{


	private long timestamp;
	private Person person;
	private SimpleDateFormat lastMoveTime;
	private String id;
	private final static String LAST_MOVE_TIME_FORMAT = "MMMM dd, yyyy hh:mm a";


	public Game()
	{
		timestamp = 0;
		person = new Person();
		id = new String();
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final Person person)
	{
		timestamp = 0;
		this.person = person;
		id = new String();
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final long timestamp, final Person person, final String id)
	{
		this.timestamp = timestamp;
		this.person = person;
		this.id = id;
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public String getId()
	{
		return id;
	}


	void setId(final String id)
	{
		this.id = id;
	}


	public String getLastMoveTime()
	{
		return lastMoveTime.format(new Date());
	}


	public Person getPerson()
	{
		return person;
	}


	void setPerson(final Person person)
	{
		this.person = person;
	}


	long getTimestamp()
	{
		return timestamp;
	}


	String getTimestampFormatted()
	{
		return "time: " + timestamp;
	}


}
