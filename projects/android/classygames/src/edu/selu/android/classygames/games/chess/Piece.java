package edu.selu.android.classygames.games.chess;


import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static byte TYPE_PAWN = 1;
	public final static byte TYPE_BISHOP = 2;
	public final static byte TYPE_KNIGHT = 3;
	public final static byte TYPE_ROOK = 4;
	public final static byte TYPE_QUEEN = 5;
	public final static byte TYPE_KING = 6;


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


	public boolean isTypePawn()
	{
		return type == TYPE_PAWN;
	}


	public boolean isTypeBishop()
	{
		return type == TYPE_BISHOP;
	}


	public boolean isTypeKnight()
	{
		return type == TYPE_KNIGHT;
	}


	public boolean isTypeRook()
	{
		return type == TYPE_ROOK;
	}


	public boolean isTypeQueen()
	{
		return type == TYPE_QUEEN;
	}


	public boolean isTypeKing()
	{
		return type == TYPE_KING;
	}


}
