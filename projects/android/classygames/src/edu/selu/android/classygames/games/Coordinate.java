package edu.selu.android.classygames.games;


public class Coordinate
{


	private byte x;
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
		this.x = (byte) x;
		this.y = (byte) y;
	}


	/**
	 * Returns this Coordinate's X position.
	 * 
	 * @return
	 * This Coordinate's X position.
	 */
	public byte getX()
	{
		return x;
	}


	/**
	 * Returns this Coordinate's Y position.
	 * 
	 * @return
	 * This Coordinate's Y position.
	 */
	public byte getY()
	{
		return y;
	}


	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}


}
