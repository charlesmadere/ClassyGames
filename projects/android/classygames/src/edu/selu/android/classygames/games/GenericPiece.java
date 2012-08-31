package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected Coordinate position;
	protected int type;


	protected GenericPiece(final Coordinate position, final int type)
	{
		this.position = position;
		this.type = type;
	}


	public abstract void draw();


	Coordinate getPosition()
	{
		return position;
	}


	void setPosition(final Coordinate position)
	{
		this.position = position;
	}


	int getType()
	{
		return type;
	}


}
