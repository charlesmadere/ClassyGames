package edu.selu.android.classygames.games;


import java.util.ArrayList;


public abstract class GenericTeam
{


	protected ArrayList<GenericPiece> pieces;

	protected int whichTeam;


	protected GenericTeam()
	{
		pieces = new ArrayList<GenericPiece>();
		whichTeam = 0;
	}


	protected GenericTeam(final int whichTeam)
	{
		this.whichTeam = whichTeam;
	}


	public void run()
	{
		for (GenericPiece piece : pieces)
		{
			piece.run();
		}
	}


	int getRemainingPieces()
	{
		return pieces.size();
	}


}
