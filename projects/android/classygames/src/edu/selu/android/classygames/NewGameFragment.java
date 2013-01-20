package edu.selu.android.classygames;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;


public class NewGameFragment extends SherlockListFragment
{


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		getSherlockActivity().invalidateOptionsMenu();
		getSherlockActivity().setTitle(R.string.new_game_fragment_title);

		return inflater.inflate(R.layout.new_game_fragment, container, false);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
