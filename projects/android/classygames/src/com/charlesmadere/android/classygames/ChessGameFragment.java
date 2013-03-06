package com.charlesmadere.android.classygames;


import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageButton;

import com.charlesmadere.android.classygames.games.Coordinate;
import com.charlesmadere.android.classygames.games.Position;
import com.charlesmadere.android.classygames.games.chess.Board;
import com.charlesmadere.android.classygames.games.chess.Piece;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class ChessGameFragment extends GenericGameFragment
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - ChessGameFragment";




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




	@Override
	protected void onCreateView()
	{

	}


	@Override
	protected void flush(final Position position)
	{
		final String tag = createTag(position.getCoordinate());
		final Piece piece = (Piece) position.getPiece();
		final ImageButton imageButton = (ImageButton) getView().findViewWithTag(tag);

		switch (piece.getType())
		{
			case Piece.TYPE_PAWN:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerPawn);
				}
				else
				{
					imageButton.setImageDrawable(opponentPawn);
				}
				break;

			case Piece.TYPE_BISHOP:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerBishop);
				}
				else
				{
					imageButton.setImageDrawable(opponentBishop);
				}
				break;

			case Piece.TYPE_KNIGHT:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerKnight);
				}
				else
				{
					imageButton.setImageDrawable(opponentKnight);
				}
				break;

			case Piece.TYPE_ROOK:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerRook);
				}
				else
				{
					imageButton.setImageDrawable(opponentRook);
				}
				break;

			case Piece.TYPE_QUEEN:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerQueen);
				}
				else
				{
					imageButton.setImageDrawable(opponentQueen);
				}
				break;

			case Piece.TYPE_KING:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerKing);
				}
				else
				{
					imageButton.setImageDrawable(opponentKing);
				}
				break;
		}
	}


	@Override
	protected int getGameView()
	{
		return R.layout.chess_game_fragment;
	}


	@Override
	protected int getLoadingText()
	{
		return R.string.chess_game_fragment_loading_text;
	}


	@Override
	protected void initNewBoard() throws JSONException
	{
		board = new Board();
	}


	@Override
	protected void initViews()
	{

	}


	@Override
	protected void onBoardClick(final ImageButton positionCurrent)
	{
		final Coordinate coordinateCurrent = new Coordinate((String) positionCurrent.getTag());
		setPositionBackground(positionCurrent, true, coordinateCurrent);

		final Position current = board.getPosition(coordinateCurrent);
		Log.d(LOG_TAG, "Click! " + coordinateCurrent + " - has piece? " + current.getPiece());
	}


	@Override
	protected void onBoardClick(final ImageButton positionPrevious, final ImageButton positionCurrent)
	{
		final Coordinate coordinatePrevious = new Coordinate((String) positionPrevious.getTag());
		setPositionBackground(positionPrevious, false, coordinatePrevious);

		final Coordinate coordinateCurrent = new Coordinate((String) positionCurrent.getTag());
		setPositionBackground(positionCurrent, true, coordinateCurrent);

		final Position current = board.getPosition(coordinateCurrent);
		final Position previous = board.getPosition(coordinatePrevious);
		Log.d(LOG_TAG, "Click! Old: " + coordinatePrevious + " has piece? " + previous.getPiece() + ", New: " + coordinateCurrent + " has piece? " + current.getPiece());
	}


	@Override
	protected void resumeOldBoard(final JSONObject boardJSON) throws JSONException
	{
		board = new Board(boardJSON);
	}


}
