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
	@Deprecated
	public static boolean checkBoardValidity(final String boardJSONData)
	{
		if (boardJSONData == null || boardJSONData.isEmpty())
		{
			return false;
		}
		else
		{
			return new Board(boardJSONData).checkValidity();
		}
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
	@Deprecated
	public static boolean checkBoardValidity(final String boardJSONDataOriginal, final String boardJSONDataNew)
	{
		if (boardJSONDataOriginal == null || boardJSONDataOriginal.isEmpty()
			|| boardJSONDataNew == null || boardJSONDataNew.isEmpty())
		{
			return false;
		}
		else
		{
			return new Board(boardJSONDataOriginal).checkValidity(boardJSONDataNew);
		}
	}


	/**
	 * Compares a completely default version of the board to the new one that we just gathered
	 * from the player.
	 * 
	 * @param boardJSONData
	 * The board as just now sent to the server by the player. This was the player's move.
	 * 
	 * @return
	 * If the board is completely valid and there is no issue, then this will return the
	 * Utilities.BOARD_NEW_GAME byte. As this method should only be run on a brand new game,
	 * the only other possible return value from this method is Utilities.BOARD_INVALID.
	 */
	public static byte checkBoardValidityAndStatus(final String boardJSONData)
	{
		if (boardJSONData == null || boardJSONData.isEmpty())
		{
			return Utilities.BOARD_INVALID;
		}
		else
		{
			return Utilities.BOARD_NEW_GAME;
		}
	}


	/**
	 * Compares the version of the board that already exists in the database to the new one that
	 * we just gathered from the player.
	 * 
	 * @param boardJSONDataOriginal
	 * The existing version of the board. This was pulled from the database.
	 * 
	 * @param boardJSONDataNew
	 * The board as just now sent to the server by the player. This was the player's move.
	 * 
	 * @param status
	 * The current status of the board.
	 * 
	 * @return
	 * If the boards are completely valid and there is no issue, then this will return the status
	 * that you gave it. If this method detects that the game is now over, it will return the
	 * Utilities.BOARD_WIN byte. If that is returned, then that means that the player who's turn
	 * this is is the winner. If the board is detected as being invalid, Utilities.BOARD_INVALID
	 * will be returned.
	 */
	public static byte checkBoardValidityAndStatus(final String boardJSONDataOriginal, final String boardJSONDataNew, final byte status)
	{
		if (boardJSONDataOriginal == null || boardJSONDataOriginal.isEmpty()
			|| boardJSONDataNew == null || boardJSONDataNew.isEmpty())
		{
			return Utilities.BOARD_INVALID;
		}
		else
		{
			return status;
		}
	}


	/**
	 * Flips team coordinate systems.
	 * 
	 * @param boardJSONData
	 * String containing teams that you want to flip.
	 * 
	 * @return
	 * A new JSON String with the teams flipped.
	 */
	public static String flipTeams(final String board)
	{
		if (board == null || board.isEmpty())
		{
			return null;
		}
		else
		{
			return Board.flipTeams(board);
		}
	}


}
