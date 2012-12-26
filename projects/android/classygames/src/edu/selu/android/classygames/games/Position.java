package edu.selu.android.classygames.games;


public class Position
{


	protected Coordinate coordinate;
	protected GenericPiece piece;


	public Position(final byte x, final byte y)
	{
		this.coordinate = new Coordinate(x, y);
	}


	public Position(final byte x, final byte y, final GenericPiece piece)
	{
		this.coordinate = new Coordinate(x, y);
		this.piece = piece;
	}


	/**
	 * Check and see if this GenericPosition object has a GenericPiece.
	 * 
	 * @return
	 * True if this GenericPosition object has a GenericPiece.
	 */
	public boolean hasPiece()
	{
		return piece != null;
	}


	/**
	 * Removes the GenericPiece object from this Position.
	 */
	public void removePiece()
	{
		piece = null;
	}


	public GenericPiece getPiece()
	{
		return piece;
	}


}
