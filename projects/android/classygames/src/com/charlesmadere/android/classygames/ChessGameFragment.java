package com.charlesmadere.android.classygames;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.GenericPiece;
import com.charlesmadere.android.classygames.models.games.Position;
import com.charlesmadere.android.classygames.models.games.chess.Board;
import com.charlesmadere.android.classygames.models.games.chess.Piece;
import com.charlesmadere.android.classygames.views.PositionView;
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
		final MenuItem castleMenuItem = menu.findItem(R.id.chess_game_fragment_menu_castle);

		if (board == null)
		{
			castleMenuItem.setEnabled(false);
		}
		else
		{
			if (((Board) board).canCastle())
			{
				castleMenuItem.setEnabled(true);
			}
			else
			{
				castleMenuItem.setEnabled(false);
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

				final ChessGlossaryDialogFragment dialog = ChessGlossaryDialogFragment.newInstance(playerColor);
				dialog.show(fManager, null);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	protected void flush(final GenericPiece piece, final PositionView positionView)
	{
		switch (piece.getType())
		{
			case Piece.TYPE_PAWN:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerPawn);
				}
				else
				{
					positionView.setImageDrawable(opponentPawn);
				}
				break;

			case Piece.TYPE_BISHOP:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerBishop);
				}
				else
				{
					positionView.setImageDrawable(opponentBishop);
				}
				break;

			case Piece.TYPE_KNIGHT:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerKnight);
				}
				else
				{
					positionView.setImageDrawable(opponentKnight);
				}
				break;

			case Piece.TYPE_ROOK:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerRook);
				}
				else
				{
					positionView.setImageDrawable(opponentRook);
				}
				break;

			case Piece.TYPE_QUEEN:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerQueen);
				}
				else
				{
					positionView.setImageDrawable(opponentQueen);
				}
				break;

			case Piece.TYPE_KING:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerKing);
				}
				else
				{
					positionView.setImageDrawable(opponentKing);
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
	protected void onBoardClick(final PositionView positionCurrent)
	{
		if (board.isBoardLocked())
		{
			clearSelectedPositions();
		}
		else
		{
			final Coordinate coordinateCurrent = positionCurrent.getCoordinate();
			final Position current = board.getPosition(coordinateCurrent);

			if (current.hasPiece() && current.getPiece().isTeamPlayer())
			{
				positionCurrent.select();
			}
			else
			{
				clearSelectedPositions();
			}
		}
	}


	@Override
	protected void onBoardClick(final PositionView positionPrevious, final PositionView positionCurrent)
	{
		if (!board.isBoardLocked())
		{
			final Coordinate coordinatePrevious = positionPrevious.getCoordinate();
			final Position previous = board.getPosition(coordinatePrevious);
			positionPrevious.unselect();

			final Coordinate coordinateCurrent = positionCurrent.getCoordinate();
			final Position current = board.getPosition(coordinateCurrent);

			if (board.move(previous, current))
			{
				flush();
				getSherlockActivity().supportInvalidateOptionsMenu();

				final int boardStatus = ((Board) board).isBoardInCheckOrCheckmate();

				switch (boardStatus)
				{
					case Board.BOARD_NORMAL:
						// doing nothing in this case is fine for now
						break;

					case Board.BOARD_CHECK:
						boardIsInCheck();
						break;

					case Board.BOARD_CHECKMATE:
						boardIsInCheckmate();
						break;
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


	private void boardIsInCheck()
	{
		Toast.makeText(getSherlockActivity(), R.string.check, Toast.LENGTH_LONG).show();

		// TODO
	}


	private void boardIsInCheckmate()
	{
		Toast.makeText(getSherlockActivity(), R.string.checkmate, Toast.LENGTH_LONG).show();

		// TODO
	}


}
