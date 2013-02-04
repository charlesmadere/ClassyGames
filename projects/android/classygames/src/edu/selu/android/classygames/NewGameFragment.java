package edu.selu.android.classygames;


import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class NewGameFragment extends SherlockListFragment
{


	/**
	 * Boolean that marks if this is the first time that the onResume() method
	 * was hit.
	 */
	private boolean isFirstOnResume = false;


	/**
	 * Variables that holds whether or not the asyncRefreshFriendsList
	 * AsyncTask is currently running.
	 */
	private boolean isAsyncRefreshFriendsListRunning = false;


	/**
	 * Holds a handle to the currently running (if it's currently running)
	 * AsyncRefreshFriendsList AsyncTask.
	 */
	private AsyncRefreshFriendsList asyncRefreshFriendsList;


	/**
	 * List Adapter for this Fragment's ListView layout item.
	 */
	private FriendsListAdapter friendsListAdapter;




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
		public void newGameFragmentOnDestroyView();
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

		if (isAsyncRefreshFriendsListRunning)
		{
			inflater.inflate(R.menu.generic_cancel, menu);
		}
		else
		{
			inflater.inflate(R.menu.generic_refresh, menu);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		newGameFragmentOnDestroyViewListener.newGameFragmentOnDestroyView();
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

			case R.id.generic_cancel_menu_cancel:
				if (isAsyncRefreshFriendsListRunning)
				{
					asyncRefreshFriendsList.cancel(true);
				}
				break;

			case R.id.generic_refresh_menu_refresh:
				refreshFriendsList();
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

		if (isFirstOnResume)
		{
			isFirstOnResume = false;
			refreshFriendsList();
		}
	}




	/**
	 * Invalidates the options menu using the Android compatibility library.
	 */
	private void compatInvalidateOptionsMenu()
	{
		ActivityCompat.invalidateOptionsMenu(getSherlockActivity());
	}


	/**
	 * Refreshes the friends list if a refresh is not already running.
	 */
	private void refreshFriendsList()
	{
		if (!isAsyncRefreshFriendsListRunning)
		{
			asyncRefreshFriendsList = new AsyncRefreshFriendsList(getSherlockActivity(), getLayoutInflater(getArguments()), (ViewGroup) getView());
			asyncRefreshFriendsList.execute();
		}
	}




	private final class AsyncRefreshFriendsList extends AsyncTask<Void, Void, ArrayList<Person>>
	{


		private Context context;
		private LayoutInflater inflater;
		private ViewGroup viewGroup;


		AsyncRefreshFriendsList(final Context context, final LayoutInflater inflater, final ViewGroup viewGroup)
		{
			this.context = context;
			this.inflater = inflater;
			this.viewGroup = viewGroup;
		}


		@Override
		protected ArrayList<Person> doInBackground(final Void... params)
		{
			ArrayList<Person> friends = null;

			if (!isCancelled())
			{

			}

			return friends;
		}


		@Override
		protected void onCancelled(final ArrayList<Person> friends)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.new_game_fragment_cancelled, viewGroup);

			isAsyncRefreshFriendsListRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPostExecute(final ArrayList<Person> friends)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.new_game_fragment, viewGroup);

			friendsListAdapter = new FriendsListAdapter(context, R.layout.new_game_fragment_listview_item, friends);
			final ListView listView = (ListView) viewGroup.findViewById(android.R.id.list);
			listView.setAdapter(friendsListAdapter);

			isAsyncRefreshFriendsListRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncRefreshFriendsListRunning = true;
			compatInvalidateOptionsMenu();

			viewGroup.removeAllViews();
			inflater.inflate(R.layout.new_game_fragment_loading, viewGroup);
		}


	}




	private final class FriendsListAdapter extends ArrayAdapter<Person>
	{


		private ArrayList<Person> friends;
		private Context context;


		FriendsListAdapter(final Context context, final int textViewResourceId, final ArrayList<Person> friends)
		{
			super(context, textViewResourceId, friends);
			this.friends = friends;
			this.context = context;
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.new_game_fragment_listview_item, null);

				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.new_game_fragment_listview_item_name);
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.new_game_fragment_listview_item_picture);

				convertView.setTag(viewHolder);
			}

			final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			final Person friend = friends.get(position);
			viewHolder.name.setText(friend.getName());
			Utilities.getImageLoader(context).displayImage(Utilities.FACEBOOK_GRAPH_API_URL + friend.getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL, viewHolder.picture);

			return convertView;
		}


	}




	private final static class ViewHolder
	{


		ImageView picture;
		TextView name;


	}




	private final class FriendsListSorter implements Comparator<Person>
	{
		@Override
		public int compare(final Person geo, final Person jarrad)
		{
			return geo.getName().compareToIgnoreCase(jarrad.getName());
		}
	}




}
