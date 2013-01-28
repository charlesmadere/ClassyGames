package edu.selu.android.classygames.games.chess;


import edu.selu.android.classygames.games.GenericBoard;


/**
 * Class representing a Chess board. This board is made up of a bunch of
 * positions. Chess is 8 by 8, so that's 64 positions.
 */
public class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;


	/**
	 * Creates a Chess board object.
	 */
	public Board()
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL);
	}


}
