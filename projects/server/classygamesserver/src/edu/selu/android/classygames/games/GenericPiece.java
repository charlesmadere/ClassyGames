package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected byte type;
	protected Coordinate coordinate;


	protected GenericPiece()
	{
		coordinate = new Coordinate();
		type = 0;
	}


	protected GenericPiece(final Coordinate coordinate, final byte type)
	{
		this.coordinate = coordinate;
		this.type = type;
	}


	Coordinate getPosition()
	{
		return coordinate;
	}


	byte getType()
	{
		return type;
	}


}
