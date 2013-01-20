package edu.selu.android.classygames;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.utilities.Utilities;


public class GamesListFragment extends SherlockListFragment
{


	/**
	 * One of this class's callback methods. This is fired whenever one of the
	 * games in the user's list of games is clicked on.
	 */
	private OnGameSelectedListener onGameSelectedListener;

	public interface OnGameSelectedListener
	{
		public void onGameSelected(final Game game);
	}


	/**
	 * One of this class's callback methods. This is fired whenever the new
	 * game button in the action bar is clicked.
	 */
	private OnNewGameSelectedListener onNewGameSelectedListener;

	public interface OnNewGameSelectedListener
	{
		public void onNewGameSelected();
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
		return inflater.inflate(R.layout.games_list_fragment, container, false);
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
			onGameSelectedListener = (OnGameSelectedListener) activity;
			onNewGameSelectedListener = (OnNewGameSelectedListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement OnGameSelectedListener!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.games_list_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.games_list_fragment_actionbar_about:
				startActivity(new Intent(getSherlockActivity(), AboutActivity.class));
				break;

			case R.id.games_list_fragment_actionbar_new_game:
				// notify the parent Activity that the new game button in the
				// action bar has been clicked
				onNewGameSelectedListener.onNewGameSelected();
				break;

			case R.id.games_list_fragment_actionbar_refresh:
				Utilities.easyToast(getSherlockActivity(), "refresh!");
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onStart()
	{
		super.onStart();

		if (getFragmentManager().findFragmentById(R.id.central_fragment_activity_fragment_container) == null)
		// When in two-pane layout, set the ListView to highlight the selected
		// list item. This is done during onStart because at this point the
		// ListView is definitely available. Consult the Android Activity
		// Lifecycle to find out a bit more:
		// https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle
		{
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}


}
