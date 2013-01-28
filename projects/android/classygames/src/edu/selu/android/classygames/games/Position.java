package edu.selu.android.classygames.games;


/**
 * Class representing a single position, or spot, on the game board.
 */
public class Position
{


	/**
	 * This Position's coordinate. This is this Position object's location on
	 * the game board.
	 */
	private Coordinate coordinate;


	/**
	 * The GenericPiece object in this Position on the game board. If this
	 * position on the game board does not have any piece on it then this
	 * variable will be null.
	 */
	private GenericPiece piece;




	/**
	 * Creates a Position object.
	 * 
	 * @param x
	 * This Position object's <strong>X</strong> coordinate.
	 * 
	 * @param y
	 * This Position object's <strong>Y</strong> coordinate.
	 */
	public Position(final byte x, final byte y)
	{
		coordinate = new Coordinate(x, y);
	}


	/**
	 * This Position object's Coordinate is it's location on the game board.
	 * 
	 * @return
	 * Returns this Position object's Coordinate.
	 */
	public Coordinate getCoordinate()
	{
		return coordinate;
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
