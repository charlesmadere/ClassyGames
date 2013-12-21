package com.charlesmadere.android.classygames.models.games.chess;


import com.charlesmadere.android.classygames.models.games.GenericPiece;


/**
 * Class representing a single Chess piece.
 */
public final class Piece extends GenericPiece
{


	public final static byte TYPE_PAWN = 1;
	public final static byte TYPE_BISHOP = 2;
	public final static byte TYPE_KNIGHT = 3;
	public final static byte TYPE_ROOK = 4;
	public final static byte TYPE_QUEEN = 5;
	public final static byte TYPE_KING = 6;




	/**
	 * Creates a Piece object. As this constructor <strong>does not</strong>
	 * take a type parameter, this Piece's type will be set to the default
	 * (a pawn).
	 * 
	 * @param team
	 * What team is this Piece on? Use one of this class's public members for
	 * this parameter. So that'd be either TEAM_OPPONENT or TEAM_PLAYER.
	 */
	public Piece(final byte team)
	{
		super(team, TYPE_PAWN);
	}


	/**
	 * Creates a Piece object.
	 * 
	 * @param team
	 * What team is this Piece on? Use one of this class's public members for
	 * this parameter. So that'd be either TEAM_OPPONENT or TEAM_PLAYER.
	 * 
	 * @param type
	 * What type of Piece is this? Use one of this class's public members for
	 * this parameter. So that'd be either TYPE_PAWN or TYPE_KNIGHT or...
	 */
	public Piece(final byte team, final byte type)
	{
		super(team, type);
	}


	/**
	 * Creates a Piece object.
	 * 
	 * @param team
	 * What team is this Piece on? Use one of this class's public members for
	 * this parameter. So that'd be either TEAM_OPPONENT or TEAM_PLAYER.
	 * 
	 * @param type
	 * What type of Piece is this? Use one of this class's public members for
	 * this parameter. So that'd be either TYPE_PAWN or TYPE_KNIGHT or...
	 */
	public Piece(final byte team, final int type)
	{
		super(team, (byte) type);
	}


	/**
	 * Creates a Piece object that is a copy of the given Piece object.
	 *
	 * @param piece
	 * The Piece object to make a copy of.
	 */
	public Piece(final Piece piece)
	{
		super(piece);
	}


	/**
	 * Checks to see if this Piece is a pawn.
	 * 
	 * @return
	 * Returns true if this Piece is a pawn.
	 */
	public boolean isTypePawn()
	{
		return type == TYPE_PAWN;
	}


	/**
	 * Checks to see if this Piece is a bishop.
	 * 
	 * @return
	 * Returns true if this Piece is a bishop.
	 */
	public boolean isTypeBishop()
	{
		return type == TYPE_BISHOP;
	}


	/**
	 * Checks to see if this Piece is a knight.
	 * 
	 * @return
	 * Returns true if this Piece is a knight.
	 */
	public boolean isTypeKnight()
	{
		return type == TYPE_KNIGHT;
	}


	/**
	 * Checks to see if this Piece is a rook.
	 * 
	 * @return
	 * Returns true if this Piece is a rook.
	 */
	public boolean isTypeRook()
	{
		return type == TYPE_ROOK;
	}


	/**
	 * Checks to see if this Piece is a queen.
	 * 
	 * @return
	 * Returns true if this Piece is a queen.
	 */
	public boolean isTypeQueen()
	{
		return type == TYPE_QUEEN;
	}


	/**
	 * Checks to see if this Piece is a king.
	 * 
	 * @return
	 * Returns true if this Piece is a king.
	 */
	public boolean isTypeKing()
	{
		return type == TYPE_KING;
	}




	@Override
	protected boolean checkIfTypeIsValid(final byte type)
	{
		switch (type)
		{
			case TYPE_PAWN:
			case TYPE_BISHOP:
			case TYPE_KNIGHT:
			case TYPE_ROOK:
			case TYPE_QUEEN:
			case TYPE_KING:
				return true;

			default:
				return false;
		}
	}


	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();

		switch (team)
		{
			case TEAM_PLAYER:
				builder.append("Player");
				break;

			case TEAM_OPPONENT:
				builder.append("Opponent");
				break;
		}

		builder.append(" ");

		switch (getType())
		{
			case TYPE_PAWN:
				builder.append("Pawn");
				break;

			case TYPE_BISHOP:
				builder.append("Bishop");
				break;

			case TYPE_KNIGHT:
				builder.append("Knight");
				break;

			case TYPE_ROOK:
				builder.append("Rook");
				break;

			case TYPE_QUEEN:
				builder.append("Queen");
				break;

			case TYPE_KING:
				builder.append("King");
				break;
		}

		return builder.toString();
	}


}
