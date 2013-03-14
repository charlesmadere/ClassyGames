package com.charlesmadere.android.classygames;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.ServerApi;
import com.charlesmadere.android.classygames.server.ServerApiForfeitGame;
import com.charlesmadere.android.classygames.server.ServerApiSkipMove;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class GamesListFragment extends SherlockFragment implements OnItemClickListener, OnItemLongClickListener
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GamesListFragment";




	/**
	 * Boolean that marks if this is the first time that the onResume() method
	 * was hit.
	 */
	private boolean isFirstOnResume = true;


	/**
	 * Used to perform a refresh of the Games List.
	 */
	private AsyncRefreshGamesList asyncRefreshGamesList;


	/**
	 * Holds a handle to a currently running (if it's currently running)
	 * ServerApi object.
	 */
	private ServerApi serverApiTask;


	/**
	 * List Adapter for this Fragment's ListView layout item.
	 */
	private GamesListAdapter gamesListAdapter;




	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * GamesListFragmentListeners interface.
	 */
	private GamesListFragmentListeners listeners;


	/**
	 * A bunch of listener methods for this Fragment.
	 */
	public interface GamesListFragmentListeners
	{


		/**
		 * This is fired when the user selects a game in their games list.
		 * 
		 * @param game
		 * The Game object that the user selected.
		 */
		public void onGameSelected(final Game game);


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
			listeners = (GamesListFragmentListeners) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && isAsyncRefreshGamesListRunning())
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
	public void onItemClick(final AdapterView<?> l, final View v, final int position, final long id)
	{
		final Game game = gamesListAdapter.getItem(position);

		if (game.isTypeGame() && game.isTurnYours())
		{
			listeners.onGameSelected(game);
		}
	}


	@Override
	public boolean onItemLongClick(final AdapterView<?> l, final View v, int position, final long id)
	{
		if (!isAnAsyncTaskRunning())
		{
			final Game game = gamesListAdapter.getItem(position);

			if (game.isTypeGame())
			{
				v.setSelected(true);

				final Context context = getSherlockActivity();
				String[] items = null;

				if (game.isTurnYours())
				{
					items = getResources().getStringArray(R.array.games_list_fragment_context_menu_entries_turn_yours);
				}
				else
				{
					items = getResources().getStringArray(R.array.games_list_fragment_context_menu_entries_turn_theirs);
				}

				final AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setItems(items, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(final DialogInterface dialog, final int which)
						{
							if (!isAnAsyncTaskRunning())
							{
								final ServerApi.ServerApiListeners serverApiListeners = new ServerApi.ServerApiListeners()
								{
									@Override
									public void onCancel()
									{
										serverApiTask = null;
									}


									@Override
									public void onComplete()
									{
										serverApiTask = null;
										listeners.onRefreshSelected();
									}


									@Override
									public void onDismiss()
									{
										serverApiTask = null;
									}
								};

								switch (which)
								{
									case 0:
										serverApiTask = new ServerApiForfeitGame(context, game, serverApiListeners);
										break;

									case 1:
										serverApiTask = new ServerApiSkipMove(context, game, serverApiListeners);
										break;
								}

								if (serverApiTask != null)
								{
									serverApiTask.execute();
								}
							}
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(final DialogInterface dialog, final int which)
						{
							dialog.dismiss();
						}
					})
					.setTitle(R.string.games_list_fragment_context_menu_text_generic);

				builder.show();

				return true;
			}

			return false;
		}
		else
		{
			return false;
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.generic_cancel_menu_cancel:
				if (isAsyncRefreshGamesListRunning())
				{
					asyncRefreshGamesList.cancel(true);
				}
				break;

			case R.id.generic_refresh_menu_refresh:
				listeners.onRefreshSelected();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onPrepareOptionsMenu(final Menu menu)
	{
		final MenuItem menuItem = menu.findItem(R.id.generic_refresh_menu_refresh);

		if (isAsyncRefreshGamesListRunning())
		{
			if (menuItem != null)
			{
				menuItem.setEnabled(false);
			}
		}
		else
		{
			if (menuItem != null)
			{
				menuItem.setEnabled(true);
			}
		}

		super.onPrepareOptionsMenu(menu);
	}


	@Override
	public void onResume()
	{
		super.onResume();

		if (isFirstOnResume)
		{
			isFirstOnResume = false;
			refreshGamesList();
		}
	}




	/**
	 * Cancels the AsyncRefreshGamesList AsyncTask if it is currently
	 * running.
	 */
	public void cancelRunningAnyAsyncTask()
	{
		if (isAsyncRefreshGamesListRunning())
		{
			asyncRefreshGamesList.cancel(true);
		}
		else if (serverApiTask != null)
		{
			serverApiTask.cancel();
		}
	}


	/**
	 * @return
	 * Returns true if the asyncRefreshGamesList AsyncTask is currently
	 * running.
	 */
	private boolean isAsyncRefreshGamesListRunning()
	{
		return asyncRefreshGamesList != null;
	}


	/**
	 * @return
	 * Returns true if an AsyncTask is running.
	 */
	public boolean isAnAsyncTaskRunning()
	{
		return isAsyncRefreshGamesListRunning() || serverApiTask != null;
	}


	/**
	 * Refreshes the Games List if a refresh is not already running.
	 */
	public void refreshGamesList()
	{
		if (!isAnAsyncTaskRunning())
		{
			asyncRefreshGamesList = new AsyncRefreshGamesList(getSherlockActivity(), getLayoutInflater(getArguments()), (ViewGroup) getView());
			asyncRefreshGamesList.execute();
		}
	}




	private final class AsyncRefreshGamesList extends AsyncTask<Void, Void, ArrayList<Game>>
	{


		private final static byte RUN_STATUS_NORMAL = 1;
		private final static byte RUN_STATUS_IOEXCEPTION = 2;
		private byte runStatus;


		private SherlockFragmentActivity fragmentActivity;
		private LayoutInflater inflater;
		private ViewGroup viewGroup;


		private AsyncRefreshGamesList(final SherlockFragmentActivity fragmentActivity, final LayoutInflater inflater, final ViewGroup viewGroup)
		{
			this.fragmentActivity = fragmentActivity;
			this.inflater = inflater;
			this.viewGroup = viewGroup;
		}


		@Override
		protected ArrayList<Game> doInBackground(final Void... params)
		{
			ArrayList<Game> games = null;
			runStatus = RUN_STATUS_NORMAL;

			if (!isCancelled())
			{
				final Person whoAmI = Utilities.getWhoAmI(fragmentActivity);

				// create the data that will be posted to the server
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, whoAmI.getIdAsString()));

				if (!isCancelled())
				{
					try
					{
						// Make a call to the Classy Games server API and store
						// the JSON response. Note that we're also sending it
						// the nameValuePairs variable that we just created.
						// The server requires we send it some information in
						// order for us to get a meaningful response back.
						final String serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_GET_GAMES, nameValuePairs);

						// This line does a lot. Check the parseServerResponse()
						// method below to get detailed information. This will
						// parse the JSON response that we got from the server
						// and create a bunch of individual Game objects out of
						// that data.
						games = parseServerResponse(serverResponse);
					}
					catch (final IOException e)
					{
						Log.e(LOG_TAG, "IOException error in AsyncPopulateGamesList - doInBackground()!", e);
					}
				}
			}

			return games;
		}


		private void cancelled()
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.games_list_fragment_cancelled, viewGroup);

			setRunningState(false);
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final ArrayList<Game> games)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final ArrayList<Game> games)
		{
			viewGroup.removeAllViews();

			if (games != null && !games.isEmpty())
			{
				inflater.inflate(R.layout.games_list_fragment, viewGroup);
				gamesListAdapter = new GamesListAdapter(fragmentActivity, R.layout.games_list_fragment_listview_item, games);

				final ListView listView = (ListView) viewGroup.findViewById(R.id.games_list_fragment_listview);
				listView.setAdapter(gamesListAdapter);
				listView.setOnItemClickListener(GamesListFragment.this);
				listView.setOnItemLongClickListener(GamesListFragment.this);
			}
			else if (runStatus == RUN_STATUS_IOEXCEPTION)
			{
				inflater.inflate(R.layout.games_list_fragment_no_internet_connection, viewGroup);
			}
			else
			{
				inflater.inflate(R.layout.games_list_fragment_no_games, viewGroup);
			}

			setRunningState(false);
		}


		@Override
		protected void onPreExecute()
		{
			setRunningState(true);

			viewGroup.removeAllViews();
			inflater.inflate(R.layout.games_list_fragment_loading, viewGroup);
		}


		/**
		 * Parses the JSON response from the server and makes a bunch of Game
		 * objects about it. 
		 * 
		 * @param serverResponse
		 * The JSON response acquired from the Classy Games server. This method
		 * <strong>does</strong> check to make sure that this String is both
		 * not null and not empty. If that scenario happens then this method
		 * will return an empty ArrayList of Game objects.
		 * 
		 * @return
		 * Returns an ArrayList of Game objects. This ArrayList has a
		 * possilibity of being empty.
		 */
		private ArrayList<Game> parseServerResponse(final String serverResponse)
		{
			final ArrayList<Game> games = new ArrayList<Game>();

			if (!isCancelled())
			{
				if (Utilities.verifyValidString(serverResponse))
				{
					try
					{
						final JSONObject jsonRaw = new JSONObject(serverResponse);
						final JSONObject jsonResult = jsonRaw.getJSONObject(ServerUtilities.POST_DATA_RESULT);
						final JSONObject jsonGameData = jsonResult.optJSONObject(ServerUtilities.POST_DATA_SUCCESS);

						if (jsonGameData == null)
						{
							final String successMessage = jsonResult.optString(ServerUtilities.POST_DATA_SUCCESS);

							if (Utilities.verifyValidString(successMessage))
							{
								Log.d(LOG_TAG, "Server returned success message: " + successMessage);
							}
							else
							{
								final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);
								Log.e(LOG_TAG, "Server returned error message: " + errorMessage);
							}
						}
						else
						{
							ArrayList<Game> turn = parseTurn(jsonGameData, ServerUtilities.POST_DATA_TURN_YOURS, Game.TURN_YOURS);
							if (turn != null && !turn.isEmpty())
							{
								games.addAll(turn);
							}

							turn = parseTurn(jsonGameData, ServerUtilities.POST_DATA_TURN_THEIRS, Game.TURN_THEIRS);
							if (turn != null && !turn.isEmpty())
							{
								games.addAll(turn);
							}
						}
					}
					catch (final JSONException e)
					{
						Log.e(LOG_TAG, "JSON String is massively malformed.");
					}

					games.trimToSize();
				}
				else
				{
					Log.e(LOG_TAG, "Empty or null String received from server on get games!");
				}
			}

			return games;
		}


		/**
		 * Creates an ArrayList of Game objects that are of the specified turn.
		 * 
		 * @param jsonGameData
		 * The JSON game data as received from the server.
		 * 
		 * @param postDataTurn
		 * Which turn to pull from the JSON game data. This variable's value
		 * should be one of the ServerUtilities.POST_DATA_TURN_* variables.
		 * 
		 * @param whichTurn
		 * Who's turn is this? This variable's value should be one of the
		 * GAME.TURN_* variables.
		 * 
		 * @return
		 * Returns all of the games of the specified turn. Has the possibility
		 * of being null. Check for that!
		 */
		private ArrayList<Game> parseTurn(final JSONObject jsonGameData, final String postDataTurn, final boolean whichTurn)
		{
			ArrayList<Game> games = null;

			try
			{
				final JSONArray turn = jsonGameData.getJSONArray(postDataTurn);
				final int turnLength = turn.length();

				if (turnLength >= 1)
				// ensure that we have at least one element in the JSONArray
				{
					games = new ArrayList<Game>();
					games.add(new Game(whichTurn, Game.TYPE_SEPARATOR));

					for (int i = 0; i < turnLength && !isCancelled(); ++i)
					// loop through all of the games in this turn
					{
						try
						{
							final JSONObject jsonGame = turn.getJSONObject(i);

							final Game game = new Game(jsonGame, whichTurn);
							games.add(game);
						}
						catch (final JSONException e)
						{
							Log.e(LOG_TAG, "Error parsing a turn's game data! (" + i + ") whichTurn: " + whichTurn);
						}
					}

					Collections.sort(games, new GamesListSorter());
				}
			}
			catch (final JSONException e)
			{
				if (whichTurn == Game.TURN_YOURS)
				{
					Log.d(LOG_TAG, "Player has no games that are his own turn.");
				}
				else
				{
					Log.d(LOG_TAG, "Player has no games that are the other people's turn.");
				}
			}

			return games;
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
				asyncRefreshGamesList = null;
			}

			Utilities.compatInvalidateOptionsMenu(fragmentActivity, true);
		}


	}




	private final class GamesListAdapter extends ArrayAdapter<Game>
	{


		private ArrayList<Game> games;
		private Drawable emptyProfilePicture;
		private Context context;


		GamesListAdapter(final Context context, final int textViewResourceId, final ArrayList<Game> games)
		{
			super(context, textViewResourceId, games);
			this.context = context;
			this.games = games;

			emptyProfilePicture = (Drawable) context.getResources().getDrawable(R.drawable.empty_profile_picture_small);
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final Game game = games.get(position);

			if (game.isTypeGame())
			{
				convertView = inflater.inflate(R.layout.games_list_fragment_listview_item, null);

				final ImageView picture = (ImageView) convertView.findViewById(R.id.games_list_fragment_listview_item_picture);
				picture.setImageDrawable(emptyProfilePicture);
				Utilities.getImageLoader(context).displayImage(FacebookUtilities.GRAPH_API_URL + game.getPerson().getId() + FacebookUtilities.GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL, picture);

				final TextView name = (TextView) convertView.findViewById(R.id.games_list_fragment_listview_item_name);
				name.setText(game.getPerson().getName());
				name.setTypeface(TypefaceUtilities.getTypeface(context.getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));

				final TextView time = (TextView) convertView.findViewById(R.id.games_list_fragment_listview_item_time);
				time.setText(game.getTimestampFormatted(context));
			}
			else
			{
				if (game.isTurnYours())
				{
					convertView = inflater.inflate(R.layout.games_list_fragment_listview_turn_yours, null);
				}
				else
				{
					convertView = inflater.inflate(R.layout.games_list_fragment_listview_turn_theirs, null);
				}

				convertView.setOnClickListener(null);
				convertView.setOnLongClickListener(null);
			}

			return convertView;
		}


	}




	private final class GamesListSorter implements Comparator<Game>
	{
		@Override
		public int compare(final Game gameOne, final Game gameTwo)
		{
			return (int) (gameTwo.getTimestamp() - gameOne.getTimestamp());
		}
	}




}
