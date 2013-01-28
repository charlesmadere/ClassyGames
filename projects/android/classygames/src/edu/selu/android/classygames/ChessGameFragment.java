package edu.selu.android.classygames;


import org.json.JSONArray;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.data.Person;


public class ChessGameFragment extends GenericGameFragment
{


	/**
	 * Bitmap representing the player's pawn piece.
	 */
	private BitmapDrawable playerPawn;


	/**
	 * Bitmap representing the player's bishop piece.
	 */
	private BitmapDrawable playerBishop;


	/**
	 * Bitmap representing the player's knight piece.
	 */
	private BitmapDrawable playerKnight;


	/**
	 * Bitmap representing the player's rook piece.
	 */
	private BitmapDrawable playerRook;


	/**
	 * Bitmap representing the player's queen piece.
	 */
	private BitmapDrawable playerQueen;


	/**
	 * Bitmap representing the player's king piece.
	 */
	private BitmapDrawable playerKing;


	/**
	 * Bitmap representing the opponent's pawn piece.
	 */
	private BitmapDrawable opponentPawn;


	/**
	 * Bitmap representing the opponent's bishop piece.
	 */
	private BitmapDrawable opponentBishop;


	/**
	 * Bitmap representing the opponent's knight piece.
	 */
	private BitmapDrawable opponentKnight;


	/**
	 * Bitmap representing the opponent's rook piece.
	 */
	private BitmapDrawable opponentRook;


	/**
	 * Bitmap representing the opponent's queen piece.
	 */
	private BitmapDrawable opponentQueen;


	/**
	 * Bitmap representing the opponent's king piece.
	 */
	private BitmapDrawable opponentKing;




	ChessGameFragment(final Person person)
	{
		super(person);
	}


	ChessGameFragment(final Game game)
	{
		super(game);
	}


	@Override
	protected int onCreateView()
	{
		return R.layout.chess_game_fragment;
	}


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{

	}


	@Override
	protected void flush()
	{

	}


	@Override
	protected int getTitle()
	{
		return R.string.chess_game_fragment_title;
	}


	@Override
	protected void initNewBoard()
	{

	}


	@Override
	protected void initViews()
	{

	}


	@Override
	protected void onBoardClick(final View v)
	{
		Log.d(LOG_TAG, "onBoardClick()! id: \"" + v.getId() + "\" tag: \"" + v.getTag() + "\"");
	}


}
