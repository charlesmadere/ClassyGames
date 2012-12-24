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


	public byte getX()
	{
		return x;
	}


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
