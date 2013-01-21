package edu.selu.android.classygames.data;


import edu.selu.android.classygames.R;
import android.content.Context;


/**
 * Class representing a single Game. This is most obviously represented when
 * seen in the app's Games List.
 */
public class Game
{


	/**
	 * Boolean representing who's turn it is. It can either be the current
	 * user's turn (TURN_YOURS => true), or the opponent's turn (TURN_THEIRS =>
	 * false).
	 */
	private boolean turn;
	public final static boolean TURN_THEIRS = false;
	public final static boolean TURN_YOURS = true;


	/**
	 * Boolean representing what type of game this is. This is not to be
	 * confused with the whichGame byte variable. Having this variable is
	 * basically a hack, as Android only allows one object type in its
	 * ListView layouts. If this is set to TYPE_SEPARATOR (true), then instead
	 * of drawing a game to the Android ListView, we draw a separator. A
	 * separator tells the user that all game's below this separator are a
	 * certain person's turn.
	 */
	private boolean type;
	public final static boolean TYPE_GAME = false;
	public final static boolean TYPE_SEPARATOR = true;


	/**
	 * Byte representing which game this is. It could be checkers, chess...
	 */
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
	 * The ID of this Game object as downloaded from the server. This is a
	 * unique hash.
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


	/**
	 * @return
	 * This Game object's ID. This is a unique hash.
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @return
	 * The person that the current Android device's user is playing against.
	 */
	public Person getPerson()
	{
		return person;
	}


	/**
	 * Returns the raw Unix Epoch. If your purpose is for something that a human is
	 * going to want to read, then you should use the getTimestampFormatted()
	 * method instead of this one.
	 * 
	 * @return
	 * The raw Unix Epoch.
	 */
	public long getTimestamp()
	{
		return timestamp;
	}


	/**
	 * Reads the Unix Epoch as stored for this Game object and compares it to
	 * the current Android system's Unix Epoch. Generates a human readable
	 * version of the difference between those two times.
	 * 
	 * @return
	 * A human readable version of the Unix Epoch.
	 */
	public String getTimestampFormatted(final Context context)
	{
		if (timestampFormatted == null || timestampFormatted.isEmpty())
		// Check to see if we've already created a formatted timestamp String
		// for this game object. If we've already created a formatted timestamp
		// String, then we can just skip the whole algorithm below and return
		// the existing String.
		{
			// find out the between the time NOW versus the time of this game's
			// last move
			final long timeDifference = (System.currentTimeMillis() / 1000) - timestamp;

			// calculate the number of WEEKS in the difference between the two
			// times
			long timeAgo = timeDifference / 604800;

			if (timeAgo >= 1)
			{
				if (timeAgo == 1)
				{
					timestampFormatted = context.getString(R.string.game_timestamp_1_week_ago);
				}
				else if (timeAgo == 2)
				{
					timestampFormatted = context.getString(R.string.game_timestamp_2_weeks_ago);
				}
				else
				{
					timestampFormatted = context.getString(R.string.game_timestamp_more_than_2_weeks_ago);
				}
			}
			else
			{
				// calculate the number of DAYS in the difference between the
				// two times
				timeAgo = timeDifference / 86400;

				if (timeAgo >= 1)
				{
					if (timeAgo == 1)
					{
						timestampFormatted = context.getString(R.string.game_timestamp_1_day_ago);
					}
					else if (timeAgo >= 2 && timeAgo <= 5)
					{
						timestampFormatted = context.getString(R.string.game_timestamp_x_days_ago, timeAgo);
					}
					else
					{
						timestampFormatted = context.getString(R.string.game_timestamp_almost_a_week_ago);
					}
				}
				else
				{
					// calculate the number of HOURS in the difference between
					// the two times
					timeAgo = timeDifference / 3600;

					if (timeAgo >= 1)
					{
						if (timeAgo == 1)
						{
							timestampFormatted = context.getString(R.string.game_timestamp_1_hour_ago);
						}
						else if (timeAgo >= 2 && timeAgo <= 12)
						{
							timestampFormatted = context.getString(R.string.game_timestamp_x_hours_ago, timeAgo);
						}
						else if (timeAgo > 12 && timeAgo <= 18)
						{
							timestampFormatted = context.getString(R.string.game_timestamp_about_half_a_day_ago);
						}
						else
						{
							timestampFormatted = context.getString(R.string.game_timestamp_almost_a_day_ago);
						}
					}
					else
					{
						// calculate the number of MINUTES in the difference
						// between the two times
						timeAgo = timeDifference / 60;

						if (timeAgo >= 1)
						{
							if (timeAgo == 1)
							{
								timestampFormatted = context.getString(R.string.game_timestamp_1_minute_ago);
							}
							else if (timeAgo >= 2 && timeAgo <= 45)
							{
								timestampFormatted = context.getString(R.string.game_timestamp_x_minutes_ago, timeAgo);
							}
							else
							{
								timestampFormatted = context.getString(R.string.game_timestamp_almost_an_hour_ago);
							}
						}
						else
						{
							timestampFormatted = context.getString(R.string.game_timestamp_just_now);
						}
					}
				}
			}
		}

		return timestampFormatted;
	}


	/**
	 * When using this method you're probably going to want to compare the
	 * value returned against some of this class's public data members, mainly
	 * the WHICH_GAME_* bytes. So that could be WHICH_GAME_CHECKERS,
	 * WHICH_GAME_CHESS...
	 * 
	 * @return
	 * A byte that represents which game this Game object represents. Could be
	 * Checkers, Chess...
	 */
	public byte getWhichGame()
	{
		return whichGame;
	}


	/**
	 * Checks to see if this Game object's turn is the opponent player's turn.
	 * 
	 * @return
	 * Returns true if this Game object's turn is the opponent player's turn.
	 */
	public boolean isTurnTheirs()
	{
		return turn == TURN_THEIRS;
	}


	/**
	 * Checks to see if this Game object's turn is the current player's turn.
	 * 
	 * @return
	 * Returns true if this Game object's turn is the current player's turn.
	 */
	public boolean isTurnYours()
	{
		return turn == TURN_YOURS;
	}


	/**
	 * Checks to see if this Game object's turn is the opponent player's turn.
	 * 
	 * @return
	 * Returns true if this Game object's turn is the opponent player's turn.
	 */
	public boolean isTypeGame()
	{
		return type == TYPE_GAME;
	}


}
