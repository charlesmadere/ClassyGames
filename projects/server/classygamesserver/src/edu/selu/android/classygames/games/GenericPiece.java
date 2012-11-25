package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected final static byte TEAM_NULL = 0;
	protected final static byte TYPE_NULL = 0;

	protected byte team;
	protected byte type;


	protected GenericPiece()
	{
		team = TEAM_NULL;
		type = TYPE_NULL;
	}
	
	
	public byte getTeam()
	{
		return team;
	}
	
	public abstract boolean isTypeNormal();
	
	
}
