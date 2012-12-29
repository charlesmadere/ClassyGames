package edu.selu.android.classygames.games.chess;


import edu.selu.android.classygames.games.GenericBoard;


public class Board extends GenericBoard
{


	public final static byte LENGTH_HORIZONTAL = 8;
	public final static byte LENGTH_VERTICAL = 8;


	public Board()
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL);
	}


}
