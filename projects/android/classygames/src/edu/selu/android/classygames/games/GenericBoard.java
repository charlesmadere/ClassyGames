package edu.selu.android.classygames.games;


public abstract class GenericBoard
{


	protected Position[][] positions;


	protected GenericBoard()
	{
		initializeBoard();
	}


	public Position getPosition(final byte x, final byte y)
	{
		return positions[x][y];
	}


	protected abstract void initializeBoard();


}
