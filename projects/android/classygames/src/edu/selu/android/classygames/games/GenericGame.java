package edu.selu.android.classygames.games;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public abstract class GenericGame
{


	protected int id;
	protected Person person;
	protected SimpleDateFormat lastMoveTime;
	private final static String lastMoveTimeFormat = "MMMM dd, yyyy hh:mm a";


	public GenericGame()
	{
		id = 0;
		person = new Person();
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final Person person)
	{
		id = 0;
		this.person = person;
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final Person person, final SimpleDateFormat lastMoveTime)
	{
		id = 0;
		this.person = person;
		this.lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final int id, final Person person)
	{
		this.id = id;
		this.person = person;
		lastMoveTime = new SimpleDateFormat(lastMoveTimeFormat, Locale.US);
	}


	public GenericGame(final int id, final Person person, final SimpleDateFormat lastMoveTime)
	{
		this.id = id;
		this.person = person;
		this.lastMoveTime = lastMoveTime;
	}


//	abstract public void run();


	public int getId()
	{
		return id;
	}


	void setId(final int id)
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
