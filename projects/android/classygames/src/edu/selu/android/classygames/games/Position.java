package edu.selu.android.classygames.games;


public class Position
{


	private GenericPiece piece;


	public Position()
	{

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


	public void setPiece(final GenericPiece piece)
	{
		this.piece = piece;
	}


	public GenericPiece getPiece()
	{
		return piece;
	}


}
