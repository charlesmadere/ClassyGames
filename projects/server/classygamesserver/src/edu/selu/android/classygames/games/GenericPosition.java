package edu.selu.android.classygames.games;


public abstract class GenericPosition
{


	protected final static byte PIECE_NONE = 0;
	protected byte hasPiece;
	protected Coordinate coordinate;


	protected GenericPosition()
	{
		coordinate = new Coordinate();
		hasPiece = PIECE_NONE;
	}


	protected GenericPosition(final Coordinate coordinate)
	{
		this.coordinate = coordinate;
	}


	Coordinate getCoordinate()
	{
		return coordinate;
	}


	boolean hasNoPiece()
	{
		return hasPiece == PIECE_NONE;
	}


}
