package edu.selu.android.classygames.games.checkers;


import java.util.LinkedList;


public class Team
{


	LinkedList<Piece> pieces;


	public Team()
	{

	}


	int getRemainingPieces()
	{
		return pieces.size();
	}


}
