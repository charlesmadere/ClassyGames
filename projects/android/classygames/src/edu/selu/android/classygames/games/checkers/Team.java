package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericTeam;


public class Team extends GenericTeam
{


	public final static int TEAM_ORANGE = 0;
	public final static int TEAM_GREEN = 1;


	public Team()
	{
		super();

		for (int i = 0; i < Checkers.NUMBER_OF_PIECES; ++i)
		{
			pieces.add(new Piece());
		}
	}


	public Team(int whichTeam)
	{
		super();

		switch (whichTeam)
		// TODO write some code that will create the pieces for whichever team. that for loop
		// above is an example of what it would (maybe) kinda look like...
		{
			case TEAM_ORANGE:
				break;

			case TEAM_GREEN:
				break;
		}
	}


}
