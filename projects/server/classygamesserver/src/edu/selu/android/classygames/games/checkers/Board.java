package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericBoard;


public class Board extends GenericBoard
{


	public final static byte LENGTH_HORIZONTAL = 8;
	public final static byte LENGTH_VERTICAL = 8;


	public Board()
	{
		positions = new Position[LENGTH_HORIZONTAL][LENGTH_VERTICAL];
	}


	/**
	 * Creates a Board object from JSON data.
	 * 
	 * @param boardJSONData
	 * A String of JSON data that represents the Board object.
	 */
	public Board(final String boardJSONData)
	// TODO
	{
		this();


	}


	@Override
	public boolean checkValidity()
	// TODO
	{


		return true;
	}


	@Override
	public boolean checkValidity(final String boardJSONData)
	// TODO
	{
		final Board boardNew = new Board(boardJSONData);

		return true;
	}


}
