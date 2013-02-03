package edu.selu.android.classygames;


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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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

import edu.selu.android.classygames.models.Game;
import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class GamesListFragment extends SherlockListFragment
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GamesListFragment";


	/**
	 * Boolean that marks if this is the first time that the onResume method
	 * was hit.
	 */
	private boolean isFirstOnResume = true;


	/**
	 * Boolean that marks if the AsyncPopulateGamesList AsyncTask is currently
	 * running. This is used in conjunction with cancelling that AsyncTask. If
	 * the cancel button is pressed and the AsyncTask is currently running...
	 */
	private boolean isAsyncPopulateGamesListRunning = false;


	/**
	 * Used to perform a refresh of the Games List.
	 */
	private AsyncPopulateGamesList asyncPopulateGamesList;


	/**
	 * List Adapter for this Fragment's ListView layout item.
	 */
	private GamesListAdapter gamesListAdapter;




	/**
	 * One of this class's callback methods. This is fired whenever this
	 * fragment's onDestroyView() method is called.
	 */
	private GamesListFragmentOnDestroyViewListener gamesListFragmentOnDestroyViewListener;

	public interface GamesListFragmentOnDestroyViewListener
	{
		public void gamesListFragmentOnDestroyView();
	}


	/**
	 * One of this class's callback methods. This is fired whenever one of the
	 * games in the user's list of games is clicked on.
	 */
	private GamesListFragmentOnGameSelectedListener gamesListFragmentOnGameSelectedListener;

	public interface GamesListFragmentOnGameSelectedListener
	{
		public void gameListFragmentOnGameSelected(final Game game);
	}


	/**
	 * One of this class's callback methods. This is fired whenever the new
	 * game button in the action bar is clicked.
	 */
	private GamesListFragmentOnNewGameSelectedListener gamesListFragmentOnNewGameSelectedListener;

	public interface GamesListFragmentOnNewGameSelectedListener
	{
		public void gamesListFragmentOnNewGameSelected();
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
			gamesListFragmentOnDestroyViewListener = (GamesListFragmentOnDestroyViewListener) activity;
			gamesListFragmentOnGameSelectedListener = (GamesListFragmentOnGameSelectedListener) activity;
			gamesListFragmentOnNewGameSelectedListener = (GamesListFragmentOnNewGameSelectedListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (isAsyncPopulateGamesListRunning)
		{
			inflater.inflate(R.menu.games_list_fragment_secondary, menu);
		}
		else
		{
			inflater.inflate(R.menu.games_list_fragment_primary, menu);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		gamesListFragmentOnDestroyViewListener.gamesListFragmentOnDestroyView();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.games_list_fragment_primary_actionbar_about:
				startActivity(new Intent(getSherlockActivity(), AboutActivity.class));
				break;

			case R.id.games_list_fragment_primary_actionbar_new_game:
				// notify the parent Activity that the new game button in the
				// action bar has been clicked
				gamesListFragmentOnNewGameSelectedListener.gamesListFragmentOnNewGameSelected();
				break;

			case R.id.games_list_fragment_primary_actionbar_refresh:
				refreshGamesList();
				break;

			case R.id.games_list_fragment_secondary_actionbar_cancel:
				if (isAsyncPopulateGamesListRunning)
				{
					asyncPopulateGamesList.cancel(true);
				}
				else
				{
					Log.e(LOG_TAG, "Cancel pressed while no AsyncTasks were running!");
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

		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setTitle(R.string.games_list_fragment_title);

		if (isFirstOnResume)
		{
			refreshGamesList();
			isFirstOnResume = false;
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
	 * Refreshes the Games List if a refresh is not already running.
	 */
	private void refreshGamesList()
	{
		if (!isAsyncPopulateGamesListRunning)
		{
			asyncPopulateGamesList = new AsyncPopulateGamesList(getSherlockActivity(), getLayoutInflater(getArguments()), (ViewGroup) getView());
			asyncPopulateGamesList.execute();
		}
	}




	private final class AsyncPopulateGamesList extends AsyncTask<Void, Integer, ArrayList<Game>>
	{




		private Context context;
		private LayoutInflater inflater;
		private ViewGroup viewGroup;


		AsyncPopulateGamesList(final Context context, final LayoutInflater inflater, final ViewGroup viewGroup)
		{
			this.context = context;
			this.inflater = inflater;
			this.viewGroup = viewGroup;
		}


		@Override
		protected ArrayList<Game> doInBackground(final Void... params)
		{
			ArrayList<Game> games = null;

			if (!isCancelled())
			{
				final Person whoAmI = Utilities.getWhoAmI(context);

				// create the data that will be 
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
						final String jsonResponse = ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAMES_ADDRESS, nameValuePairs);

						// This line does a lot. Check the parseServerResponse()
						// method below to get detailed information. This will
						// parse the JSON response that we got from the server
						// and create a bunch of individual Game objects out of
						// that data.
						games = parseServerResponse(jsonResponse);
					}
					catch (final IOException e)
					{
						Log.e(LOG_TAG, "IOException error in AsyncPopulateGamesList - doInBackground()!", e);
					}
				}
			}

			return games;
		}


		@Override
		protected void onCancelled(final ArrayList<Game> games)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.games_list_fragment_cancelled, viewGroup);

			isAsyncPopulateGamesListRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPostExecute(final ArrayList<Game> games)
		{
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.games_list_fragment, viewGroup);

			gamesListAdapter = new GamesListAdapter(context, R.layout.games_list_fragment_listview_item, games);
			final ListView listView = (ListView) viewGroup.findViewById(android.R.id.list);
			listView.setAdapter(gamesListAdapter);

			isAsyncPopulateGamesListRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncPopulateGamesListRunning = true;
			compatInvalidateOptionsMenu();

			viewGroup.removeAllViews();
			inflater.inflate(R.layout.games_list_fragment_loading, viewGroup);
		}


		/**
		 * Parses the JSON response from the server and makes a bunch of Game
		 * objects about it. 
		 * 
		 * @param jsonResponse
		 * The JSON response acquired from the Classy Games server. This method
		 * <strong>does</strong> check to make sure that this String is both
		 * not null and not empty. If that scenario happens then this method
		 * will return an empty ArrayList of Game objects.
		 * 
		 * @return
		 * Returns an ArrayList of Game objects. This ArrayList has a
		 * possilibity of being empty.
		 */
		private ArrayList<Game> parseServerResponse(final String jsonResponse)
		{
			final ArrayList<Game> games = new ArrayList<Game>();

			if (!isCancelled())
			{
				if (jsonResponse == null || jsonResponse.isEmpty())
				{
					Log.e(LOG_TAG, "Empty or null String received from server on get games!");
				}
				else
				{
					try
					{
						final JSONObject jsonRaw = new JSONObject(jsonResponse);
						final JSONObject jsonResult = jsonRaw.getJSONObject(ServerUtilities.POST_DATA_RESULT);
						final JSONObject jsonGameData = jsonResult.optJSONObject(ServerUtilities.POST_DATA_SUCCESS);

						if (jsonGameData == null)
						{
							final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);
							Log.e(LOG_TAG, "Server returned error message: " + errorMessage);
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
							final Game game = parseGame(jsonGame, whichTurn);

							if (game != null)
							{
								games.add(game);
							}
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
		 * Creates a single Game object out of some JSON data as received from
		 * the server.
		 * 
		 * @param gameData
		 * JSON data pertaining to a single Game object.
		 * 
		 * @param whichTurn
		 * Who's turn is this? This variable's value should be one of the
		 * Game.TURN_* variables.
		 * 
		 * @return
		 * Returns a single Game object. Has the possibility of being null.
		 * Make sure to check for that!
		 */
		private Game parseGame(final JSONObject gameData, final boolean whichTurn)
		{
			Game game = null;

			if (!isCancelled())
			{
				try
				{
					// create data from the server's json response
					final long id = gameData.getLong(ServerUtilities.POST_DATA_ID);
					final String name = gameData.getString(ServerUtilities.POST_DATA_NAME);
					final String gameId = gameData.getString(ServerUtilities.POST_DATA_GAME_ID);
					final long timestamp = gameData.getLong(ServerUtilities.POST_DATA_LAST_MOVE);
	
					// create a new game from this data
					game = new Game(timestamp, new Person(id, name), gameId, whichTurn);
				}
				catch (final JSONException e)
				{
					Log.e(LOG_TAG, "Error parsing individual game data!");
				}
			}

			return game;
		}




	}




	private class GamesListAdapter extends ArrayAdapter<Game>
	{




		private ArrayList<Game> games;
		private Context context;


		public GamesListAdapter(final Context context, final int textViewResourceId, final ArrayList<Game> games)
		{
			super(context, textViewResourceId, games);
			this.context = context;
			this.games = games;
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
				Utilities.getImageLoader(context).displayImage(Utilities.FACEBOOK_GRAPH_API_URL + game.getPerson().getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SMALL_SSL, picture);

				final TextView name = (TextView) convertView.findViewById(R.id.games_list_fragment_listview_item_name);
				name.setText(game.getPerson().getName());
				name.setTypeface(Utilities.getTypeface(context.getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));

				final TextView time = (TextView) convertView.findViewById(R.id.games_list_fragment_listview_item_time);
				time.setText(game.getTimestampFormatted(context));

				if (game.isTurnYours())
				{
					convertView.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(final View v)
						{
							gamesListFragmentOnGameSelectedListener.gameListFragmentOnGameSelected(game);
						}
					});
				}
				else
				{
					convertView.setOnClickListener(null);
				}
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
			}

			return convertView;
		}


	}




	private class GamesListSorter implements Comparator<Game>
	{
		@Override
		public int compare(final Game gameOne, final Game gameTwo)
		{
			return (int) (gameTwo.getTimestamp() - gameOne.getTimestamp());
		}
	}




}
