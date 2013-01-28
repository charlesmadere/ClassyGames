package edu.selu.android.classygames.games;


/**
 * Class representing a single position, or spot, on the game board.
 */
public class Position
{


	/**
	 * The GenericPiece object in this Position on the game board. If this
	 * position on the game board does not have any piece on it then this
	 * variable will be null.
	 */
	private GenericPiece piece;




	/**
	 * Creates a Position object.
	 */
	public Position()
	{

	}


	/**
	 * Returns this Position object's GenericPiece object. Note that it's
	 * possible for this object to be null; to check for that you should use
	 * this class's hasPiece() method.
	 * 
	 * @return
	 * Returns the Piece object belonging to this Position.
	 */
	public GenericPiece getPiece()
	{
		return piece;
	}


	/**
	 * Checks and sees if this GenericPosition object has a GenericPiece.
	 * 
	 * @return
	 * Returns true if this GenericPosition object has a GenericPiece. Returns
	 * false if the GenericPiece object is either null or is of a null type.
	 */
	public boolean hasPiece()
	{
		return piece != null && !piece.isTypeNull();
	}


	/**
	 * Removes the GenericPiece object from this Position.
	 */
	public void removePiece()
	{
		piece.kill();
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


}
