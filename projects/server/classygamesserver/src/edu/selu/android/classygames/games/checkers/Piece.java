package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	private final static byte TYPE_NORMAL = 0;
	private final static byte TYPE_KING = 1;


	public Piece()
	{
		super();
	}


	public Piece(final Coordinate coordinate)
	{
		super(coordinate, TYPE_NORMAL);
	}


	boolean isTypeNormal()
	{
		return type == TYPE_NORMAL;
	}


	boolean isTypeKing()
	{
		return type == TYPE_KING;
	}


	void setToKing()
	{
		type = TYPE_KING;
	}


}
