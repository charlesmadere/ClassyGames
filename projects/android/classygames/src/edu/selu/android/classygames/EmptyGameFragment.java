package edu.selu.android.classygames;


import org.json.JSONArray;

import android.view.View;
import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.data.Person;


/**
 * This Fragment class is used when there is no game currently loaded.
 */
public class EmptyGameFragment extends GenericGameFragment
{


	EmptyGameFragment(final Person person)
	{
		super(person);
	}


	EmptyGameFragment(final Game game)
	{
		super(game);
	}


	@Override
	protected void buildTeam(final JSONArray team, final byte whichTeam)
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
