package edu.selu.android.classygames.games;


public abstract class GenericPosition
{


	protected Coordinate coordinate;
	protected GenericPiece piece;


	protected GenericPosition()
	{
		coordinate = new Coordinate();
		piece = null;
	}


	protected GenericPosition(final Coordinate coordinate)
	{
		this.coordinate = coordinate;
		piece = null;
	}


	GenericPiece getPiece()
	{
		return piece;
	}


	/**
	 * Test and see if this Position object has a Piece associated with it.
	 * 
	 * @return
	 * True if this Position object has a Piece.
	 */
	public boolean hasPiece()
	{
		return piece != null;
	}


}
