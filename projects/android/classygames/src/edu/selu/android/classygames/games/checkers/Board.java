package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericBoard;


public class Board extends GenericBoard
{


	public final static byte LENGTH_HORIZONTAL = 8;
	public final static byte LENGTH_VERTICAL = 8;


	public Board()
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL);
	}


	@Override
	public boolean isPositionValid(final byte x, final byte y)
	{
		return x >= 0 && x < LENGTH_HORIZONTAL && y >= 0 && y < LENGTH_VERTICAL;
	}


	@Override
	public boolean isPositionValid(final int x, final int y)
	{
		return isPositionValid((byte) x, (byte) y);
	}


	@Override
	public boolean isPositionValid(final Coordinate coordinate)
	{
		return isPositionValid((byte) coordinate.getX(), (byte) coordinate.getY());
	}


}
