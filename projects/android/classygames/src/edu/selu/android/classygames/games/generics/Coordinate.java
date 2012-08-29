package edu.selu.android.classygames.games.generics;


public class Coordinate
{


	private int x;
	private int y;


	public Coordinate()
	{
		x = 0;
		y = 0;
	}


	public Coordinate(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}


	int getX()
	{
		return x;
	}


	void setX(final int x)
	{
		this.x = x;
	}


	int getY()
	{
		return y;
	}


	void setY(final int y)
	{
		this.y = y;
	}


	void set(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}


}
