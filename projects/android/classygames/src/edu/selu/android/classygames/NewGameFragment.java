package edu.selu.android.classygames;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.utilities.Utilities;


public class NewGameFragment extends SherlockListFragment
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.new_game_fragment, container, false);
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.new_game_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(R.string.central_fragment_activity_title);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				final FragmentManager fManager = getSherlockActivity().getSupportFragmentManager();
				fManager.popBackStack();

				final FragmentTransaction fTransaction = fManager.beginTransaction();
				fTransaction.remove(this);
				fTransaction.commit();
				break;

			case R.id.new_game_fragment_actionbar_refresh:
				Utilities.easyToast(getSherlockActivity(), "other arrow");
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onResume()
	{
		super.onResume();

		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.new_game_fragment_title);
	}


}
