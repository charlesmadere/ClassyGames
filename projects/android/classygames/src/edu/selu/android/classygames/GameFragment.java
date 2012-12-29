package edu.selu.android.classygames;


import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragment;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.GenericBoard;


public abstract class GameFragment extends SherlockFragment implements OnClickListener
{


	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_ID = "GAME_PERSON_CHALLENGED_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_NAME = "GAME_PERSON_CHALLENGED_NAME";


	/**
	 * Boolean indicating if the board is locked or not. Once the board has
	 * been locked it can only be locked by using undo.
	 */
	protected boolean boardLocked = false;


	/**
	 * The ID of this game. If this is a brand new game, this value does not
	 * need to be grabbed from the server and can stay null. Otherwise, this
	 * String <strong>must</strong> have a value.
	 */
	protected String gameId = null;


	/**
	 * Object representing the living person that I am playing against.
	 */
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


	@Override
	public void onClick(final View v)
	{
		if (!boardLocked)
		// only continue if the board is currently unlocked
		{
			onBoardClick(v);
		}
	}


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces accordingly.
	 * 
	 * @param v
	 * The View object that was clicked.
	 */
	protected abstract void onBoardClick(final View v);


	/**
	 * Initializes the game board as seen on the device's screen.
	 */
	protected abstract void initBoard();


	/**
	 * Initializes the game's pieces <strong>as if it's a brand new game
	 * </strong>. This method should <strong>only ever be used if it is a brand
	 * new game</strong>.
	 */
	protected abstract void initPieces();


}
