package edu.selu.android.classygames;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import edu.selu.android.classygames.games.Position;


/**
 * This Fragment class is used when there is no game currently loaded.
 */
public class EmptyGameFragment extends GenericGameFragment
{


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
		return null;
	}


	@Override
	protected int getGameView()
	{
		return R.layout.empty_game_fragment;
	}


	@Override
	protected int getLoadingText()
	{
		return R.string.generic_game_fragment_loading_text;
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


}
