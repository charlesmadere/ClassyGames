package edu.selu.android.classygames;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class FriendsListFragment extends SherlockListFragment
{


	/**
	 * Boolean that marks if this is the first time that the onResume() method
	 * was hit.
	 */
	private boolean isFirstOnResume = true;


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
	 * One of this class's callback methods. This is fired whenever the use
	 * has selected a friend in their friends list.
	 */
	private FriendsListFragmentOnFriendSelectedListener friendsListFragmentOnFriendSelectedListener;

	public interface FriendsListFragmentOnFriendSelectedListener
	{
		public void friendsListFragmentOnFriendSelected(final Person friend);
	}


	/**
	 * One of this class's callback methods. This is fired whenever the user
	 * has selected the Refresh button on the action bar.
	 */
	private FriendsListFragmentOnRefreshSelectedListener friendsListFragmentOnRefreshSelectedListener;

	public interface FriendsListFragmentOnRefreshSelectedListener
	{
		public void friendsListFragmentOnRefreshSelected();
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
		return inflater.inflate(R.layout.friends_list_fragment, container, false);
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
			friendsListFragmentOnFriendSelectedListener = (FriendsListFragmentOnFriendSelectedListener) activity;
			friendsListFragmentOnRefreshSelectedListener = (FriendsListFragmentOnRefreshSelectedListener) activity;
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
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.generic_cancel_menu_cancel:
				if (isAsyncRefreshFriendsListRunning)
				{
					asyncRefreshFriendsList.cancel(true);
				}
				break;

			case R.id.generic_refresh_menu_refresh:
				friendsListFragmentOnRefreshSelectedListener.friendsListFragmentOnRefreshSelected();
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
		actionBar.setTitle(R.string.friends_list_fragment_title);

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
			asyncRefreshFriendsList = new AsyncRefreshFriendsList(getSherlockActivity(), getLayoutInflater(getArguments()), Session.getActiveSession(), (ViewGroup) getView());
			asyncRefreshFriendsList.execute();
		}
	}




	private final class AsyncRefreshFriendsList extends AsyncTask<Void, Void, ArrayList<Person>>
	{


		private Context context;
		private LayoutInflater inflater;
		private Session session;
		private ViewGroup viewGroup;


		AsyncRefreshFriendsList(final Context context, final LayoutInflater inflater, final Session session, final ViewGroup viewGroup)
		{
			this.context = context;
			this.inflater = inflater;
			this.session = session;
			this.viewGroup = viewGroup;
		}


		@Override
		protected ArrayList<Person> doInBackground(final Void... params)
		{
			final ArrayList<Person> friends = new ArrayList<Person>();

			if (!isCancelled())
			{
				Request.newMyFriendsRequest(session, new GraphUserListCallback()
				{
					@Override
					public void onCompleted(final List<GraphUser> users, final Response response)
					{
						for (int i = 0; i < users.size() && !isCancelled(); ++i)
						{
							final GraphUser user = users.get(i);
							final String id = user.getId();
							final String name = user.getName();

							if (Person.isIdValid(id) && Person.isNameValid(name))
							{
								final Person friend = new Person(id, name);
								friends.add(friend);
							}
						}

						Collections.sort(friends, new FriendsListSorter());
					}
				}).executeAndWait();
			}

			return friends;
		}


		@Override
		protected void onCancelled(final ArrayList<Person> friends)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.friends_list_fragment_cancelled, viewGroup);

			isAsyncRefreshFriendsListRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPostExecute(final ArrayList<Person> friends)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.friends_list_fragment, viewGroup);

			friendsListAdapter = new FriendsListAdapter(context, R.layout.friends_list_fragment_listview_item, friends);
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
			inflater.inflate(R.layout.friends_list_fragment_loading, viewGroup);
		}


	}




	private final class FriendsListAdapter extends ArrayAdapter<Person>
	{


		private ArrayList<Person> friends;
		private Context context;
		private Drawable emptyProfilePicture;


		FriendsListAdapter(final Context context, final int textViewResourceId, final ArrayList<Person> friends)
		{
			super(context, textViewResourceId, friends);
			this.friends = friends;
			this.context = context;

			emptyProfilePicture = (Drawable) context.getResources().getDrawable(R.drawable.empty_profile_picture_small);
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.friends_list_fragment_listview_item, null);

				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.friends_list_fragment_listview_item_name);
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.friends_list_fragment_listview_item_picture);

				convertView.setTag(viewHolder);
			}

			final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			final Person friend = friends.get(position);
			viewHolder.name.setText(friend.getName());
			viewHolder.name.setTypeface(Utilities.TypefaceUtilities.getTypeface(context.getAssets(), Utilities.TypefaceUtilities.BLUE_HIGHWAY_D));
			viewHolder.picture.setImageDrawable(emptyProfilePicture);
			Utilities.getImageLoader(context).displayImage(Utilities.FacebookUtilities.GRAPH_API_URL + friend.getId() + Utilities.FacebookUtilities.GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL, viewHolder.picture);

			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(final View v)
				{
					friendsListFragmentOnFriendSelectedListener.friendsListFragmentOnFriendSelected(friend);
				}
			});

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
