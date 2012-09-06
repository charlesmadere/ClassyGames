package edu.selu.android.classygames.games.checkers;


import android.content.res.Resources;
import edu.selu.android.classygames.games.GenericGame;


public class Checkers extends GenericGame
{


	public final static int NUMBER_OF_PIECES = 12;

	// ignore these warnings for now!!
	private static Team TEAM_GREEN;
	private static Team TEAM_ORANGE;


	public Checkers()
	{
		TEAM_GREEN = new Team();
		TEAM_ORANGE = new Team();
	}


	public Checkers(final Resources resources)
	{
		TEAM_GREEN = new Team(resources, Team.TEAM_GREEN);
		TEAM_ORANGE = new Team(resources, Team.TEAM_ORANGE);
	}


	@Override
	public void run()
	{
		TEAM_GREEN.run();
		TEAM_ORANGE.run();
	}


}
