package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericBoard;


/**
 * Class representing a Checkers board. This board is made up of a bunch of
 * positions. Checkers is 8 by 8, so that's 64 positions.
 */
public class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;




	/**
	 * Creates a Checkers board object.
	 */
	public Board()
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL);
	}


}
