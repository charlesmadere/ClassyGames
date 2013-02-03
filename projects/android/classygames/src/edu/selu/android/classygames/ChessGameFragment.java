package edu.selu.android.classygames;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import edu.selu.android.classygames.games.Position;
import edu.selu.android.classygames.games.chess.Piece;


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




	@Override
	protected void onCreateView()
	{

	}


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{

	}


	@Override
	protected JSONObject createJSONPiece(final byte whichTeam, final Position position) throws JSONException
	{
		JSONObject JSONPiece = null;

		return JSONPiece;
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
					imageButton.setImageDrawable(playerPawn);
				else
					imageButton.setImageDrawable(opponentPawn);
				break;

			case Piece.TYPE_BISHOP:
				if (piece.isTeamPlayer())
					imageButton.setImageDrawable(playerBishop);
				else
					imageButton.setImageDrawable(opponentBishop);
				break;

			case Piece.TYPE_KNIGHT:
				if (piece.isTeamPlayer())
					imageButton.setImageDrawable(playerKnight);
				else
					imageButton.setImageDrawable(opponentKnight);
				break;

			case Piece.TYPE_ROOK:
				if (piece.isTeamPlayer())
					imageButton.setImageDrawable(playerRook);
				else
					imageButton.setImageDrawable(opponentRook);
				break;

			case Piece.TYPE_QUEEN:
				if (piece.isTeamPlayer())
					imageButton.setImageDrawable(playerQueen);
				else
					imageButton.setImageDrawable(opponentQueen);
				break;

			case Piece.TYPE_KING:
				if (piece.isTeamPlayer())
					imageButton.setImageDrawable(playerKing);
				else
					imageButton.setImageDrawable(opponentKing);
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
