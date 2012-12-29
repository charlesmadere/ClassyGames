package edu.selu.android.classygames.data;


public class Game
{


	private boolean turn;
	public final static boolean TURN_THEIRS = false;
	public final static boolean TURN_YOURS = true;

	private boolean type;
	public final static boolean TYPE_GAME = false;
	public final static boolean TYPE_SEPARATOR = true;

	private byte whichGame;
	public final static byte WHICH_GAME_CHECKERS = 0;
	public final static byte WHICH_GAME_CHESS = 1;


	/**
	 * The Unix Epoch as downloaded from the server.
	 */
	private long timestamp;


	/**
	 * The person to show in the Games List as your opponent.
	 */
	private Person person;


	/**
	 * The ID of this Game object as downloaded from the server.
	 */
	private String id;


	/**
	 * A human readable version of the Unix Epoch.
	 */
	private String timestampFormatted;


	/**
	 * Creates a Game object.
	 * 
	 * @param timestamp
	 * The timestamp as received from the Classy Games server. This should be the
	 * system epoch. What is the system epoch?
	 * https://en.wikipedia.org/wiki/Unix_epoch
	 * 
	 * @param person
	 * The opposing player. If I am Charles Madere and my opponent is Geonathan Sena,
	 * then this Person object will be for Geonathan Sena.
	 * 
	 * @param id
	 * The ID as received from the Classy Games server. This should be a rather long
	 * String that resembles a hash.
	 * 
	 * @param turn
	 * Who's turn is it? Use one of the TURN_* variables as defined in this class
	 * for this parameter. There are only two choices, <strong>TURN_THEIRS</strong>
	 * or <strong>TURN_YOURS</strong>.
	 */
	public Game(final long timestamp, final Person person, final String id, final boolean turn)
	{
		this.turn = turn;
		type = TYPE_GAME;
		whichGame = WHICH_GAME_CHECKERS;
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
		timestamp = (System.currentTimeMillis() / 1000) + 7200;
	}


	public String getId()
	{
		return id;
	}


	public Person getPerson()
	{
		return person;
	}


	public long getTimestamp()
	{
		return timestamp;
	}


	public String getTimestampFormatted()
	{
		if (timestampFormatted == null || timestampFormatted.isEmpty())
		// check to see if we've already created a formatted timestamp String
		// for this game object
		{
			// find out the between the time NOW versus the time of this game's
			// last move
			final long timeDifference = (System.currentTimeMillis() / 1000) - timestamp;

			// calculate the number of weeks in the difference between the two times
			long timeAgo = timeDifference / 604800;

			if (timeAgo >= 1)
			{
				if (timeAgo == 1)
				{
					timestampFormatted = "1 week ago";
				}
				else if (timeAgo == 2)
				{
					timestampFormatted = "2 weeks ago";
				}
				else
				{
					timestampFormatted = "more than 2 weeks ago";
				}
			}
			else
			{
				// calculate the number of days in the difference between the
				// two times
				timeAgo = timeDifference / 86400;

				if (timeAgo >= 1)
				{
					if (timeAgo == 1)
					{
						timestampFormatted = "1 day ago";
					}
					else if (timeAgo >= 2 && timeAgo <= 5)
					{
						timestampFormatted = timeAgo + " days ago";
					}
					else
					{
						timestampFormatted = "almost a week ago";
					}
				}
				else
				{
					// calculate the number of hours in the difference
					// between the two times
					timeAgo = timeDifference / 3600;

					if (timeAgo >= 1)
					{
						if (timeAgo == 1)
						{
							timestampFormatted = "1 hour ago";
						}
						else if (timeAgo >= 2 && timeAgo <= 12)
						{
							timestampFormatted = timeAgo + " hours ago";
						}
						else if (timeAgo > 12 && timeAgo <= 18)
						{
							timestampFormatted = "about half a day ago";
						}
						else
						{
							timestampFormatted = "almost a day ago";
						}
					}
					else
					{
						// calculate the number of minutes in the
						// difference between the two times
						timeAgo = timeDifference / 60;

						if (timeAgo >= 1)
						{
							if (timeAgo == 1)
							{
								timestampFormatted = "1 minute ago";
							}
							else if (timeAgo >= 2 && timeAgo <= 45)
							{
								timestampFormatted = timeAgo + " minutes ago";
							}
							else
							{
								timestampFormatted = "almost an hour ago";
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


	public byte getWhichGame()
	{
		return whichGame;
	}


}
