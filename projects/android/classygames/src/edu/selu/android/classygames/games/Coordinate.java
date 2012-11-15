package edu.selu.android.classygames.games;


public class Coordinate
{


	private byte x;
	private byte y;


	public Coordinate()
	{
		x = 0;
		y = 0;
	}


	public Coordinate(final byte x, final byte y)
	{
		this.x = x;
		this.y = y;
	}


	public byte getX()
	{
		return x;
	}


	void setX(final byte x)
	{
		this.x = x;
	}


	public byte getY()
	{
		return y;
	}


	void setY(final byte y)
	{
		this.y = y;
	}


	void set(final byte x, final byte y)
	{
		this.x = x;
		this.y = y;
	}


}
