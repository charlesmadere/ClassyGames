package edu.selu.android.classygames.games;


public abstract class GenericBoard
{


	protected Position[][] positions;


	protected GenericBoard(final byte lengthHorizontal, final byte lengthVertical)
	{
		positions = new Position[lengthHorizontal][lengthVertical];
		initializeBoard(lengthHorizontal, lengthVertical);
	}


	private void initializeBoard(final byte lengthHorizontal, final byte lengthVertical)
	{
		for (byte x = 0; x < lengthHorizontal; ++x)
		{
			for (byte y = 0; y < lengthVertical; ++y)
			{
				positions[x][y] = new Position(x, y);
			}
		}
	}


	public Position getPosition(final byte x, final byte y)
	{
		return positions[x][y];
	}


}
