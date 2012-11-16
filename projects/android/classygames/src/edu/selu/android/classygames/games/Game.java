package edu.selu.android.classygames.games;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.selu.android.classygames.data.Person;


public class Game
{


	private boolean turn;
	public final static boolean TURN_THEIRS = false;
	public final static boolean TURN_YOURS = true;

	private boolean type;
	public final static boolean TYPE_GAME = false;
	public final static boolean TYPE_SEPARATOR = true;

	private long timestamp;
	private Person person;
	private SimpleDateFormat lastMoveTime;
	private String id;
	private final static String LAST_MOVE_TIME_FORMAT = "MMMM dd, yyyy hh:mm a";


	public Game()
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		timestamp = 0;
		person = new Person();
		id = new String();
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final Person person)
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		timestamp = 0;
		this.person = person;
		id = new String();
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final long timestamp, final Person person, final String id)
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		this.timestamp = timestamp;
		this.person = person;
		this.id = id;
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	public Game(final long timestamp, final Person person, final String id, final boolean turn)
	{
		this.turn = turn;
		type = TYPE_GAME;
		this.timestamp = timestamp;
		this.person = person;
		this.id = id;
		lastMoveTime = new SimpleDateFormat(LAST_MOVE_TIME_FORMAT, Locale.US);
	}


	/**
	 * Use this constructor for creating a separator in the games list. Use one of the
	 * constants defined in this class for both of these parameters.
	 * 
	 * @param turn
	 * Game.TURN_YOURS or Game.TURN_THEIRS
	 * 
	 * @param type
	 * Game.TYPE_GAME or Game.TYPE_SEPARATOR
	 */
	public Game(final boolean turn, final boolean type)
	{
		this.turn = turn;
		this.type = type;
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


	public boolean isTurnYours()
	{
		return turn == TURN_YOURS;
	}


	public boolean isTypeGame()
	{
		return type == TYPE_GAME;
	}


}
