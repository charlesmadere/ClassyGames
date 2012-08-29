package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.generics.Coordinate;


public class Position
{


	public final static int PIECE_NONE = 0;


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


}
