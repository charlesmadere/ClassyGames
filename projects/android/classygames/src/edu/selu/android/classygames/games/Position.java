package edu.selu.android.classygames.games;


/**
 * Represents a single position, or spot, on the game board.
 */
public class Position
{


	/**
	 * The GenericPiece object in this Position on the game board. If this
	 * position on the game board does not have any piece on it then this
	 * variable will be null.
	 */
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
		return piece != null && piece.isTypeNull();
	}


	/**
	 * Removes the GenericPiece object from this Position.
	 */
	public void removePiece()
	{
		piece = null;
	}


	/**
	 * Assigns a GenericPiece object to this Position.
	 * 
	 * @param piece
	 * The Piece object to assign to this Position.
	 */
	public void setPiece(final GenericPiece piece)
	{
		this.piece = piece;
	}


	/**
	 * Returns this GenericPiece object's piece.
	 * 
	 * @return
	 * The Piece object belonging to this Position.
	 */
	public GenericPiece getPiece()
	{
		return piece;
	}


}
