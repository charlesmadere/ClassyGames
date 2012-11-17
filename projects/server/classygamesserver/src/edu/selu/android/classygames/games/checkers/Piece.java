package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static byte TEAM_USER = 1;
	public final static byte TEAM_CHALLENGED = 2;
	public final static byte TYPE_NORMAL = 1;
	public final static byte TYPE_KING = 2;


	public Piece()
	{
		super();
	}


	public Piece(final byte team, final byte type)
	{
		super();

		if (checkIfTeamIsValid(team))
		{
			this.team = team;
		}

		if (checkIfTypeIsValid(type))
		{
			this.type = type;
		}
	}


	public static boolean checkIfTeamIsValid(final byte team)
	{
		switch (team)
		{
			case TEAM_USER:
			case TEAM_CHALLENGED:
				return true;

			default:
				return false;
		}
	}


	public static boolean checkIfTypeIsValid(final byte type)
	{
		switch (type)
		{
			case TYPE_NORMAL:
			case TYPE_KING:
				return true;

			default:
				return false;
		}
	}


}
