package edu.selu.android.classygames;


import org.json.JSONArray;

import edu.selu.android.classygames.games.chess.Board;

import android.view.View;


public class ChessGameFragment extends GenericGameFragment
{


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{

	}


	@Override
	protected void initBoardNew()
	{
		board = new Board();
	}


	@Override
	protected void initBoardOld()
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
