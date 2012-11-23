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
	private String id;
	private String timestampFormatted;

	public Game()
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		timestamp = 0;
		person = new Person();
		id = new String();
		
	}


	public Game(final Person person)
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		timestamp = 0;
		this.person = person;
		id = new String();
		
	}


	public Game(final long timestamp, final Person person, final String id)
	{
		turn = TURN_YOURS;
		type = TYPE_GAME;
		this.timestamp = timestamp;
		this.person = person;
		this.id = id;
	}


	public Game(final long timestamp, final Person person, final String id, final boolean turn)
	{
		this.turn = turn;
		type = TYPE_GAME;
		this.timestamp = timestamp;
		this.person = person;
		this.id = id;
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
		if (timestampFormatted == null || timestampFormatted.isEmpty())
		{
			long timeNow = System.currentTimeMillis() / 1000;
			long timeofGame = timeNow - timestamp;
			int weeksAgo = (int) (timeofGame / 604800);
			
			if (weeksAgo > 0)
			{
				switch (weeksAgo) 
				{
					case 1:
						timestampFormatted = "1 week ago";
						break;

					case 2:
						timestampFormatted = "2 weeks ago";
						break;
						
					default:
						timestampFormatted = "more than 2 weeks ago";
						break;
				}
			} 
			else
			{
				int daysAgo = (int) (timeofGame / 86400);
				
				if (daysAgo >= 1)
				{
					switch (daysAgo) 
					{
						case 1:
							timestampFormatted = "1 day ago";
							break;
							
						case 2:
							timestampFormatted = "15 days ago";
							break;
							
						default:
							timestampFormatted = " a month ago";
							break;
					}
				}
				else
				{
					int hoursAgo =(int) (timeofGame / 3600);
					if (hoursAgo >= 1)
					{
						switch (hoursAgo) 
						{
							case 1:
								timestampFormatted = "1 hour ago";
								break;

							case 2:
								timestampFormatted = "10 hours ago";
								break;
								
							default:
								timestampFormatted = " a half day ago";
								break;
						}
						
					}
					else
					{
						int minutesAgo = (int) (timeofGame / 60);
						if (minutesAgo >= 2)
						{
							switch (minutesAgo) 
							{
								case 1:
									timestampFormatted = "5 minutes ago";
									break;

								case 2:
									timestampFormatted = "30 minutes ago";
									break;
									
								default:
									timestampFormatted = "1 hour ago";
									break;
							}
							
						}
						else
						{
							timestampFormatted = "just now";
						}
					}
				}
			}
		}
		
		
		return timestampFormatted;		
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
