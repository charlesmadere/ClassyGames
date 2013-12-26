package com.charlesmadere.android.classygames;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.GenericPiece;
import com.charlesmadere.android.classygames.models.games.Position;
import com.charlesmadere.android.classygames.models.games.checkers.Board;
import com.charlesmadere.android.classygames.models.games.checkers.Piece;
import com.charlesmadere.android.classygames.views.PositionView;
import org.json.JSONException;
import org.json.JSONObject;


public final class CheckersGameFragment extends GenericGameFragment
{


	/**
	 * Bitmap representing the opponent's normal piece.
	 */
	private BitmapDrawable opponentNormal;


	/**
	 * Bitmap representing the opponent's king piece.
	 */
	private BitmapDrawable opponentKing;


	/**
	 * Bitmap representing the player's normal piece.
	 */
	private BitmapDrawable playerNormal;


	/**
	 * Bitmap representing the player's king piece.
	 */
	private BitmapDrawable playerKing;




	public static CheckersGameFragment newInstance(final String gameId, final byte whichGame, final Person person)
	{
		final Bundle arguments = prepareArguments(gameId, whichGame, person);
		final CheckersGameFragment fragment = new CheckersGameFragment();
		fragment.setArguments(arguments);

		return fragment;
	}




	@Override
	protected void createOptionsMenu(final Menu menu, final MenuInflater inflater)
	{

	}


	@Override
	protected boolean optionsItemSelected(final MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void flush(final GenericPiece piece, final PositionView positionView)
	{
		switch (piece.getType())
		{
			case Piece.TYPE_NORMAL:
				if (piece.isTeamPlayer())
				{
					positionView.setImageDrawable(playerNormal);
				}
				else
				{
					positionView.setImageDrawable(opponentNormal);
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
		return getString(R.string.green);
	}


	@Override
	protected String getDefaultOpponentsPieceColor()
	{
		return getString(R.string.orange);
	}


	@Override
	protected int getGameView()
	{
		return R.layout.checkers_and_chess_game_fragment;
	}


	@Override
	protected int getLoadingText()
	{
		return R.string.loading_checkers_game_against_x;
	}


	@Override
	protected int getSettingsKeyForPlayersPieceColor()
	{
		return R.string.settings_key_players_checkers_piece_color;
	}


	@Override
	protected int getSettingsKeyForOpponentsPieceColor()
	{
		return R.string.settings_key_opponents_checkers_piece_color;
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
			playerNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_blue);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_blue);
		}
		else
		{
			opponentNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_blue);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_blue);
		}
	}


	@Override
	protected void loadGreenPieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_green);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_green);
		}
		else
		{
			opponentNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_green);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_green);
		}
	}


	@Override
	protected void loadOrangePieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_orange);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_orange);
		}
		else
		{
			opponentNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_orange);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_orange);
		}
	}


	@Override
	protected void loadPinkPieceResources(final Resources res, final boolean isPlayersColor)
	{
		if (isPlayersColor)
		{
			playerNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_pink);
			playerKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_pink);
		}
		else
		{
			opponentNormal = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_normal_pink);
			opponentKing = (BitmapDrawable) res.getDrawable(R.drawable.piece_checkers_king_pink);
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

			if (current.hasPiece())
			{
				clearSelectedPositions();
			}
			else
			{
				positionCurrent.select();

				if (board.move(previous, current))
				{
					flush();
					getSherlockActivity().supportInvalidateOptionsMenu();

					if (board.isBoardLocked())
					{
						clearSelectedPositions();
					}
				}
				else
				{
					clearSelectedPositions();
				}
			}
		}
	}


	@Override
	protected void resumeOldBoard(final JSONObject boardJSON) throws JSONException
	{
		board = new Board(boardJSON);
	}


}
