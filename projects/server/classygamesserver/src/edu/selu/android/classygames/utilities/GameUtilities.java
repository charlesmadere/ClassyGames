package edu.selu.android.classygames.utilities;


import edu.selu.android.classygames.games.checkers.Board;


public class GameUtilities
{


	/**
	 * Compares a default board to the given board to see if a valid move has occurred.
	 * 
	 * @param board
	 * The contents of the new board as a JSON String. This should have been pulled from the device
	 * and then sent here.
	 * 
	 * @return
	 * Returns true if the arrangement of pieces on the board is valid.
	 */
	public static boolean checkBoardValidity(final String boardJSONData)
	{
		return new Board(boardJSONData).checkValidity();
	}


	/**
	 * Compares the original board to the new board to see if a valid move has occurred.
	 * 
	 * @param boardOriginal
	 * The contents of the original board as a JSON String. This should be pulled from the database
	 * and then sent here.
	 * 
	 * @param boardNew
	 * The contents of the new board as a JSON String. This should have been pulled from the device
	 * and then sent here.
	 * 
	 * @return
	 * Returns true if the arrangement of pieces on the board is valid. 
	 */
	public static boolean checkBoardValidity(final String boardJSONDataOriginal, final String boardJSONDataNew)
	{
		return new Board(boardJSONDataOriginal).checkValidity(boardJSONDataNew);
	}


}
