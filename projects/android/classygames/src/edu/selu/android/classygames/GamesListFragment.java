package edu.selu.android.classygames;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;


public class GamesListFragment extends SherlockListFragment
{


	/**
	 * This class's callback method. This is fired whenever one of the games
	 * in the user's list of games is clicked on.
	 */
	private OnGameSelectedListener callback;


	public interface OnGameSelectedListener
	{
		public void onGameSelected(final int position);
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
			callback = (OnGameSelectedListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement OnGameSelectedListener!");
		}
	}


	@Override
	public void onStart()
	{
		super.onStart();

		if (getFragmentManager().findFragmentById(R.id.central_fragment_activity_fragment_empty_game_fragment) != null)
		// When in two-pane layout, set the ListView to highlight the selected
		// list item. This is done during onStart because at this point the
		// ListView is definitely available. Consult the Android Activity
		// Lifecycle to find out a bit more:
		// https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle
		{
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}


	@Override
	public void onListItemClick(final ListView l, final View v, final int position, final long id)
	{
		// notify the parent Activity that a game has been selected
		callback.onGameSelected(position);

		// set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}


}
