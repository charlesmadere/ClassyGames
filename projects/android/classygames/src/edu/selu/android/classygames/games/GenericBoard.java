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


	public Position getPosition(final int x, final int y)
	{
		return getPosition((byte) x, (byte) y);
	}


	public Position getPosition(final Coordinate coordinate)
	{
		return getPosition((byte) coordinate.getX(), (byte) coordinate.getY());
	}


}
