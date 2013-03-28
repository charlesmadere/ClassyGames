package com.charlesmadere.android.classygames;


import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.charlesmadere.android.classygames.games.Coordinate;
import com.charlesmadere.android.classygames.games.Position;
import com.charlesmadere.android.classygames.games.checkers.Board;
import com.charlesmadere.android.classygames.games.checkers.Piece;


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

		view.findViewById(R.id.checkers_game_fragment_x0y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y0).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y0).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y1).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y1).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y2).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y2).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y3).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y3).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y4).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y4).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y5).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y5).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y6).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y6).setOnClickListener(onBoardClick);

		view.findViewById(R.id.checkers_game_fragment_x0y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x1y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x2y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x3y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x4y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x5y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x6y7).setOnClickListener(onBoardClick);
		view.findViewById(R.id.checkers_game_fragment_x7y7).setOnClickListener(onBoardClick);

		final Resources resources = getResources();
		final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();

		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout()
			{
				final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				final View view = getView();

				if (view != null)
				{
					final View boardPosition = view.findViewById(R.id.checkers_game_fragment_x7y7);
	
					if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
					{
						final int width = boardPosition.getWidth();
						layoutParams.height = width;
	
						view.findViewById(R.id.checkers_game_fragment_y0).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y1).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y2).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y3).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y4).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y5).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y6).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_y7).setLayoutParams(layoutParams);
					}
					else
					{
						final int height = boardPosition.getHeight();
						layoutParams.width = height;
	
						view.findViewById(R.id.checkers_game_fragment_x0).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x1).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x2).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x3).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x4).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x5).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x6).setLayoutParams(layoutParams);
						view.findViewById(R.id.checkers_game_fragment_x7).setLayoutParams(layoutParams);
					}
	
					if (viewTreeObserver.isAlive())
					{
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
						{
							viewTreeObserver.removeOnGlobalLayoutListener(this);
						}
						else
						{
							viewTreeObserver.removeGlobalOnLayoutListener(this);
						}
					}
				}
			}
		});

		// Load Drawables for checkers pieces into memory. This is done so that
		// later when we draw these checkers pieces onto the board, that draw
		// process can be done very quickly as all of the picture data has
		// already been loaded.
		playerNormal = (BitmapDrawable) resources.getDrawable(R.drawable.piece_checkers_green_normal);
		playerKing = (BitmapDrawable) resources.getDrawable(R.drawable.piece_checkers_green_king);
		opponentNormal = (BitmapDrawable) resources.getDrawable(R.drawable.piece_checkers_orange_normal);
		opponentKing = (BitmapDrawable) resources.getDrawable(R.drawable.piece_checkers_orange_king);
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
