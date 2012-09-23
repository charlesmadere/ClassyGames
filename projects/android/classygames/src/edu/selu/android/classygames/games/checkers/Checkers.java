package edu.selu.android.classygames.games.checkers;


import java.text.SimpleDateFormat;

import android.content.res.Resources;
import edu.selu.android.classygames.games.GenericGame;
import edu.selu.android.classygames.games.Person;


public class Checkers extends GenericGame
{


	public final static int NUMBER_OF_PIECES = 12;

	private static Team TEAM_GREEN;
	private static Team TEAM_ORANGE;


	public Checkers()
	{
		super();

		TEAM_GREEN = new Team();
		TEAM_ORANGE = new Team();
	}


	public Checkers(final Person person)
	{
		super(person);

		TEAM_GREEN = new Team();
		TEAM_ORANGE = new Team();
	}


	public Checkers(final Person person, final SimpleDateFormat lastMoveTime)
	{
		super(person, lastMoveTime);

		TEAM_GREEN = new Team();
		TEAM_ORANGE = new Team();
	}


	public Checkers(final int id, final Person person, final Resources resources)
	{
		super(id, person);

		TEAM_GREEN = new Team(resources, Team.TEAM_GREEN);
		TEAM_ORANGE = new Team(resources, Team.TEAM_ORANGE);
	}


	public Checkers(final int id, final Person person, final SimpleDateFormat lastMoveTime, final Resources resources)
	{
		super(id, person, lastMoveTime);

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
