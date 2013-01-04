package edu.selu.android.classygames;


import org.json.JSONArray;

import android.graphics.Bitmap;
import android.view.View;


public class ChessGameFragment extends GenericGameFragment
{


	/**
	 * Bitmap representing the player's pawn piece.
	 */
	private Bitmap playerPawn;


	/**
	 * Bitmap representing the player's bishop piece.
	 */
	private Bitmap playerBishop;


	/**
	 * Bitmap representing the player's knight piece.
	 */
	private Bitmap playerKnight;


	/**
	 * Bitmap representing the player's rook piece.
	 */
	private Bitmap playerRook;


	/**
	 * Bitmap representing the player's queen piece.
	 */
	private Bitmap playerQueen;


	/**
	 * Bitmap representing the player's king piece.
	 */
	private Bitmap playerKing;


	/**
	 * Bitmap representing the opponent's pawn piece.
	 */
	private Bitmap opponentPawn;


	/**
	 * Bitmap representing the opponent's bishop piece.
	 */
	private Bitmap opponentBishop;


	/**
	 * Bitmap representing the opponent's knight piece.
	 */
	private Bitmap opponentKnight;


	/**
	 * Bitmap representing the opponent's rook piece.
	 */
	private Bitmap opponentRook;


	/**
	 * Bitmap representing the opponent's queen piece.
	 */
	private Bitmap opponentQueen;


	/**
	 * Bitmap representing the opponent's king piece.
	 */
	private Bitmap opponentKing;


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{

	}


	@Override
	protected void initBoardNew()
	{

	}


	@Override
	protected void initViews()
	{

	}


	@Override
	protected void onBoardClick(final View v)
	{

	}


}
