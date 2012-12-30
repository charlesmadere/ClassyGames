package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static byte TYPE_NORMAL = 1;
	public final static byte TYPE_KING = 2;


	/**
	 * Creates a Piece object.
	 * 
	 * @param team
	 * What team is this Piece on? Use one of this class's public members for
	 * this parameter. So that'd be either TEAM_OPPONENT or TEAM_PLAYER.
	 * 
	 * @param type
	 * What type of Piece is this? Use one of this class's public members for
	 * this parameter. So that'd be either TYPE_NORMAL or TYPE_KING.
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
	 * this parameter. So that'd be either TYPE_NORMAL or TYPE_KING.
	 */
	public Piece(final byte team, final int type)
	{
		super(team, (byte) type);
	}


	/**
	 * Crowns a Piece object. This Piece object is now a King.
	 */
	public void ascendToKing()
	{
		type = TYPE_KING;
	}


	@Override
	protected boolean checkIfTypeIsValid(final byte type)
	{
		switch (type)
		{
			case TYPE_NORMAL:
			case TYPE_KING:
				return true;

			default:
				return false;
		}
	}


	public boolean isTypeNormal()
	{
		return type == TYPE_NORMAL;
	}


	public boolean isTypeKing()
	{
		return type == TYPE_KING;
	}


}
