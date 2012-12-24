package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.Position;


public class Board extends GenericBoard
{


	public final static byte LENGTH_HORIZONTAL = 8;
	public final static byte LENGTH_VERTICAL = 8;


	public Board()
	{
		super();

		positions = new Position[LENGTH_HORIZONTAL][LENGTH_VERTICAL];
	}


	@Override
	protected void initializeBoard()
	{
		for (byte x = 0; x < LENGTH_HORIZONTAL; ++x)
		{
			for (byte y = 0; y < LENGTH_VERTICAL; ++y)
			{
				positions[x][y] = new Position(x, y);
			}
		}
	}


}
