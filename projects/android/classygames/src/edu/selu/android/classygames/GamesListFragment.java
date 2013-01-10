package edu.selu.android.classygames;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;


public class GamesListFragment extends SherlockListFragment
{


	private OnGameSelectedListener callback;


	public interface OnGameSelectedListener
	{
		public void onGameSelected(final int position);
	}


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onStart()
	{
		super.onStart();

		if (getFragmentManager().findFragmentById(R.id.central_fragment_activity_fragment_game) != null)
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
	public void onListItemClick(final ListView l, final View v, final int position, final long id)
	{
		// notify the parent Activity that a game has been selected
		callback.onGameSelected(position);

		// set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{
			case R.id.games_list_fragment_activity_actionbar_about:
				
				return true;

			case R.id.games_list_fragment_activity_actionbar_new_game:
				
				return true;

			case R.id.games_list_fragment_activity_actionbar_refresh:
				
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
