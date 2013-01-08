package edu.selu.android.classygames;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.GenericPiece;
import edu.selu.android.classygames.games.checkers.Board;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public abstract class GenericGameFragment extends SherlockFragment
{


	protected final static String LOG_TAG = Utilities.LOG_TAG + " - Game Logic";

	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_ID = "GAME_PERSON_CHALLENGED_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_NAME = "GAME_PERSON_CHALLENGED_NAME";


	/**
	 * Boolean indicating if the board is locked or not. Once the board has
	 * been locked it can only be locked by using undo.
	 */
	protected boolean boardLocked = false;


	/**
	 * The ID of this game. If this is a brand new game, this value does not
	 * need to be grabbed from the server and can stay null. Otherwise, this
	 * String <strong>must</strong> have a value.
	 */
	protected String gameId;


	/**
	 * Object representing the living person that I am playing against.
	 */
	protected Person personChallenged;


	/**
	 * JSON String downloaded from the server that represents the board.
	 */
	protected String boardJSON;


	/**
	 * The actual logical representation of the board.
	 */
	protected GenericBoard board;


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces and / or performs actions accordingly.
	 */
	protected OnClickListener onBoardClick;




	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		// retrieve data passed to this fragment
		final Bundle bundle = getArguments();

		if (bundle == null || bundle.isEmpty())
		{
			fragmentHasError();
		}
		else
		{
			gameId = bundle.getString(INTENT_DATA_GAME_ID);
			final long challengedId = bundle.getLong(INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = bundle.getString(INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (challengedId <= 0 || challengedName == null || challengedName.isEmpty())
			{
				fragmentHasError();
			}
			else
			{
				personChallenged = new Person(challengedId, challengedName);

				onBoardClick = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						onBoardClick(v);
					}
				};

				initViews();

				if (gameId == null || gameId.isEmpty())
				{
					initBoard();
				}
				else
				{
					new AsyncGetGame().execute();
				}
			}
		}

		return super.onCreateView(inflater, container, savedInstanceState);
	}


	private void fragmentHasError()
	{

	}


	/**
	 * Initializes the game board. If this game has been downloaded from the
	 * server, then this method will run initBoardOld(). Otherwise, this is a
	 * brand new game and this method will run initBoardNew().
	 */
	private void initBoard()
	{
		if (boardJSON == null || boardJSON.isEmpty())
		// Test to see if this game has been downloaded from the server. This
		// statement will validate as true if the game has been downloaded from
		// the server.
		{
			initBoardNew();
		}
		else
		{
			initBoardOld();
		}
	}


	private void initBoardOld()
	{
		if (boardJSON != null && !boardJSON.isEmpty())
		{
			try
			{
				final JSONArray teams = new JSONObject(boardJSON).getJSONObject("board").getJSONArray("teams");

				if (teams.length() == 2)
				{
					buildTeam(teams.getJSONArray(0), GenericPiece.TEAM_PLAYER);
					buildTeam(teams.getJSONArray(1), GenericPiece.TEAM_OPPONENT);
				}
				else
				{
					Log.e(LOG_TAG, "JSON String has improper number of teams!");
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "JSON String is massively malformed.");
			}
		}
		else
		{
			Log.e(LOG_TAG, "Tried to build the board from either a null or empty JSON String!");
		}
	}


	/**
	 * Locks the board. This prevents the player from continuing to move
	 * pieces around. The player must press the undo button if they want to
	 * move anything from this point on.
	 */
	protected void lockBoard()
	{
		if (!boardLocked)
		{
			boardLocked = true;
		}
	}


	private String parseServerResponse(final String jsonString)
	{
		if (jsonString == null || jsonString.isEmpty())
		{
			Log.e(LOG_TAG, "Empty string received from server on send move!");
		}
		else
		{
			try
			{
				final JSONObject jsonData = new JSONObject(jsonString);
				final JSONObject jsonResult = jsonData.getJSONObject(ServerUtilities.POST_DATA_RESULT);

				try
				{
					final String successData = jsonResult.getString(ServerUtilities.POST_DATA_SUCCESS);
					return successData;
				}
				catch (final JSONException e)
				{
					try
					{
						final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);
						Log.e(LOG_TAG, "Data returned from server contained an error message: " + errorMessage);
					}
					catch (final JSONException e1)
					{
						Log.e(LOG_TAG, "Data returned from server contained neither a success nor an error message!");
					}
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "Couldn't grab result object from server response.");
			}
		}

		return null;
	}


	/**
	 * Undoes the user's last move on the board. Unlocks the board, allowing
	 * the user to make a different move on the board.
	 */
	protected void undo()
	{
		if (boardLocked)
		{
			

			boardLocked = false;
		}
	}




	protected final class AsyncGetGame extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... v)
		{
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, gameId));

			try
			{
				return ServerUtilities.postToServer( ServerUtilities.SERVER_GET_GAME_ADDRESS, nameValuePairs );
			}
			catch (final IOException e)
			{
				Log.e(Utilities.LOG_TAG, "Error in HTTP POST to " + ServerUtilities.SERVER_GET_GAME_ADDRESS, e);
			}

			return null;
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			boardJSON = parseServerResponse(serverResponse);
			initBoard();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GenericGameFragment.this.getSherlockActivity());
			progressDialog.setMessage(GenericGameFragment.this.getString(R.string.game_fragment_getgame_progressdialog_message));
			progressDialog.setTitle(R.string.game_fragment_getgame_progressdialog_title);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}


	}


	private final class AsyncSendMove extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... v)
		{
			try
			{
				final JSONArray teams = createJSONTeams();
				final JSONObject board = new JSONObject();
				board.put("teams", teams);

				final JSONObject object = new JSONObject();
				object.put("board", board);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				try
				{
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, Long.valueOf(personChallenged.getId()).toString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, personChallenged.getName()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, Long.valueOf(Utilities.getWhoAmI(GenericGameFragment.this.getSherlockActivity()).getId()).toString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_BOARD, object.toString()));

					if (gameId == null || gameId.isEmpty())
					{
						return ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_GAME_ADDRESS, nameValuePairs);
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, gameId));
						return ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_MOVE_ADDRESS, nameValuePairs);
					}
				}
				catch (final IOException e)
				{
					Log.e(Utilities.LOG_TAG, "Error in HTTP POST to server.", e);
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Error in creating JSON data to send to server.", e);
			}

			return null;
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			parseServerResponse(serverResponse);
			progressDialog.dismiss();

			// new, fragment way, of doing finish()
			getSherlockActivity().getSupportFragmentManager().beginTransaction().remove(GenericGameFragment.this).commit();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GenericGameFragment.this.getActivity());
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(GenericGameFragment.this.getString(R.string.game_fragment_sendmove_progressdialog_message));
			progressDialog.setTitle(R.string.game_fragment_sendmove_progressdialog_title);
			progressDialog.show();

			GenericGameFragment.this.setResult(CentralFragmentActivity.NEED_TO_REFRESH);
		}


		private JSONArray createJSONTeams()
		{
			final JSONArray teamPlayer = createJSONTeam(GenericPiece.TEAM_PLAYER);
			final JSONArray teamOpponent = createJSONTeam(GenericPiece.TEAM_OPPONENT);

			final JSONArray teams = new JSONArray();
			teams.put(teamPlayer);
			teams.put(teamOpponent);

			return teams;
		}


		private JSONArray createJSONTeam(final byte whichTeam)
		{
			final JSONArray team = new JSONArray();

			for (byte x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
			{
				for (byte y = 0; y < Board.LENGTH_VERTICAL; ++y)
				{
					if (board.getPosition(x, y).hasPiece() && board.getPosition(x, y).getPiece().isTeam(whichTeam))
					// this position has a piece in it that is of the given team
					{
						try
						{
							final JSONArray coordinate = new JSONArray();
							coordinate.put(x);
							coordinate.put(y);

							final JSONObject piece = new JSONObject();
							piece.put("coordinate", coordinate);
							piece.put("type", board.getPosition(x, y).getPiece().getType());

							team.put(piece);
						}
						catch (final JSONException e)
						{
							Log.e(LOG_TAG, "Error in createJSONTeam, x = " + x + ", y = " + y);
						}
					}
				}
			}

			return team;
		}


	}




	/**
	 * Create's a team from some JSON data.
	 * 
	 * @param team
	 * An individual team as parsed in from a JSON String.
	 * 
	 * @param whichTeam
	 * GenericPiece.TEAM_* should be used here.
	 */
	protected abstract void buildTeam(final JSONArray team, final byte whichTeam);


	/**
	 * Create's a team from some JSON data.
	 * 
	 * @param team
	 * An individual team as parsed in from a JSON String.
	 * 
	 * @param whichTeam
	 * GenericPiece.TEAM_* should be used here.
	 */
	protected void buildTeam(final JSONArray team, final int whichTeam)
	{
		buildTeam(team, (byte) whichTeam);
	}


	/**
	 * Initialize the game board as if it's a <strong>brand new game</strong>.
	 */
	protected abstract void initBoardNew();


	/**
	 * Initializes some of this Fragment's view data, such as the actionbar's
	 * title, layout configurations, and onClickListeners.
	 */
	protected abstract void initViews();


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces and / or performs actions accordingly.
	 *
	 * @param v
	 * The View object that was clicked on.
	 */
	protected abstract void onBoardClick(final View v);


}
