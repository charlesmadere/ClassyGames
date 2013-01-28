package edu.selu.android.classygames;


import org.json.JSONArray;

import android.view.View;
import edu.selu.android.classygames.games.Position;


/**
 * This Fragment class is used when there is no game currently loaded.
 */
public class EmptyGameFragment extends GenericGameFragment
{


	EmptyGameFragment()
	{
		super();
	}


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
	{

	}


	@Override
	protected int getTitle()
	{
		return R.string.empty_game_fragment_title;
	}


	@Override
	protected void flush(final Position position)
	{

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

	}


	@Override
	protected int onCreateView()
	{
		return R.layout.empty_game_fragment;
	}


}
