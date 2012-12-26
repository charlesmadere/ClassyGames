package edu.selu.android.classygames;


import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragment;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.GenericBoard;


public abstract class GameFragment extends SherlockFragment implements OnClickListener
{


	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_ID = "GAME_PERSON_CHALLENGED_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_NAME = "GAME_PERSON_CHALLENGED_NAME";

	protected boolean boardLocked = false;
	protected String gameId = null;
	protected Person personChallenged = null;

	/**
	 * JSON String downloaded from the server that represents the board.
	 */
	protected String boardJSON = null;

	/**
	 * The actual logical representation of the board.
	 */
	protected GenericBoard board = null;


	/**
	 * Locks the board. This prevents the player from continuing to move
	 * pieces around. The player must press the undo button if they want to
	 * move anything from this point on.
	 */
	protected void lockBoard()
	{
		if (!boardLocked)
		{
			boardLocked = true;
		}
	}


	/**
	 * Undoes the user's last move on the board. Unlocks the board, allowing
	 * the user to make a different move on the board.
	 */
	protected void undo()
	{
		if (boardLocked)
		{
			boardLocked = false;
		}
	}


	protected abstract void initBoard();
	protected abstract void initPieces();


}
