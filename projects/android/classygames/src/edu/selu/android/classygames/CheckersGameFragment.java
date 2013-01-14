package edu.selu.android.classygames;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.Menu;

import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.Position;
import edu.selu.android.classygames.games.checkers.Board;
import edu.selu.android.classygames.games.checkers.Piece;


public class CheckersGameFragment extends GenericGameFragment
{


	/**
	 * Bitmap representing the player's normal piece.
	 */
	private Bitmap playerNormal;


	/**
	 * Bitmap representing the player's king piece.
	 */
	private Bitmap playerKing;


	/**
	 * Bitmap representing the opponent's normal piece.
	 */
	private Bitmap opponentNormal;


	/**
	 * Bitmap representing the opponent's king piece.
	 */
	private Bitmap opponentKing;




	@Override
	protected int onCreateView()
	{
		return R.layout.checkers_game_fragment;
	}


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{
		for (int i = 0; i < team.length(); ++i)
		{
			try
			{
				final JSONObject piece = team.getJSONObject(i);
				final JSONArray coordinates = piece.getJSONArray("coordinate");

				if (coordinates.length() == 2)
				{
					final Coordinate coordinate = new Coordinate(coordinates.getInt(0), coordinates.getInt(1));

					if (board.isPositionValid(coordinate))
					{
						final int type = piece.getInt("type");
						board.getPosition(coordinate).setPiece(new Piece(whichTeam, type));
					}
					else
					{
						Log.e(LOG_TAG, "Coordinate outside proper range: " + coordinate + ".");
					}
				}
				else
				{
					Log.e(LOG_TAG, "A piece had an improper number of coordinate values.");
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "A team's piece was massively malformed.");
			}
		}
	}


	@Override
	protected void initBoardNew()
	{
		board = new Board();

		// set up the pieces for the current player's team
		board.getPosition(1, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(3, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(5, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(7, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(0, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(2, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(4, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(6, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(1, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(3, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(5, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		board.getPosition(7, 2).setPiece(new Piece(Piece.TEAM_PLAYER));

		// set up the pieces for the opponent player's team
		board.getPosition(0, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(2, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(4, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(6, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(1, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(3, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(5, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(7, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(0, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(2, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(4, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		board.getPosition(6, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
	}


	@Override
	protected void initViews()
	{
		getView().findViewById(R.id.checkers_game_fragment_x0y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y0).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y1).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y2).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y3).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y4).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y5).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y6).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y7).setOnClickListener(onBoardClick);

		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.height = getView().findViewById(R.id.checkers_game_fragment_x7y7).getWidth();

		getView().findViewById(R.id.checkers_game_fragment_y0).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y1).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y2).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y3).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y4).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y5).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y6).setLayoutParams(layoutParams);
		getView().findViewById(R.id.checkers_game_fragment_y7).setLayoutParams(layoutParams);

		// Load bitmaps for checkers pieces into memory. This is done so that
		// later when we draw these checkers pieces onto the board, that draw
		// process can be done very quickly.
		playerNormal = BitmapFactory.decodeResource(CheckersGameFragment.this.getResources(), R.drawable.piece_checkers_green_normal);
		playerKing = BitmapFactory.decodeResource(CheckersGameFragment.this.getResources(), R.drawable.piece_checkers_green_king);
		opponentNormal = BitmapFactory.decodeResource(CheckersGameFragment.this.getResources(), R.drawable.piece_checkers_orange_normal);
		opponentKing = BitmapFactory.decodeResource(CheckersGameFragment.this.getResources(), R.drawable.piece_checkers_orange_king);
	}


	@Override
	protected void onBoardClick(final View v)
	{
		Log.d(LOG_TAG, "Click! " + v.getId());
	}


	@Override
	public void onPrepareOptionsMenu(final Menu menu)
	{
		if (boardLocked)
		{
			menu.findItem(R.id.generic_game_fragment_actionbar_send_move).setEnabled(true);
			menu.findItem(R.id.generic_game_fragment_actionbar_undo_move).setEnabled(true);
		}
		else
		{
			menu.findItem(R.id.generic_game_fragment_actionbar_send_move).setEnabled(false);
			menu.findItem(R.id.generic_game_fragment_actionbar_undo_move).setEnabled(false);
		}
	}




	/**
	 * Renders all of the game's pieces on the board by first clearing all of
	 * the existing pieces from it and then placing all of the current pieces.
	 */
	private void flush()
	{
		// clear all of the existing pieces from the board
		for (byte x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
		{
			for (byte y = 0; y < Board.LENGTH_VERTICAL; ++y)
			{
				final String tag = createTag(x, y);
				((ImageButton) getView().findViewWithTag(tag)).setImageBitmap(null);
			}
		}

		// place all of the pieces back onto the board
		for (byte x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
		{
			for (byte y = 0; y < Board.LENGTH_VERTICAL; ++y)
			{
				final Position position = (Position) board.getPosition(x, y);

				if (position.hasPiece())
				{
					final Piece piece = (Piece) position.getPiece();
					final String tag = createTag(x, y);

					if (piece.isTypeNormal())
					{
						if (piece.isTeamPlayer())
						{
							((ImageButton) getView().findViewWithTag(tag)).setImageBitmap(playerNormal);
						}
						else
						{
							((ImageButton) getView().findViewWithTag(tag)).setImageBitmap(opponentNormal);
						}
					}
					else
					{
						if (piece.isTeamPlayer())
						{
							((ImageButton) getView().findViewWithTag(tag)).setImageBitmap(playerKing);
						}
						else
						{
							((ImageButton) getView().findViewWithTag(tag)).setImageBitmap(opponentKing);
						}
					}
				}
			}
		}
	}


}
