package edu.selu.android.classygames.games;


import java.util.ArrayList;


public abstract class GenericTeam
{


	protected ArrayList<GenericPiece> pieces;


	protected GenericTeam()
	{
		pieces = new ArrayList<GenericPiece>();
	}


	void draw()
	{
		for (GenericPiece piece : pieces)
		{
			piece.draw();
		}
	}


	int getRemainingPieces()
	{
		return pieces.size();
	}


}
