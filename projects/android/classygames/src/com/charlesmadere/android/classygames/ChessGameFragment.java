package com.charlesmadere.android.classygames;


import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.games.Coordinate;
import com.charlesmadere.android.classygames.games.Position;
import com.charlesmadere.android.classygames.games.chess.Board;
import com.charlesmadere.android.classygames.games.chess.Piece;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;


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
	protected void createOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		// TODO
		// Chess will require an options menu to perform some moves like
		// castling. Off the top of my head right now that's the only one that
		// I can think of, but there may be more.
	}


	@Override
	protected boolean optionsItemSelected(final MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void prepareOptionsMenu(final Menu menu)
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
		return R.string.loading_chess_game_against_x;
	}


	@Override
	protected void initNewBoard() throws JSONException
	{
		board = new Board();
	}


	@Override
	protected void initViews()
	{
		final View view = getView();

		setBoardOnClickListeners
		(
			view.findViewById(R.id.chess_game_fragment_x0y0),
			view.findViewById(R.id.chess_game_fragment_x1y0),
			view.findViewById(R.id.chess_game_fragment_x2y0),
			view.findViewById(R.id.chess_game_fragment_x3y0),
			view.findViewById(R.id.chess_game_fragment_x4y0),
			view.findViewById(R.id.chess_game_fragment_x5y0),
			view.findViewById(R.id.chess_game_fragment_x6y0),
			view.findViewById(R.id.chess_game_fragment_x7y0),
			view.findViewById(R.id.chess_game_fragment_x0y1),
			view.findViewById(R.id.chess_game_fragment_x1y1),
			view.findViewById(R.id.chess_game_fragment_x2y1),
			view.findViewById(R.id.chess_game_fragment_x3y1),
			view.findViewById(R.id.chess_game_fragment_x4y1),
			view.findViewById(R.id.chess_game_fragment_x5y1),
			view.findViewById(R.id.chess_game_fragment_x6y1),
			view.findViewById(R.id.chess_game_fragment_x7y1),
			view.findViewById(R.id.chess_game_fragment_x0y2),
			view.findViewById(R.id.chess_game_fragment_x1y2),
			view.findViewById(R.id.chess_game_fragment_x2y2),
			view.findViewById(R.id.chess_game_fragment_x3y2),
			view.findViewById(R.id.chess_game_fragment_x4y2),
			view.findViewById(R.id.chess_game_fragment_x5y2),
			view.findViewById(R.id.chess_game_fragment_x6y2),
			view.findViewById(R.id.chess_game_fragment_x7y2),
			view.findViewById(R.id.chess_game_fragment_x0y3),
			view.findViewById(R.id.chess_game_fragment_x1y3),
			view.findViewById(R.id.chess_game_fragment_x2y3),
			view.findViewById(R.id.chess_game_fragment_x3y3),
			view.findViewById(R.id.chess_game_fragment_x4y3),
			view.findViewById(R.id.chess_game_fragment_x5y3),
			view.findViewById(R.id.chess_game_fragment_x6y3),
			view.findViewById(R.id.chess_game_fragment_x7y3),
			view.findViewById(R.id.chess_game_fragment_x0y4),
			view.findViewById(R.id.chess_game_fragment_x1y4),
			view.findViewById(R.id.chess_game_fragment_x2y4),
			view.findViewById(R.id.chess_game_fragment_x3y4),
			view.findViewById(R.id.chess_game_fragment_x4y4),
			view.findViewById(R.id.chess_game_fragment_x5y4),
			view.findViewById(R.id.chess_game_fragment_x6y4),
			view.findViewById(R.id.chess_game_fragment_x7y4),
			view.findViewById(R.id.chess_game_fragment_x0y5),
			view.findViewById(R.id.chess_game_fragment_x1y5),
			view.findViewById(R.id.chess_game_fragment_x2y5),
			view.findViewById(R.id.chess_game_fragment_x3y5),
			view.findViewById(R.id.chess_game_fragment_x4y5),
			view.findViewById(R.id.chess_game_fragment_x5y5),
			view.findViewById(R.id.chess_game_fragment_x6y5),
			view.findViewById(R.id.chess_game_fragment_x7y5),
			view.findViewById(R.id.chess_game_fragment_x0y6),
			view.findViewById(R.id.chess_game_fragment_x1y6),
			view.findViewById(R.id.chess_game_fragment_x2y6),
			view.findViewById(R.id.chess_game_fragment_x3y6),
			view.findViewById(R.id.chess_game_fragment_x4y6),
			view.findViewById(R.id.chess_game_fragment_x5y6),
			view.findViewById(R.id.chess_game_fragment_x6y6),
			view.findViewById(R.id.chess_game_fragment_x7y6),
			view.findViewById(R.id.chess_game_fragment_x0y7),
			view.findViewById(R.id.chess_game_fragment_x1y7),
			view.findViewById(R.id.chess_game_fragment_x2y7),
			view.findViewById(R.id.chess_game_fragment_x3y7),
			view.findViewById(R.id.chess_game_fragment_x4y7),
			view.findViewById(R.id.chess_game_fragment_x5y7),
			view.findViewById(R.id.chess_game_fragment_x6y7),
			view.findViewById(R.id.chess_game_fragment_x7y7)
		);

		// Below we're going to create two different int arrays. One will
		// contain all of the board's ROWS and the other will contain all of
		// the board's COLUMNS. This is needed because the board as taken
		// directly from the raw XML file do not have each individual
		// position's height and width dimensions equal. This means that each
		// position is not a square. This issue can only be fixed in code, and
		// that code requires having a handle to all of board's rows and
		// columns.

		// create an array of handles to the board's rows
		final int [] xPositions = new int[8];
		xPositions[0] = R.id.chess_game_fragment_x0;
		xPositions[1] = R.id.chess_game_fragment_x1;
		xPositions[2] = R.id.chess_game_fragment_x2;
		xPositions[3] = R.id.chess_game_fragment_x3;
		xPositions[4] = R.id.chess_game_fragment_x4;
		xPositions[5] = R.id.chess_game_fragment_x5;
		xPositions[6] = R.id.chess_game_fragment_x6;
		xPositions[7] = R.id.chess_game_fragment_x7;

		// create an array of handles to the board's columns
		final int [] yPositions = new int[8];
		yPositions[0] = R.id.chess_game_fragment_y0;
		yPositions[1] = R.id.chess_game_fragment_y1;
		yPositions[2] = R.id.chess_game_fragment_y2;
		yPositions[3] = R.id.chess_game_fragment_y3;
		yPositions[4] = R.id.chess_game_fragment_y4;
		yPositions[5] = R.id.chess_game_fragment_y5;
		yPositions[6] = R.id.chess_game_fragment_y6;
		yPositions[7] = R.id.chess_game_fragment_y7;

		// run the method that will perform the actual board resizing code
		setAllBoardPositionsToEqualHeightAndWidth(view, R.id.chess_game_fragment_x0y7, xPositions, yPositions);
	}


	@Override
	protected void loadPieces()
	{
		// TODO
		// Artwork for the chess pieces has been created but is not yet
		// included in the app. Once that artwork is added to the app, then
		// this method will need to be filled out.
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
