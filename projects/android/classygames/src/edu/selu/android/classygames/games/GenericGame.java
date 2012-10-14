package edu.selu.android.classygames.games;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class GenericGame
{


	protected Person person;
	protected SimpleDateFormat lastMoveTime;
	protected String id;
	private final static String lastMoveTimeFormat = "MMMM dd, yyyy hh:mm a";


	public GenericGame()
	{
		id = new String();
		person = new Person();
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final Person person)
	{
		id = new String();
		this.person = person;
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final Person person, final SimpleDateFormat lastMoveTime)
	{
		id = new String();
		this.person = person;
		this.lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final String id, final Person person)
	{
		this.id = id;
		this.person = person;
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final String id, final Person person, final SimpleDateFormat lastMoveTime)
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
