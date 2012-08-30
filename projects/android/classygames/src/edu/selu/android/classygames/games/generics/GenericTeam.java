package edu.selu.android.classygames.games.generics;


import java.util.ArrayList;


public class GenericTeam
{


	protected ArrayList<GenericPiece> pieces;


	protected GenericTeam()
	{
		pieces = new ArrayList<GenericPiece>();
	}


	int getRemainingPieces()
	{
		return pieces.size();
	}


}
