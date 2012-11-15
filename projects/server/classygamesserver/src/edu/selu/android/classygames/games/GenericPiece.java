package edu.selu.android.classygames.games;


public abstract class GenericPiece
{


	protected final static byte TEAM_NULL = -1;
	protected final static byte TYPE_NULL = -1;

	protected byte team;
	protected byte type;


	protected GenericPiece()
	{
		team = TEAM_NULL;
		type = TYPE_NULL;
	}


}
