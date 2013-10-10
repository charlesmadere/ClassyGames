package com.charlesmadere.android.classygames.models.games;


public final class Coordinate
{


	/**
	 * This Coordinate object's <strong>X</strong> position.
	 */
	private byte x;


	/**
	 * This Coordinate object's <strong>Y</strong> position.
	 */
	private byte y;




	/**
	 * Creates a Coordinate object. Think of this object as an ordered pair. So
	 * it should look like this: (x, y). If x were to be 5 and y were to be 7,
	 * then the ordered pair would be this: (5, 7).
	 * 
	 * @param x
	 * The X value for this coordinate.
	 * 
	 * @param y
	 * The Y value for this coordinate.
	 */
	public Coordinate(final byte x, final byte y)
	{
		this.x = x;
		this.y = y;
	}


	/**
	 * Creates a Coordinate object. Think of this object as an ordered pair. So
	 * it should look like this: (x, y). If x were to be 5 and y were to be 7,
	 * then the ordered pair would be this: (5, 7).
	 * 
	 * @param x
	 * The X value for this coordinate.
	 * 
	 * @param y
	 * The Y value for this coordinate.
	 */
	public Coordinate(final int x, final int y)
	{
		this((byte) x, (byte) y);
	}


	/**
	 * Returns this Coordinate object's <strong>X</strong> position.
	 * 
	 * @return
	 * This Coordinate object's <strong>X</strong> position.
	 */
	public byte getX()
	{
		return x;
	}


	/**
	 * Returns this Coordinate object's <strong>Y</strong> position.
	 * 
	 * @return
	 * This Coordinate object's <strong>Y</strong> position.
	 */
	public byte getY()
	{
		return y;
	}


	/**
	 * Checks to see if this Coordinate's X and Y values are either both even
	 * (as in X % 2 == 0 and Y % 2 == 0) <strong>or</strong> they're both odd
	 * (as in Y % 2 == 1 and Y % 2 == 1). If X is 3 and Y is 2, then this will
	 * return false. If X is 3 and Y is 3, this will return true. If X is 4 and
	 * Y is 2, then this will return true.
	 * 
	 * @return
	 * Returns true if either both X and Y are even numbers or if both X and Y
	 * are odd.
	 */
	public boolean areBothEitherEvenOrOdd()
	{
		return (x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1);
	}




	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}


}
