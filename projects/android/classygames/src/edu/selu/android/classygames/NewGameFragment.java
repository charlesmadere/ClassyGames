package edu.selu.android.classygames;


import android.app.Activity;
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


	/**
	 * One of this class's callback methods. This is fired whenever this
	 * fragment's onDestroyView() method is called. The reason that we need
	 * this thing is mostly for hacky work around purposes: for some reason
	 * Android is not properly setting the title on the Action Bar whenever we
	 * exit one fragment for another. So the end result of this listener stuff
	 * is that the user presses back on their device (or on the Action Bar) and
	 * then instead of seeing "New Game" on the Action Bar, we'll see "Games
	 * List". Without this work around we'd see "New Game", even though we'd be
	 * currently viewing the GamesListFragment.
	 */
	private NewGameFragmentOnDestroyViewListener newGameFragmentOnDestroyViewListener;

	public interface NewGameFragmentOnDestroyViewListener
	{
		public void newGameFragmentOnDestroyViewListener();
	}




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
	public void onAttach(final Activity activity)
	// This makes sure that the Activity containing this Fragment has
	// implemented the callback interface. If the callback interface has not
	// been implemented, an exception is thrown.
	{
		super.onAttach(activity);

		try
		{
			newGameFragmentOnDestroyViewListener = (NewGameFragmentOnDestroyViewListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		// Typically when fragments have their own menu, Android will add that
		// fragment's menu items to the already existing menu. This can be good
		// but can also create issues. In this case we definitely need to get
		// that functionality out of here because the NewGameFragment and
		// GamesListFragment both have a refresh button... and each of them
		// do completely different things!
		menu.clear();

		inflater.inflate(R.menu.new_game_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		newGameFragmentOnDestroyViewListener.newGameFragmentOnDestroyViewListener();
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
