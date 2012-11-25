package edu.selu.android.classygames.games;


public abstract class GenericBoard
{


	protected byte maxTeamSize;
	protected GenericPosition[][] positions;


	protected GenericBoard()
	{
		initializeBoard();
	}


	/**
	 * Checks the validity of the board against a completely default board.
	 * 
	 * @return
	 * True if the board is valid.
	 */
	public abstract byte checkValidity();


	/**
	 * Checks the validity of the board then checks the new board (created from the
	 * boardJSONData String) against the original board to see if it is valid.
	 * 
	 * @param boardJSONData
	 * The new board's data as a JSON String.
	 * 
	 * @return
	 * True if the boards are valid.
	 */
	public abstract byte checkValidity(final String boardJSONData);


	public abstract GenericPosition getPosition(final byte x, final byte y);


	protected abstract void initializeBoard();


}
