package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.charlesmadere.android.classygames.models.ListItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import java.util.*;


public final class FriendsListFragment extends SherlockListFragment implements
	AdapterView.OnItemClickListener
{


	private final static String PREFERENCES_NAME = "FriendsListFragment_Preferences";




	private ListView list;
	private TextView empty;
	private LinearLayout loading;
	private TextView noInternetConnection;


	/**
	 * Boolean that marks if this is the first time that the onResume() method
	 * was hit. We do this because we don't want to refresh the friends list
	 * if it is not in need of refreshing.
	 * In other words, it's not in need of a refreshment. It's not thirsty. Har
	 * har.
	 */
	private boolean isFirstOnResume = true;


	/**
	 * Holds a handle to the currently running (if it's currently running)
	 * AsyncRefreshFriendsList AsyncTask.
	 */
	private AsyncRefreshFriendsList asyncRefreshFriendsList;


	/**
	 * If the user has selected a friend in their friends list, then this will
	 * be a handle to that Friend object. If no friend is currently selected,
	 * then this will be null.
	 */
	private ListItem<Person> selectedFriend;


	private SharedPreferences sPreferences;




	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * Listeners interface.
	 */
	private Listeners listeners;


	/**
	 * A bunch of listener methods for this Fragment.
	 */
	public interface Listeners
	{


		/**
		 * This is fired whenever the user has selected a friend in their
		 * friends list.
		 * 
		 * @param friend
		 * The Facebook friend that the user selected.
		 */
		public void onFriendSelected(final Person friend);


		/**
		 * This is fired whenever the user has selected the Refresh button on
		 * the action bar.
		 */
		public void onRefreshSelected();


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
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final View view = getView();
		list = getListView();
		list.setOnItemClickListener(this);
		empty = (TextView) view.findViewById(android.R.id.empty);
		loading = (LinearLayout) view.findViewById(R.id.friends_list_fragment_loading);
		noInternetConnection = (TextView) view.findViewById(R.id.fragment_no_internet_connection);
	}


	@Override
	public void onAttach(final Activity activity)
	// This makes sure that the Activity containing this Fragment has
	// implemented the callback interface. If the callback interface has not
	// been implemented, an exception is thrown.
	{
		super.onAttach(activity);
		listeners = (Listeners) activity;
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.friends_list_fragment, menu);

		if (!isAnAsyncTaskRunning())
		{
			final MenuItem refreshMenuItem = menu.findItem(R.id.friends_list_fragment_menu_refresh);
			refreshMenuItem.setVisible(true);

			final MenuItem searchMenuItem = menu.findItem(R.id.friends_list_fragment_menu_search);
			searchMenuItem.setVisible(true);

			final SearchView searchView = (SearchView) searchMenuItem.getActionView();
			searchView.setQueryHint(getString(R.string.search_friends));
			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
			{
				@Override
				public boolean onQueryTextChange(final String newText)
				{
					if (list != null)
					{
						final FriendsListAdapter adapter = (FriendsListAdapter) list.getAdapter();

						if (adapter != null)
						{
							adapter.getFilter().filter(newText);
						}
					}

					return true;
				}


				@Override
				public boolean onQueryTextSubmit(final String query)
				{
					return true;
				}
			});

			searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener()
			{
				@Override
				public boolean onMenuItemActionCollapse(final MenuItem item)
				{
					searchView.setQuery(null, true);
					return true;
				}


				@Override
				public boolean onMenuItemActionExpand(final MenuItem item)
				{
					return true;
				}
			});
		}

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		cancelRunningAnyAsyncTask();
		super.onDestroyView();
	}


	@Override
	public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id)
	{
		@SuppressWarnings("unchecked")
		final ListItem<Person> friend = (ListItem<Person>) parent.getItemAtPosition(position);

		if (!friend.isSelected())
		{
			listeners.onFriendSelected(friend.get());
			selectedFriend = friend;
			selectedFriend.select();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				view.setActivated(true);
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.friends_list_fragment_menu_refresh:
				if (!isAnAsyncTaskRunning())
				{
					empty.setText(R.string.friends_list_fragment_no_friends);
					getPreferences().edit().clear().commit();
					listeners.onRefreshSelected();
					refreshFriendsList();
				}
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

		if (isFirstOnResume)
		{
			isFirstOnResume = false;
			refreshFriendsList();
		}
	}




	/**
	 * Cancels the currently running AsyncTask (if any).
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			asyncRefreshFriendsList.cancel(true);
		}
	}


	private void deselectFriend()
	{
		if (selectedFriend != null)
		{
			selectedFriend.unselect();
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			for (int i = 0; i < list.getChildCount(); ++i)
			{
				final View view = list.getChildAt(i);
				view.setActivated(false);
			}
		}
	}


	private SharedPreferences getPreferences()
	{
		if (sPreferences == null)
		{
			sPreferences = getSherlockActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		}

		return sPreferences;
	}


	/**
	 * @return
	 * Returns true if the asyncRefreshFriendsList AsyncTask is currently
	 * running.
	 */
	private boolean isAnAsyncTaskRunning()
	{
		return asyncRefreshFriendsList != null;
	}


	public boolean onBackPressed()
	{
		if (isAnAsyncTaskRunning())
		{
			cancelRunningAnyAsyncTask();
		}
		else
		{
			deselectFriend();
		}

		return false;
	}


	/**
	 * Refreshes the friends list if a refresh is not already running.
	 */
	public void refreshFriendsList()
	{
		if (!isAnAsyncTaskRunning())
		{
			asyncRefreshFriendsList = new AsyncRefreshFriendsList();
			asyncRefreshFriendsList.execute();
		}
	}




	private final class AsyncRefreshFriendsList extends AsyncTask<Void, Void, LinkedList<ListItem<Person>>>
		implements Comparator<ListItem<Person>>
	{


		private final static byte RUN_STATUS_NORMAL = 1;
		private final static byte RUN_STATUS_NO_NETWORK_CONNECTION = 2;
		private byte runStatus;


		private SherlockFragmentActivity fragmentActivity;


		private AsyncRefreshFriendsList()
		{
			fragmentActivity = getSherlockActivity();
			runStatus = RUN_STATUS_NORMAL;
		}


		@Override
		protected LinkedList<ListItem<Person>> doInBackground(final Void... params)
		{
			final LinkedList<ListItem<Person>> friends = new LinkedList<ListItem<Person>>();

			if (!isCancelled())
			{
				@SuppressWarnings("unchecked")
				final Map<String, String> map = (Map<String, String>) getPreferences().getAll();

				if (map == null || map.isEmpty())
				{
					if (Utilities.checkForNetworkConnectivity(fragmentActivity))
					{
						final Session session = Session.getActiveSession();

						Request.newMyFriendsRequest(session, new GraphUserListCallback()
						{
							@Override
							public void onCompleted(final List<GraphUser> users, final Response response)
							{
								if (users != null && !users.isEmpty())
								{
									for (int i = 0; i < users.size() && !isCancelled(); ++i)
									{
										final GraphUser user = users.get(i);
										final String id = user.getId();
										final String name = user.getName();

										final Person friend = addFriend(id, name);

										if (friend != null)
										{
											final ListItem<Person> listItem = new ListItem<Person>(friend);
											friends.add(listItem);
										}
									}
								}

								final SharedPreferences.Editor editor = getPreferences().edit();
								editor.clear();

								for (int i = 0; i < friends.size() && !isCancelled(); ++i)
								{
									final Person friend = friends.get(i).get();
									editor.putString(String.valueOf(friend.getId()), friend.getName());
								}

								// http://stackoverflow.com/questions/5960678/whats-the-difference-between-commit-and-apply
								// Basically, commit() is slower than apply() because it synchronously writes these
								// SharedPreferences changes to storage. Because we're potentially writing a bunch of data
								// to SharedPreferences right now (the user's whole list of Facebook friends!) we can get a
								// potentially pretty decent speed boost out of this.

								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
								{
									editor.apply();
								}
								else
								{
									editor.commit();
								}
							}
						}).executeAndWait();
					}
					else
					{
						runStatus = RUN_STATUS_NO_NETWORK_CONNECTION;
					}
				}
				else
				{
					final Set<String> set = map.keySet();

					for (final Iterator<String> i = set.iterator(); i.hasNext() && !isCancelled(); )
					{
						final String id = i.next();
						final String name = map.get(id);

						final Person friend = addFriend(id, name);

						if (friend != null)
						{
							final ListItem<Person> listItem = new ListItem<Person>(friend);
							friends.add(listItem);
						}
					}
				}
			}

			// sorts the list of friends using the Comparator method found in
			// this class
			Collections.sort(friends, this);

			return friends;
		}


		private void cancelled()
		{
			setRunningState(false);

			final SharedPreferences.Editor editor = getPreferences().edit();
			editor.clear();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			{
				editor.apply();
			}
			else
			{
				editor.commit();
			}
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final LinkedList<ListItem<Person>> friends)
		{
			cancelled();
		}


		@Override
		public int compare(final ListItem<Person> geo, final ListItem<Person> jarrad)
		{
			return geo.get().getName().compareToIgnoreCase(jarrad.get().getName());
		}


		@Override
		protected void onPostExecute(final LinkedList<ListItem<Person>> friends)
		{
			if (runStatus == RUN_STATUS_NORMAL && friends != null && !friends.isEmpty())
			{
				final FriendsListAdapter adapter = new FriendsListAdapter(friends);
				list.setAdapter(adapter);
				list.setVisibility(View.VISIBLE);
				empty.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				noInternetConnection.setVisibility(View.GONE);
			}
			else if (runStatus == RUN_STATUS_NO_NETWORK_CONNECTION)
			{
				list.setVisibility(View.GONE);
				empty.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				noInternetConnection.setVisibility(View.VISIBLE);
			}
			else
			{
				list.setVisibility(View.GONE);
				empty.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
				noInternetConnection.setVisibility(View.GONE);
			}

			setRunningState(false);
		}


		@Override
		protected void onPreExecute()
		{
			setRunningState(true);

			list.setVisibility(View.GONE);
			empty.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			noInternetConnection.setVisibility(View.GONE);
		}


		/**
		 * Creates a Person object out of the given data (if the given data is
		 * valid).
		 *
		 * @param id
		 * The friend's Facebook ID.
		 *
		 * @param name
		 * The friend's Facebook name.
		 *
		 * @return
		 * Returns a Person object representing the given Facebook friend. Has
		 * the possibility of returning null if the given data is invalid.
		 */
		private Person addFriend(final String id, final String name)
		{
			Person friend = null;

			if (Person.isIdValid(id) && Person.isNameValid(name))
			{
				friend = new Person(id, name);
			}

			return friend;
		}


		/**
		 * Use this method to reset the options menu. This should only be used when
		 * an AsyncTask has either just begun or has just ended.
		 * 
		 * @param isRunning
		 * True if the AsyncTask is just starting to run, false if it's just
		 * finished.
		 */
		private void setRunningState(final boolean isRunning)
		{
			if (!isRunning)
			{
				asyncRefreshFriendsList = null;
			}

			fragmentActivity.supportInvalidateOptionsMenu();
		}


	}




	private final class FriendsListAdapter extends BaseAdapter implements Filterable
	{


		private Activity activity;
		private Drawable emptyProfilePicture;
		private Filter filter;
		private LayoutInflater inflater;
		private LinkedList<ListItem<Person>> friends;


		private FriendsListAdapter(final LinkedList<ListItem<Person>> friends)
		{
			this.friends = friends;

			activity = getSherlockActivity();
			inflater = activity.getLayoutInflater();
			emptyProfilePicture = getResources().getDrawable(R.drawable.empty_profile_picture_small);
			filter = new FriendsListFilter(friends);
		}


		@Override
		public int getCount()
		{
			return friends.size();
		}


		@Override
		public Filter getFilter()
		{
			return filter;
		}


		@Override
		public ListItem getItem(int position)
		{
			return friends.get(position);
		}


		@Override
		public long getItemId(final int position)
		{
			return position;
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			final ListItem<Person> listItem = friends.get(position);
			final Person friend = listItem.get();

			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.friends_list_fragment_listview_item, null);
				final ViewHolder viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}

			final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.name.setText(friend.getName());
			viewHolder.picture.setImageDrawable(emptyProfilePicture);
			final String friendsPictureURL = FacebookUtilities.getFriendsPictureSquare(activity, friend.getId());
			Utilities.getImageLoader().displayImage(friendsPictureURL, viewHolder.picture);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				if (listItem.isSelected())
				{
					convertView.setActivated(true);
				}
				else
				{
					convertView.setActivated(false);
				}
			}

			return convertView;
		}




		/**
		 * This class performs the actual filtering of the friends in the
		 * friends list.
		 */
		private final class FriendsListFilter extends Filter
		{


			private LinkedList<ListItem<Person>> friendsCopy;
			private String previousQuery;


			private FriendsListFilter(final LinkedList<ListItem<Person>> friends)
			{
				friendsCopy = new LinkedList<ListItem<Person>>(friends);
			}


			@Override
			protected FilterResults performFiltering(final CharSequence constraint)
			// The CharSequence constraint variable is the actual text that the
			// user is searching for.
			{
				final FilterResults filterResults = new FilterResults();

				if (constraint == null || constraint.length() < 1)
				// Check to see if the text that the user searched for is null
				// or empty. If either of these checks prove true, then we know
				// that the user wants to clear their search, which means that
				// they want to see their entire, unfiltered, friends list.
				{
					if (friends.size() != friendsCopy.size())
					// The friendsCopy object contains the original unsorted
					// list of friends. This check compares the sizes of the
					// two lists, if they are the same size then we know that
					// the friends list has not been altered / filtered and
					// this means that we don't need to waste the performance
					// power needed to create a copy of the original list.
					{
						// Creates a copy of the original, unaltered friends
						// list.
						friends = new LinkedList<ListItem<Person>>(friendsCopy);
					}

					filterResults.count = friendsCopy.size();
					filterResults.values = friendsCopy;
					previousQuery = null;
				}
				else
				{
					// The friends that are found to contain the text that the
					// user has searched for will be placed in this LinkedList.
					final LinkedList<ListItem<Person>> filteredFriends = new LinkedList<ListItem<Person>>();

					// Grab a String version of the user's search text and
					// convert it to lower case form.
					final String query = constraint.toString().toLowerCase();

					if (previousQuery != null && previousQuery.length() > query.length())
					// Check the previous query and see if it was a bigger
					// String than the current query. This is needed because if
					// the user entered "Ka", but then deleted it down to just
					// "K", the friends list would only continue to filter
					// based on the results found from the "Ka" search.
					{
						friends = new LinkedList<ListItem<Person>>(friendsCopy);
					}

					// store the user's current search query
					previousQuery = query;

					for (final ListItem<Person> friend : friends)
					// search through every friend in the list of friends
					{
						final String name = friend.get().getName().toLowerCase();

						if (name.contains(query))
						{
							// This friend's name was found to contain the text
							// that the user is searching for. Add this friend
							// to the list of filtered friends.
							filteredFriends.add(friend);
						}
					}

					filterResults.count = filteredFriends.size();
					filterResults.values = filteredFriends;
				}

				return filterResults;
			}


			@Override
			protected void publishResults(final CharSequence constraint, final FilterResults results)
			{
				// Clear the list of friends that is currently being shown on
				// the screen.
				friends.clear();

				@SuppressWarnings("unchecked")
				final LinkedList<ListItem<Person>> values = (LinkedList<ListItem<Person>>) results.values;

				// Add the list of filtered friends to the newly cleared list.
				friends.addAll(values);

				if (friends.isEmpty())
				{
					empty.setText(R.string.no_friends_found);
				}
				else
				{
					empty.setText(R.string.friends_list_fragment_no_friends);
				}

				// This refreshes the friends list as shown on the device's
				// screen.
				notifyDataSetChanged();
			}




		}




		private final class ViewHolder
		{


			private ImageView picture;
			private TextView name;


			private ViewHolder(final View view)
			{
				picture = (ImageView) view.findViewById(R.id.friends_list_fragment_listview_item_picture);
				name = (TextView) view.findViewById(R.id.friends_list_fragment_listview_item_name);
			}


		}




	}




}
