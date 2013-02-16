package edu.selu.android.classygames.games;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A generic Board class. All games need to have their own Board class that
 * extends from this one.
 */
public abstract class GenericBoard
{


	/**
	 * How many positions the board has horizontally. This can be thought of as
	 * the board's X limit.
	 */
	private byte lengthHorizontal;


	/**
	 * How many positions the board has vertically. This can be thought of as
	 * the board's Y limit.
	 */
	private byte lengthVertical;


	/**
	 * This board's positions. This is a two dimensional array that should be
	 * accessed as [X][Y]. So a position on the board that is (5, 3) - (X = 5
	 * and Y = 3), would be [5][3].
	 */
	protected Position[][] positions;




	/**
	 * Creates the Board object. Initializes all of the board's positions.
	 * 
	 * @param lengthHorizontal
	 * The <strong>X length</strong> of the game board.
	 * 
	 * @param lengthVertical
	 * The <strong>Y length</strong> of the game board.
	 */
	protected GenericBoard(final byte lengthHorizontal, final byte lengthVertical)
	{
		this.lengthHorizontal = lengthHorizontal;
		this.lengthVertical = lengthVertical;

		initializePositions();
	}


	/**
	 * Initializes all of this board's positions.
	 */
	private void initializePositions()
	{
		positions = new Position[lengthHorizontal][lengthVertical];

		for (byte x = 0; x < lengthHorizontal; ++x)
		{
			for (byte y = 0; y < lengthVertical; ++y)
			{
				positions[x][y] = new Position(x, y);
			}
		}
	}


	/**
	 * @return
	 * Returns the number of positions that the board has horizontally.
	 */
	public byte getLengthHorizontal()
	{
		return lengthHorizontal;
	}


	/**
	 * @return
	 * Returns the number of positions that the board has vertically.
	 */
	public byte getLengthVertical()
	{
		return lengthVertical;
	}


	/**
	 * Returns a specific Position on the Board. Think of X and Y as an ordered
	 * pair. Note that <strong>bound checking is not performed</strong>, so if
	 * you do something stupid you could get an ArrayIndexOutOfBoundsException.
	 * 
	 * @param x
	 * The X coordinate for the Position that you want.
	 * 
	 * @param y
	 * The Y coordinate for the Position that you want.
	 * 
	 * @return
	 * The Position as specified by the X and Y parameters.
	 */
	public Position getPosition(final byte x, final byte y)
	{
		return positions[x][y];
	}


	/**
	 * Returns a specific Position on the Board. Think of X and Y as an ordered
	 * pair. Note that <strong>bound checking is not performed</strong>, so if
	 * you do something stupid you could get an ArrayIndexOutOfBoundsException.
	 * 
	 * @param x
	 * The X coordinate for the Position that you want.
	 * 
	 * @param y
	 * The Y coordinate for the Position that you want.
	 * 
	 * @return
	 * The Position as specified by the X and Y parameters.
	 */
	public Position getPosition(final int x, final int y)
	{
		return getPosition((byte) x, (byte) y);
	}


	/**
	 * Returns a specific Position on the Board. Note that <strong>bound
	 * checking is not performed</strong>, so if you do something stupid you
	 * could get an ArrayIndexOutOfBoundsException.
	 * 
	 * @param coordinate
	 * The single Coordinate object containing the X and Y positions.
	 * 
	 * @return
	 * The Position as specified by the Coordinate's X and Y positions.
	 */
	public Position getPosition(final Coordinate coordinate)
	{
		return getPosition(coordinate.getX(), coordinate.getY());
	}


	/**
	 * Checks to see if a given position is actually located on a real,
	 * existing, position on the board.
	 * 
	 * @param x
	 * The X position to be checked.
	 * 
	 * @param y
	 * The Y position to be checked.
	 * 
	 * @return
	 * True if the X, Y position passed in does exist.
	 */
	public boolean isPositionValid(final byte x, final byte y)
	{
		return x >= 0 && x < lengthHorizontal && y >= 0 && y < lengthVertical;
	}


	/**
	 * Checks to see if a given position is actually located on a real,
	 * existing, position on the board.
	 * 
	 * @param x
	 * The X position to be checked.
	 * 
	 * @param y
	 * The Y position to be checked.
	 * 
	 * @return
	 * True if the X, Y position passed in does exist.
	 */
	public boolean isPositionValid(final int x, final int y)
	{
		return isPositionValid((byte) x, (byte) y);
	}


	/**
	 * Checks to see if a given position is actually located on a real,
	 * existing, position on the board.
	 * 
	 * @param coordinate
	 * The X and Y positions to be checked.
	 * 
	 * @return
	 * True if the X, Y position passed in does exist.
	 */
	public boolean isPositionValid(final Coordinate coordinate)
	{
		return isPositionValid(coordinate.getX(), coordinate.getY());
	}


	/**
	 * Creates a JSON representation of this GenericBoard object. The
	 * JSONObject that is created here <strong>is not stored</strong>, so
	 * please be sure to only run this method when you really need to okay? :)
	 * 
	 * @return
	 * Returns a JSONObject that represents this GenericBoard object.
	 * 
	 * @throws JSONException
	 * If a glitch or something happened while trying to create this JSONObject
	 * then a JSONException will be thrown.
	 */
	public JSONObject makeJSON() throws JSONException
	{
		final JSONObject teams = new JSONObject();
		teams.put("teams", makeJSONTeams());

		final JSONObject board = new JSONObject();
		board.put("board", teams);

		return board;
	}


	/**
	 * Creates a JSONArray of both teams (the player's and the opponent's) on
	 * the board. This JSONArray is actually a JSONArray of two other
	 * JSONArray's: one is the player's pieces and the other is the opponent's
	 * pieces.
	 * 
	 * @return
	 * Returns a JSONArray that contains two other JSONArrays: one is the
	 * player's pieces and the other is the opponent's pieces.
	 * 
	 * @throws JSONException
	 * If a glitch or something happened while trying to create this JSON
	 * String then a JSONException will be thrown.
	 */
	private JSONArray makeJSONTeams() throws JSONException
	{
		final JSONArray teamPlayer = makeJSONTeam(GenericPiece.TEAM_PLAYER);
		final JSONArray teamOpponent = makeJSONTeam(GenericPiece.TEAM_OPPONENT);

		final JSONArray teams = new JSONArray();
		teams.put(teamPlayer);
		teams.put(teamOpponent);

		return teams;
	}


	/**
	 * Creates a JSONArray of one team's pieces. The team to make the pieces
	 * for is specified by the whichTeam parameter.
	 * 
	 * @param whichTeam
	 * Which team do you want to make a JSONArray for? You should use one of
	 * the GenericPiece class's TEAM_* public static members for this
	 * parameter.
	 * 
	 * @return
	 * Returns a JSONArray that contains all of the given team's pieces. This
	 * has the possibility of being an empty JSONArray.
	 * 
	 * @throws JSONException
	 * If a glitch or something happened while trying to create this JSON
	 * String then a JSONException will be thrown.
	 */
	private JSONArray makeJSONTeam(final byte whichTeam) throws JSONException
	{
		final JSONArray team = new JSONArray();

		for (byte x = 0; x < lengthHorizontal; ++x)
		{
			for (byte y = 0; y < lengthVertical; ++y)
			{
				final Position position = getPosition(x, y);

				if (position.hasPiece() && position.getPiece().isTeam(whichTeam))
				{
					team.put(position.makeJSON());
				}
			}
		}

		return team;
	}




	/**
	 * Makes this GenericBoard object a default board. This should only be used
	 * for a brand new game. If a game is being resumed then this should not be
	 * used. This should probably be run (in the case that it needs to be)
	 * immediately after the object is constructed.
	 */
	protected abstract void makeDefaultBoard();


}
