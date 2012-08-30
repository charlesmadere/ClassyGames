package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.generics.Coordinate;
import edu.selu.android.classygames.games.generics.GenericPosition;


public class Position extends GenericPosition
{


	public final static int PIECE_NONE = 0;
	public final static int PIECE_NORMAL = 1;
	public final static int PIECE_KING = 2;


	Coordinate spot;
	int hasPiece;


	public Position()
	{
		spot = new Coordinate();
		hasPiece = PIECE_NONE;
	}


	int getHasPiece()
	{
		return hasPiece;
	}


	boolean hasNoPiece()
	{
		return hasPiece == PIECE_NONE;
	}


	boolean hasNormalPiece()
	{
		return hasPiece == PIECE_NORMAL;
	}


	boolean hasKingPiece()
	{
		return hasPiece == PIECE_KING;
	}


}
