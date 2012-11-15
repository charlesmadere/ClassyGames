package edu.selu.android.classygames.games;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.selu.android.classygames.data.Person;


public class Game
{


	protected Person person;
	protected SimpleDateFormat lastMoveTime;
	protected String id;
	private final static String LAST_MOVE_TIME_FORMAT = "MMMM dd, yyyy hh:mm a";


	public Game()
	{
		id = new String();
		person = new Person();
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final Person person)
	{
		id = new String();
		this.person = person;
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final Person person, final SimpleDateFormat lastMoveTime)
	{
		id = new String();
		this.person = person;
		this.lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final String id, final Person person)
	{
		this.id = id;
		this.person = person;
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final String id, final Person person, final SimpleDateFormat lastMoveTime)
	{
		this.id = id;
		this.person = person;
		this.lastMoveTime = lastMoveTime;
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


}
