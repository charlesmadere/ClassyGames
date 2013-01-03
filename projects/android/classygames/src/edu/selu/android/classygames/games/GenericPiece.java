package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	/**
	 * This variable represents which team this GenericPiece object is on.
	 */
	protected byte team;
	private final static byte TEAM_NULL = 0;
	public final static byte TEAM_PLAYER = 1;
	public final static byte TEAM_OPPONENT = 2;


	/**
	 * This variable represents which type of GenericPiece object that this is.
	 */
	protected byte type;
	private final static byte TYPE_NULL = 0;


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


	public boolean isTeam(final byte whichTeam)
	{
		return team == whichTeam;
	}


	public boolean isTeamOpponent()
	{
		return team == TEAM_OPPONENT;
	}


	public boolean isTeamPlayer()
	{
		return team == TEAM_PLAYER;
	}


	public byte getType()
	{
		return type;
	}


	/**
	 * Tests to see if the passed in type is actually a valid type.
	 * 
	 * @param type
	 * The type to test.
	 * 
	 * @return
	 * True if the given type is valid.
	 */
	protected abstract boolean checkIfTypeIsValid(final byte type);


}
