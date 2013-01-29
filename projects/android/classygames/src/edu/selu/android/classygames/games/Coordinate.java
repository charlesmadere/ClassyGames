package edu.selu.android.classygames.games;


public class Coordinate
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


	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}


}
