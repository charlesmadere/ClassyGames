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
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.*;


public final class FriendsListFragment extends SherlockListFragment implements
	OnItemClickListener
{


	private final static String PREFERENCES_NAME = "FriendsListFragment_Preferences";




	private ListView list;
	private TextView empty;
	private LinearLayout loading;
	private TextView cancelledLoading;
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
	 * List Adapter for this Fragment's ListView layout item.
	 */
	private FriendsListAdapter friendsListAdapter;


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
		cancelledLoading = (TextView) view.findViewById(R.id.friends_list_fragment_cancelled_loading);
		noInternetConnection = (TextView) view.findViewById(R.id.fragment_no_internet_connection);
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
			listeners = (Listeners) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && isAnAsyncTaskRunning())
		{
			inflater.inflate(R.menu.generic_cancel, menu);
		}
		else
		{
			inflater.inflate(R.menu.friends_list_fragment, menu);

			final MenuItem searchMenuItem = menu.findItem(R.id.friends_list_fragment_menu_search);
			final SearchView searchView = (SearchView) searchMenuItem.getActionView();
			searchView.setQueryHint(getString(R.string.search_friends));

			searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
			{
				@Override
				public boolean onQueryTextChange(final String newText)
				{
					if (friendsListAdapter != null)
					{
						friendsListAdapter.getFilter().filter(newText);
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
	public void onItemClick(final AdapterView<?> l, final View v, final int position, final long id)
	{
		final Person friend = (Person) l.getItemAtPosition(position);
		listeners.onFriendSelected(friend);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.generic_cancel_menu_cancel:
				if (isAnAsyncTaskRunning())
				{
					asyncRefreshFriendsList.cancel(true);
				}
				break;

			case R.id.friends_list_fragment_menu_refresh:
				final SharedPreferences.Editor editor = getPreferences().edit();
				editor.clear();
				editor.commit();

				listeners.onRefreshSelected();
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
	public void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			asyncRefreshFriendsList.cancel(true);
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
	public boolean isAnAsyncTaskRunning()
	{
		return asyncRefreshFriendsList != null;
	}


	/**
	 * Refreshes the friends list if a refresh is not already running.
	 */
	public void refreshFriendsList()
	{
		if (!isAnAsyncTaskRunning())
		{
			asyncRefreshFriendsList = new AsyncRefreshFriendsList(getSherlockActivity());
			asyncRefreshFriendsList.execute();
		}
	}




	private final class AsyncRefreshFriendsList extends AsyncTask<Void, Void, ArrayList<Person>>
		implements Comparator<Person>
	{


		private final static byte RUN_STATUS_NORMAL = 1;
		private final static byte RUN_STATUS_NO_NETWORK_CONNECTION = 3;
		private byte runStatus;


		private SharedPreferences sPreferences;
		private SherlockFragmentActivity fragmentActivity;
		private Session session;


		private AsyncRefreshFriendsList(final SherlockFragmentActivity fragmentActivity)
		{
			this.fragmentActivity = fragmentActivity;
			sPreferences = getPreferences();
			session = Session.getActiveSession();
			runStatus = RUN_STATUS_NORMAL;
		}


		@Override
		protected ArrayList<Person> doInBackground(final Void... params)
		{
			final ArrayList<Person> friends = new ArrayList<Person>();

			if (!isCancelled())
			{
				@SuppressWarnings("unchecked")
				final Map<String, String> map = (Map<String, String>) sPreferences.getAll();

				if (map == null || map.isEmpty())
				{
					if (Utilities.checkForNetworkConnectivity(fragmentActivity))
					{
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
											friends.add(friend);
										}
									}

									friends.trimToSize();
								}

								final SharedPreferences.Editor editor = sPreferences.edit();
								editor.clear();

								for (int i = 0; i < friends.size() && !isCancelled(); ++i)
								{
									final Person friend = friends.get(i);
									editor.putString(friend.getIdAsString(), friend.getName());
								}

								editor.commit();
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
							friends.add(friend);
						}
					}

					friends.trimToSize();
				}
			}

			// sorts the list of friends using the Comparator method found in
			// this class
			Collections.sort(friends, this);

			return friends;
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


		private void cancelled()
		{
			list.setVisibility(View.GONE);
			empty.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			cancelledLoading.setVisibility(View.VISIBLE);
			noInternetConnection.setVisibility(View.GONE);

			setRunningState(false);
		}


		@Override
		public int compare(final Person geo, final Person jarrad)
		{
			return geo.getName().compareToIgnoreCase(jarrad.getName());
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final ArrayList<Person> friends)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final ArrayList<Person> friends)
		{
			if (runStatus == RUN_STATUS_NORMAL && friends != null && !friends.isEmpty())
			{
				friendsListAdapter = new FriendsListAdapter(fragmentActivity, friends);
				list.setAdapter(friendsListAdapter);

				list.setVisibility(View.VISIBLE);
				empty.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				cancelledLoading.setVisibility(View.GONE);
				noInternetConnection.setVisibility(View.GONE);
			}
			else if (runStatus == RUN_STATUS_NO_NETWORK_CONNECTION)
			{
				list.setVisibility(View.GONE);
				empty.setVisibility(View.GONE);
				loading.setVisibility(View.GONE);
				cancelledLoading.setVisibility(View.GONE);
				noInternetConnection.setVisibility(View.VISIBLE);
			}
			else
			{
				list.setVisibility(View.GONE);
				empty.setVisibility(View.VISIBLE);
				loading.setVisibility(View.GONE);
				cancelledLoading.setVisibility(View.GONE);
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
			cancelledLoading.setVisibility(View.GONE);
			noInternetConnection.setVisibility(View.GONE);
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

			Utilities.compatInvalidateOptionsMenu(fragmentActivity, true);
		}


	}




	private final class FriendsListAdapter extends ArrayAdapter<Person> implements Filterable
	{


		private ArrayList<Person> friends;
		private Context context;
		private Drawable emptyProfilePicture;
		private Filter filter;
		private ImageLoader imageLoader;
		private LayoutInflater inflater;


		private FriendsListAdapter(final Context context, final ArrayList<Person> friends)
		{
			super(context, R.layout.friends_list_fragment_listview_item, friends);
			this.friends = friends;
			this.context = context;

			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			emptyProfilePicture = context.getResources().getDrawable(R.drawable.empty_profile_picture_small);
			filter = new FriendsListFilter(friends);
			imageLoader = Utilities.getImageLoader(context);
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
		public Person getItem(int position)
		{
			return friends.get(position);
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.friends_list_fragment_listview_item, null);

				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.friends_list_fragment_listview_item_name);
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.friends_list_fragment_listview_item_picture);

				convertView.setTag(viewHolder);
			}

			final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			final Person friend = friends.get(position);
			viewHolder.name.setText(friend.getName());
			TypefaceUtilities.applyTypefaceBlueHighway(context.getAssets(), viewHolder.name);
			viewHolder.picture.setImageDrawable(emptyProfilePicture);
			imageLoader.displayImage(FacebookUtilities.getFriendsPictureSquare(context, friend.getId()), viewHolder.picture);

			return convertView;
		}




		/**
		 * This class performs the actual filtering of the friends in the
		 * friends list.
		 */
		private final class FriendsListFilter extends Filter
		{


			private ArrayList<Person> friendsCopy;
			private String previousQuery;


			private FriendsListFilter(final ArrayList<Person> friends)
			{
				friendsCopy = new ArrayList<Person>(friends);
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
						friends = new ArrayList<Person>(friendsCopy);
					}

					filterResults.count = friendsCopy.size();
					filterResults.values = friendsCopy;
					previousQuery = null;
				}
				else
				{
					// The friends that are found to contain the text that the
					// user has searched for will be placed in this ArrayList.
					final ArrayList<Person> filteredFriends = new ArrayList<Person>();

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
						friends = new ArrayList<Person>(friendsCopy);
					}

					// store the user's current search query
					previousQuery = query;

					for (final Person friend : friends)
					// search through every friend in the list of friends
					{
						final String name = friend.getName().toLowerCase();

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
				final ArrayList<Person> values = (ArrayList<Person>) results.values;

				// Add the list of filtered friends to the newly cleared list.
				friends.addAll(values);

				// This refreshes the friends list as shown on the device's
				// screen.
				notifyDataSetChanged();
			}


		}




		private final class ViewHolder
		{


			private ImageView picture;
			private TextView name;


		}


	}


}
