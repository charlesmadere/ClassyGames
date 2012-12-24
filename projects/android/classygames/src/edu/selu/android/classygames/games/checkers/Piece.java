package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	private final static byte TYPE_NORMAL = 1;
	private final static byte TYPE_KING = 2;


	public Piece(final byte team, final byte type)
	{
		super(team, type);
	}


	@Override
	public boolean checkIfTypeIsValid(final byte type)
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
