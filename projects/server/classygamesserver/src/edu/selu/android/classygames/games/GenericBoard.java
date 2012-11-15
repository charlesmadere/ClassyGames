package edu.selu.android.classygames.games;


public abstract class GenericBoard
{

	
	protected GenericPosition positions[][];


	protected GenericPosition getPosition(final byte x, final byte y)
	{
		return positions[x][y];
	}


	public abstract boolean checkValidity();


	public abstract boolean checkValidity(final String boardJSONData);


}
