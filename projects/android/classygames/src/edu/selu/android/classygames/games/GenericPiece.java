package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected byte team;
	protected final static byte TEAM_NULL = 0;
	public final static byte TEAM_OPPONENT = 1;
	public final static byte TEAM_PLAYER = 2;

	protected byte type;
	protected final static byte TYPE_NULL = 0;


	protected GenericPiece()
	{
		team = TEAM_NULL;
		type = TYPE_NULL;
	}


	protected GenericPiece(final byte team, final byte type)
	{
		this();

		if (checkIfTeamIsValid(team) && checkIfTypeIsValid(type))
		{
			this.team = team;
			this.type = type;
		}
	}


	private boolean checkIfTeamIsValid(final byte team)
	{
		switch (team)
		{
			case TEAM_PLAYER:
			case TEAM_OPPONENT:
				return true;

			default:
				return false;
		}
	}


	public boolean isTeamOpponent()
	{
		return team == TEAM_OPPONENT;
	}


	public boolean isTeamPlayer()
	{
		return team == TEAM_PLAYER;
	}


	protected abstract boolean checkIfTypeIsValid(final byte type);


}
