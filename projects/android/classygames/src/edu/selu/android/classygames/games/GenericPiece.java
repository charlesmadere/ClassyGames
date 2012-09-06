package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected Coordinate position;

	protected int id;
	protected int type;


	protected GenericPiece()
	{
		position = new Coordinate();
		id = 0;
		type = 0;
	}


	protected GenericPiece(final Coordinate position, final int id, final int type)
	{
		this.position = position;
		this.id = id;
		this.type = type;
	}


	public abstract void run();


	Coordinate getPosition()
	{
		return position;
	}


	void setPosition(final Coordinate position)
	{
		this.position = position;
	}


	int getId()
	{
		return id;
	}


	int getType()
	{
		return type;
	}


}
