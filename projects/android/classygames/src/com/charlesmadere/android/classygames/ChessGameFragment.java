package com.charlesmadere.android.classygames;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.Position;
import com.charlesmadere.android.classygames.models.games.chess.Board;
import com.charlesmadere.android.classygames.models.games.chess.Piece;
import org.json.JSONException;
import org.json.JSONObject;


public final class ChessGameFragment extends GenericGameFragment
{


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
	 * Stores the String R.string.* value for the player's chosen piece color.
	 */
	private int playerColor;




	public static ChessGameFragment newInstance(final String gameId, final byte whichGame, final Person person)
	{
		final Bundle arguments = GenericGameFragment.prepareArguments(gameId, whichGame, person);
		final ChessGameFragment fragment = new ChessGameFragment();
		fragment.setArguments(arguments);

		return fragment;
	}




	@Override
	protected void createOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.chess_game_fragment, menu);
		final MenuItem castle = menu.findItem(R.id.chess_game_fragment_menu_castle);

		if (board == null)
		{
			castle.setEnabled(false);
		}
		else
		{
			if (((Board) board).canCastle())
			{
				castle.setEnabled(true);
			}
			else
			{
				castle.setEnabled(false);
			}
		}
	}


	@Override
	protected boolean optionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.chess_game_fragment_menu_castle:

				break;

			case R.id.chess_game_fragment_menu_glossary:
				final FragmentManager fManager = getChildFragmentManager();
				final FragmentTransaction fTransaction = fManager.beginTransaction();
				fTransaction.addToBackStack(null);

				final Bundle arguments = new Bundle();
				arguments.putInt(ChessGlossaryDialogFragment.KEY_PLAYER_COLOR, playerColor);

				final ChessGlossaryDialogFragment dialog = new ChessGlossaryDialogFragment();
				dialog.setArguments(arguments);
				dialog.show(fManager, null);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
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
	protected String getDefaultPlayersPieceColor()
	{
		return getString(R.string.pink);
	}


	@Override
	protected String getDefaultOpponentsPieceColor()
	{
		return getString(R.string.blue);
	}


	@Override
	protected int getGameView()
	{
		return R.layout.checkers_and_chess_game_fragment;
	}


	@Override
	protected int getLoadingText()
	{
		return R.string.loading_chess_game_against_x;
	}


	@Override
	protected int getSettingsKeyForPlayersPieceColor()
	{
		return R.string.settings_key_players_chess_piece_color;
	}


	@Override
	protected int getSettingsKeyForOpponentsPieceColor()
	{
		return R.string.settings_key_opponents_chess_piece_color;
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
			R.id.checkers_and_chess_game_fragment_x0y0,
			R.id.checkers_and_chess_game_fragment_x1y0,
			R.id.checkers_and_chess_game_fragment_x2y0,
			R.id.checkers_and_chess_game_fragment_x3y0,
			R.id.checkers_and_chess_game_fragment_x4y0,
			R.id.checkers_and_chess_game_fragment_x5y0,
			R.id.checkers_and_chess_game_fragment_x6y0,
			R.id.checkers_and_chess_game_fragment_x7y0,
			R.id.checkers_and_chess_game_fragment_x0y1,
			R.id.checkers_and_chess_game_fragment_x1y1,
			R.id.checkers_and_chess_game_fragment_x2y1,
			R.id.checkers_and_chess_game_fragment_x3y1,
			R.id.checkers_and_chess_game_fragment_x4y1,
			R.id.checkers_and_chess_game_fragment_x5y1,
			R.id.checkers_and_chess_game_fragment_x6y1,
			R.id.checkers_and_chess_game_fragment_x7y1,
			R.id.checkers_and_chess_game_fragment_x0y2,
			R.id.checkers_and_chess_game_fragment_x1y2,
			R.id.checkers_and_chess_game_fragment_x2y2,
			R.id.checkers_and_chess_game_fragment_x3y2,
			R.id.checkers_and_chess_game_fragment_x4y2,
			R.id.checkers_and_chess_game_fragment_x5y2,
			R.id.checkers_and_chess_game_fragment_x6y2,
			R.id.checkers_and_chess_game_fragment_x7y2,
			R.id.checkers_and_chess_game_fragment_x0y3,
			R.id.checkers_and_chess_game_fragment_x1y3,
			R.id.checkers_and_chess_game_fragment_x2y3,
			R.id.checkers_and_chess_game_fragment_x3y3,
			R.id.checkers_and_chess_game_fragment_x4y3,
			R.id.checkers_and_chess_game_fragment_x5y3,
			R.id.checkers_and_chess_game_fragment_x6y3,
			R.id.checkers_and_chess_game_fragment_x7y3,
			R.id.checkers_and_chess_game_fragment_x0y4,
			R.id.checkers_and_chess_game_fragment_x1y4,
			R.id.checkers_and_chess_game_fragment_x2y4,
			R.id.checkers_and_chess_game_fragment_x3y4,
			R.id.checkers_and_chess_game_fragment_x4y4,
			R.id.checkers_and_chess_game_fragment_x5y4,
			R.id.checkers_and_chess_game_fragment_x6y4,
			R.id.checkers_and_chess_game_fragment_x7y4,
			R.id.checkers_and_chess_game_fragment_x0y5,
			R.id.checkers_and_chess_game_fragment_x1y5,
			R.id.checkers_and_chess_game_fragment_x2y5,
			R.id.checkers_and_chess_game_fragment_x3y5,
			R.id.checkers_and_chess_game_fragment_x4y5,
			R.id.checkers_and_chess_game_fragment_x5y5,
			R.id.checkers_and_chess_game_fragment_x6y5,
			R.id.checkers_and_chess_game_fragment_x7y5,
			R.id.checkers_and_chess_game_fragment_x0y6,
			R.id.checkers_and_chess_game_fragment_x1y6,
			R.id.checkers_and_chess_game_fragment_x2y6,
			R.id.checkers_and_chess_game_fragment_x3y6,
			R.id.checkers_and_chess_game_fragment_x4y6,
			R.id.checkers_and_chess_game_fragment_x5y6,
			R.id.checkers_and_chess_game_fragment_x6y6,
			R.id.checkers_and_chess_game_fragment_x7y6,
			R.id.checkers_and_chess_game_fragment_x0y7,
			R.id.checkers_and_chess_game_fragment_x1y7,
			R.id.checkers_and_chess_game_fragment_x2y7,
			R.id.checkers_and_chess_game_fragment_x3y7,
			R.id.checkers_and_chess_game_fragment_x4y7,
			R.id.checkers_and_chess_game_fragment_x5y7,
			R.id.checkers_and_chess_game_fragment_x6y7,
			R.id.checkers_and_chess_game_fragment_x7y7
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
		xPositions[0] = R.id.checkers_and_chess_game_fragment_x0;
		xPositions[1] = R.id.checkers_and_chess_game_fragment_x1;
		xPositions[2] = R.id.checkers_and_chess_game_fragment_x2;
		xPositions[3] = R.id.checkers_and_chess_game_fragment_x3;
		xPositions[4] = R.id.checkers_and_chess_game_fragment_x4;
		xPositions[5] = R.id.checkers_and_chess_game_fragment_x5;
		xPositions[6] = R.id.checkers_and_chess_game_fragment_x6;
		xPositions[7] = R.id.checkers_and_chess_game_fragment_x7;

		// create an array of handles to the board's columns
		final int [] yPositions = new int[8];
		yPositions[0] = R.id.checkers_and_chess_game_fragment_y0;
		yPositions[1] = R.id.checkers_and_chess_game_fragment_y1;
		yPositions[2] = R.id.checkers_and_chess_game_fragment_y2;
		yPositions[3] = R.id.checkers_and_chess_game_fragment_y3;
		yPositions[4] = R.id.checkers_and_chess_game_fragment_y4;
		yPositions[5] = R.id.checkers_and_chess_game_fragment_y5;
		yPositions[6] = R.id.checkers_and_chess_game_fragment_y6;
		yPositions[7] = R.id.checkers_and_chess_game_fragment_y7;

		// run the method that will perform the actual board resizing code
		setAllBoardPositionsToEqualHeightAndWidth(view, R.id.checkers_and_chess_game_fragment_x0y7, xPositions, yPositions);
	}


	@Override
	protected void loadBluePieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_blue);
			playerBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_blue);
			playerKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_blue);
			playerRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_blue);
			playerQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_blue);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_blue);

			playerColor = R.string.blue;
		}
		else
		{
			opponentPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_blue);
			opponentBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_blue);
			opponentKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_blue);
			opponentRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_blue);
			opponentQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_blue);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_blue);
		}
	}


	@Override
	protected void loadGreenPieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_green);
			playerBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_green);
			playerKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_green);
			playerRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_green);
			playerQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_green);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_green);

			playerColor = R.string.green;
		}
		else
		{
			opponentPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_green);
			opponentBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_green);
			opponentKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_green);
			opponentRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_green);
			opponentQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_green);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_green);
		}
	}


	@Override
	protected void loadOrangePieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_orange);
			playerBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_orange);
			playerKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_orange);
			playerRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_orange);
			playerQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_orange);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_orange);

			playerColor = R.string.orange;
		}
		else
		{
			opponentPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_orange);
			opponentBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_orange);
			opponentKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_orange);
			opponentRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_orange);
			opponentQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_orange);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_orange);
		}
	}


	@Override
	protected void loadPinkPieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_pink);
			playerBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_pink);
			playerKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_pink);
			playerRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_pink);
			playerQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_pink);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_pink);

			playerColor = R.string.pink;
		}
		else
		{
			opponentPawn = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_pawn_pink);
			opponentBishop = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_bishop_pink);
			opponentKnight = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_knight_pink);
			opponentRook = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_rook_pink);
			opponentQueen = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_queen_pink);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_chess_king_pink);
		}
	}


	@Override
	protected void onBoardClick(final ImageButton positionCurrent)
	{
		if (board.isBoardLocked())
		{
			clearSelectedPositions();
		}
		else
		{
			final Coordinate coordinateCurrent = new Coordinate((String) positionCurrent.getTag());
			final Position current = board.getPosition(coordinateCurrent);

			if (current.hasPiece() && current.getPiece().isTeamPlayer())
			{
				positionCurrent.setSelected(true);
			}
			else
			{
				clearSelectedPositions();
			}
		}
	}


	@Override
	protected void onBoardClick(final ImageButton positionPrevious, final ImageButton positionCurrent)
	{
		if (!board.isBoardLocked())
		{
			final Coordinate coordinatePrevious = new Coordinate((String) positionPrevious.getTag());
			final Position previous = board.getPosition(coordinatePrevious);
			positionPrevious.setSelected(false);

			final Coordinate coordinateCurrent = new Coordinate((String) positionCurrent.getTag());
			final Position current = board.getPosition(coordinateCurrent);

			if (!current.hasPiece())
			{
				positionCurrent.setSelected(true);

				if (board.move(previous, current))
				{
					flush();
					getSherlockActivity().supportInvalidateOptionsMenu();

					switch (((Board) board).isBoardInCheckOrCheckmate())
					{
						case Board.BOARD_NORMAL:
							break;

						case Board.BOARD_CHECK:
							break;

						case Board.BOARD_CHECKMATE:
							break;
					}
				}
			}

			clearSelectedPositions();
		}
	}


	@Override
	protected void resumeOldBoard(final JSONObject boardJSON) throws JSONException
	{
		board = new Board(boardJSON);
	}


}
