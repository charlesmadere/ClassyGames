package com.charlesmadere.android.classygames;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageButton;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.games.Coordinate;
import com.charlesmadere.android.classygames.games.Position;
import com.charlesmadere.android.classygames.games.checkers.Board;
import com.charlesmadere.android.classygames.games.checkers.Piece;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckersGameFragment extends GenericGameFragment
{


	/**
	 * Bitmap representing the player's normal piece.
	 */
	private BitmapDrawable playerNormal;


	/**
	 * Bitmap representing the player's king piece.
	 */
	private BitmapDrawable playerKing;


	/**
	 * Bitmap representing the opponent's normal piece.
	 */
	private BitmapDrawable opponentNormal;


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

	}


	@Override
	protected void destroyBitmapDrawables()
	{
		destroyBitmapDrawables
		(
			playerNormal,
			playerKing,
			opponentNormal,
			opponentKing
		);
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
			case Piece.TYPE_NORMAL:
				if (piece.isTeamPlayer())
				{
					imageButton.setImageDrawable(playerNormal);
				}
				else
				{
					imageButton.setImageDrawable(opponentNormal);
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
		return R.layout.checkers_game_fragment;
	}


	@Override
	protected int getLoadingText()
	{
		return R.string.loading_checkers_game_against_x;
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
			view.findViewById(R.id.checkers_game_fragment_x0y0),
			view.findViewById(R.id.checkers_game_fragment_x1y0),
			view.findViewById(R.id.checkers_game_fragment_x2y0),
			view.findViewById(R.id.checkers_game_fragment_x3y0),
			view.findViewById(R.id.checkers_game_fragment_x4y0),
			view.findViewById(R.id.checkers_game_fragment_x5y0),
			view.findViewById(R.id.checkers_game_fragment_x6y0),
			view.findViewById(R.id.checkers_game_fragment_x7y0),
			view.findViewById(R.id.checkers_game_fragment_x0y1),
			view.findViewById(R.id.checkers_game_fragment_x1y1),
			view.findViewById(R.id.checkers_game_fragment_x2y1),
			view.findViewById(R.id.checkers_game_fragment_x3y1),
			view.findViewById(R.id.checkers_game_fragment_x4y1),
			view.findViewById(R.id.checkers_game_fragment_x5y1),
			view.findViewById(R.id.checkers_game_fragment_x6y1),
			view.findViewById(R.id.checkers_game_fragment_x7y1),
			view.findViewById(R.id.checkers_game_fragment_x0y2),
			view.findViewById(R.id.checkers_game_fragment_x1y2),
			view.findViewById(R.id.checkers_game_fragment_x2y2),
			view.findViewById(R.id.checkers_game_fragment_x3y2),
			view.findViewById(R.id.checkers_game_fragment_x4y2),
			view.findViewById(R.id.checkers_game_fragment_x5y2),
			view.findViewById(R.id.checkers_game_fragment_x6y2),
			view.findViewById(R.id.checkers_game_fragment_x7y2),
			view.findViewById(R.id.checkers_game_fragment_x0y3),
			view.findViewById(R.id.checkers_game_fragment_x1y3),
			view.findViewById(R.id.checkers_game_fragment_x2y3),
			view.findViewById(R.id.checkers_game_fragment_x3y3),
			view.findViewById(R.id.checkers_game_fragment_x4y3),
			view.findViewById(R.id.checkers_game_fragment_x5y3),
			view.findViewById(R.id.checkers_game_fragment_x6y3),
			view.findViewById(R.id.checkers_game_fragment_x7y3),
			view.findViewById(R.id.checkers_game_fragment_x0y4),
			view.findViewById(R.id.checkers_game_fragment_x1y4),
			view.findViewById(R.id.checkers_game_fragment_x2y4),
			view.findViewById(R.id.checkers_game_fragment_x3y4),
			view.findViewById(R.id.checkers_game_fragment_x4y4),
			view.findViewById(R.id.checkers_game_fragment_x5y4),
			view.findViewById(R.id.checkers_game_fragment_x6y4),
			view.findViewById(R.id.checkers_game_fragment_x7y4),
			view.findViewById(R.id.checkers_game_fragment_x0y5),
			view.findViewById(R.id.checkers_game_fragment_x1y5),
			view.findViewById(R.id.checkers_game_fragment_x2y5),
			view.findViewById(R.id.checkers_game_fragment_x3y5),
			view.findViewById(R.id.checkers_game_fragment_x4y5),
			view.findViewById(R.id.checkers_game_fragment_x5y5),
			view.findViewById(R.id.checkers_game_fragment_x6y5),
			view.findViewById(R.id.checkers_game_fragment_x7y5),
			view.findViewById(R.id.checkers_game_fragment_x0y6),
			view.findViewById(R.id.checkers_game_fragment_x1y6),
			view.findViewById(R.id.checkers_game_fragment_x2y6),
			view.findViewById(R.id.checkers_game_fragment_x3y6),
			view.findViewById(R.id.checkers_game_fragment_x4y6),
			view.findViewById(R.id.checkers_game_fragment_x5y6),
			view.findViewById(R.id.checkers_game_fragment_x6y6),
			view.findViewById(R.id.checkers_game_fragment_x7y6),
			view.findViewById(R.id.checkers_game_fragment_x0y7),
			view.findViewById(R.id.checkers_game_fragment_x1y7),
			view.findViewById(R.id.checkers_game_fragment_x2y7),
			view.findViewById(R.id.checkers_game_fragment_x3y7),
			view.findViewById(R.id.checkers_game_fragment_x4y7),
			view.findViewById(R.id.checkers_game_fragment_x5y7),
			view.findViewById(R.id.checkers_game_fragment_x6y7),
			view.findViewById(R.id.checkers_game_fragment_x7y7)
		);

		// create an array of the board's rows
		final int [] xPositions = new int[8];
		xPositions[0] = R.id.checkers_game_fragment_x0;
		xPositions[1] = R.id.checkers_game_fragment_x1;
		xPositions[2] = R.id.checkers_game_fragment_x2;
		xPositions[3] = R.id.checkers_game_fragment_x3;
		xPositions[4] = R.id.checkers_game_fragment_x4;
		xPositions[5] = R.id.checkers_game_fragment_x5;
		xPositions[6] = R.id.checkers_game_fragment_x6;
		xPositions[7] = R.id.checkers_game_fragment_x7;

		// create an array of the board's columns
		final int [] yPositions = new int[8];
		yPositions[0] = R.id.checkers_game_fragment_y0;
		yPositions[1] = R.id.checkers_game_fragment_y1;
		yPositions[2] = R.id.checkers_game_fragment_y2;
		yPositions[3] = R.id.checkers_game_fragment_y3;
		yPositions[4] = R.id.checkers_game_fragment_y4;
		yPositions[5] = R.id.checkers_game_fragment_y5;
		yPositions[6] = R.id.checkers_game_fragment_y6;
		yPositions[7] = R.id.checkers_game_fragment_y7;

		setAllBoardPositionsToEqualHeightAndWidth(view, R.id.checkers_game_fragment_x0y7, xPositions, yPositions);
	}


	@Override
	protected void loadPieces()
	{
		final String blue = getString(R.string.blue);
		final String green = getString(R.string.green);
		final String orange = getString(R.string.orange);
		final String pink = getString(R.string.pink);

		final String keyPlayerColor = getString(R.string.settings_key_players_checkers_piece_color);
		final String keyOpponentColor = getString(R.string.settings_key_opponents_checkers_piece_color);

		final SharedPreferences sPreferences = Utilities.getPreferences(getSherlockActivity());

		// Read in the colors that the player has selected to use for their
		// checkers pieces. If the user has not set a color, the playerColor
		// String will default to green and the opponentColor String will
		// default to orange.
		String playerColor = sPreferences.getString(keyPlayerColor, green);
		String opponentColor = sPreferences.getString(keyOpponentColor, orange);

		if (playerColor.equalsIgnoreCase(opponentColor))
		// Check to see if the color that the player has set is for their own
		// color is the same as the one that they set for the opponent's color.
		// This if statement will validate as true if that is the case.
		{
			playerColor = green;
			opponentColor = orange;

			final SharedPreferences.Editor editor = sPreferences.edit();

			// Change the value as saved in the user's preferences to the
			// default colors. This fixes the conflicting color issue.
			editor.putString(keyPlayerColor, playerColor);
			editor.putString(keyOpponentColor, opponentColor);
			editor.commit();
		}

		final int playerColorNormal;
		final int playerColorKing;

		if (playerColor.equalsIgnoreCase(blue))
		{
			playerColorNormal = R.drawable.piece_checkers_blue_normal;
			playerColorKing = R.drawable.piece_checkers_blue_king;
		}
		else if (playerColor.equalsIgnoreCase(orange))
		{
			playerColorNormal = R.drawable.piece_checkers_orange_normal;
			playerColorKing = R.drawable.piece_checkers_orange_king;
		}
		else if (playerColor.equalsIgnoreCase(pink))
		{
			playerColorNormal = R.drawable.piece_checkers_pink_normal;
			playerColorKing = R.drawable.piece_checkers_pink_king;
		}
		else
		{
			playerColorNormal = R.drawable.piece_checkers_green_normal;
			playerColorKing = R.drawable.piece_checkers_green_king;
		}

		final int opponentColorNormal;
		final int opponentColorKing;

		if (opponentColor.equalsIgnoreCase(blue))
		{
			opponentColorNormal = R.drawable.piece_checkers_blue_normal;
			opponentColorKing = R.drawable.piece_checkers_blue_king;
		}
		else if (opponentColor.equalsIgnoreCase(green))
		{
			opponentColorNormal = R.drawable.piece_checkers_green_normal;
			opponentColorKing = R.drawable.piece_checkers_green_king;
		}
		else if (opponentColor.equalsIgnoreCase(pink))
		{
			opponentColorNormal = R.drawable.piece_checkers_pink_normal;
			opponentColorKing = R.drawable.piece_checkers_pink_king;
		}
		else
		{
			opponentColorNormal = R.drawable.piece_checkers_orange_normal;
			opponentColorKing = R.drawable.piece_checkers_orange_king;
		}

		// Load Drawables for checkers pieces into memory. This is done so that
		// later when we draw these checkers pieces onto the board, that draw
		// process can be done very quickly as all of the picture data has
		// already been loaded.

		final Resources resources = getResources();
		playerNormal = (BitmapDrawable) resources.getDrawable(playerColorNormal);
		playerKing = (BitmapDrawable) resources.getDrawable(playerColorKing);
		opponentNormal = (BitmapDrawable) resources.getDrawable(opponentColorNormal);
		opponentKing = (BitmapDrawable) resources.getDrawable(opponentColorKing);
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
				setPositionBackground(positionCurrent, true, coordinateCurrent);
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
			setPositionBackground(positionPrevious, false, coordinatePrevious);

			final Coordinate coordinateCurrent = new Coordinate((String) positionCurrent.getTag());
			final Position current = board.getPosition(coordinateCurrent);

			if (!current.hasPiece())
			{
				setPositionBackground(positionCurrent, true, coordinateCurrent);

				if (board.move(previous, current))
				{
					flush();
					readyToSendMove(true);

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
			else
			{
				clearSelectedPositions();
			}
		}
	}


	@Override
	protected void resumeOldBoard(final JSONObject boardJSON) throws JSONException
	{
		board = new Board(boardJSON);
	}


}
