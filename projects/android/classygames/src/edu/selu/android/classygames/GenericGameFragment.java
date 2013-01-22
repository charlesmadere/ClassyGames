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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.GenericPiece;
import edu.selu.android.classygames.games.Position;
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
	 * Object representing the living person that I (the user) am playing
	 * against.
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
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		// retrieve data passed to this fragment
		final Bundle arguments = getArguments();

		if (arguments == null || arguments.isEmpty())
		{
			fragmentHasError();
		}
		else
		{
			gameId = arguments.getString(INTENT_DATA_GAME_ID);
			final long challengedId = arguments.getLong(INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = arguments.getString(INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (Person.isIdValid(challengedId) && Person.isNameValid(challengedName))
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
			else
			{
				fragmentHasError();
			}
		}

		return inflater.inflate(onCreateView(), container, false);
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.generic_game_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

	}


	/**
	 * <p>Creates a tag to be used in a findViewWithTag() operation.</p>
	 * 
	 * <p><strong>Examples</strong><br />
	 * createTag(3, 5) returns "x3y5"<br />
	 * createTag(0, 0) returns "x0y0"<br /></p>
	 * 
	 * @param x
	 * The <strong>X</strong> coordinate to create the tag from.
	 * 
	 * @param y
	 * The <strong>Y</strong> coordinate to create the tag from.
	 * 
	 * @return
	 * Returns a tag made from the input arguments.
	 */
	protected String createTag(final byte x, final byte y)
	{
		return "x" + x + "y" + y;
	}


	/**
	 * If this fragment has some kind of issue with the data passed to it then
	 * this method should be called. This fragment <strong>requires</strong>
	 * some specific data and it absolutely can't run in the event that that
	 * data is missing.
	 */
	private void fragmentHasError()
	{
		Log.d(LOG_TAG, "fragmentHasError()!");
	}


	/**
	 * Parses the passed in tag String for two numbers (those two numbers are
	 * the tag's coordinates) and returns those coordinates as a byte array.
	 * The returned byte array will have a length of just 2, with [0] being
	 * the X coordinate and [1] being the Y coordinate.
	 * 
	 * <p><strong>Example</strong><br />
	 * final String tag = "x2y9";<br />
	 * final byte[] coordinates = getCoordinatesFromTag(tag);<br />
	 * coordinates[0]: 2, coordinates[1]: 9</p>
	 * 
	 * @param tag
	 * The tag to parse for coordinates. Note that this method <strong>does not
	 * check</strong> for a null or empty String. If this method encounters
	 * either of those scenarios then there will probably be a crash. The tag
	 * that this method parses for should be formatted like so: "x5y3", "x0y6",
	 * "x15y3", or "x21y32".
	 * 
	 * @return
	 * Returns a byte array with [0] being the given tag's X coordinate and [1]
	 * being the given tag's Y coordinate.
	 */
	protected byte[] getCoordinatesFromTag(final String tag)
	{
		// Initialize an array for the coordinates. This will be returned.
		final byte[] coordinates = new byte[2];

		boolean inDigits = false;

		// used to store positions in the tag String in order to make
		// substrings
		int beginIndex = 0, endIndex = 0;

		// store the position we're currently at in the tag String
		int i = 0;

		// This will be used with whether or not the below loop continues to
		// run.
		boolean bothCoordinatesFound = false;

		do
		// Continue to loop until the bothCoordinatesFound variable is true.
		// This will only happen when... both coordinates have been found!
		{
			// save the current char in the String
			final char character = tag.charAt(i);

			// check to see if the char is a digit
			final boolean characterIsDigit = Character.isDigit(character);

			if (!inDigits && characterIsDigit)
			// if our position in the tag String is not already in digits and
			// the current char is a digit
			{
				// mark that we're now in digits
				inDigits = true;

				// store the beginning substring position
				beginIndex = i;
			}
			else if (inDigits && !characterIsDigit)
			// if our position in the tag String is in digits and the current
			// char is not a digit
			{
				// mark that we're no longer in digits
				inDigits = false;

				// store the end substring position
				endIndex = i;

				// create a substring from the tag String
				String sub = tag.substring(beginIndex, endIndex);

				// Parse that substring into a byte. This value is the tag
				// String's X value.
				coordinates[0] = Byte.parseByte(sub);

				// create another substring from the tag String
				sub = tag.substring(endIndex + 1, tag.length());

				// Parse that substring into a byte. this value is the tag
				// String's Y value.
				coordinates[1] = Byte.parseByte(sub);

				// Both coordinates have been found and stored. The loop can
				// exit now.
				bothCoordinatesFound = true;
			}

			// move to the next character in the tag String
			++i;
		}
		while (!bothCoordinatesFound);

		return coordinates;
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


	/**
	 * This method will initialize the game board as if this is an in progress
	 * game.
	 */
	private void initBoardOld()
	{
		if (boardJSON == null || boardJSON.isEmpty())
		{
			Log.e(LOG_TAG, "Tried to build the board from either a null or empty JSON String!");
		}
		else
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
					Log.e(LOG_TAG, "JSON String has improper number of teams! teams.length(): " + teams.length());
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "JSON String is massively malformed.");
			}
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


	/**
	 * Reads in a JSON response String as received from the web server and
	 * pulls the needed information out of it. If there is an error during this
	 * process, null is returned.
	 * 
	 * @param jsonString
	 * The JSON response String as received from the web server. This method
	 * <strong>does</strong> check to see if this passed in String is either
	 * null or empty. In that case, the method will immediately log that error
	 * and then return null.
	 * 
	 * @return
	 * Returns a String containing only the necessary game information. But, if
	 * there is an error in the parsing process, this method will log some
	 * stuff and then return null.
	 */
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




	private final class AsyncGetGame extends AsyncTask<Void, Void, String>
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
			progressDialog.setMessage(GenericGameFragment.this.getString(R.string.generic_game_fragment_getgame_progressdialog_message));
			progressDialog.setTitle(R.string.generic_game_fragment_getgame_progressdialog_title);
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
				final JSONObject board = new JSONObject();
				board.put("teams", createJSONTeams());

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
		protected void onCancelled(final String serverResponse)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			parseServerResponse(serverResponse);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(getSherlockActivity());
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(getString(R.string.generic_game_fragment_sendmove_progressdialog_message));
			progressDialog.setTitle(R.string.generic_game_fragment_sendmove_progressdialog_title);
			progressDialog.show();
		}


		/**
		 * 
		 * 
		 * @return
		 * 
		 */
		private JSONArray createJSONTeams()
		{
			final JSONArray teamPlayer = createJSONTeam(GenericPiece.TEAM_PLAYER);
			final JSONArray teamOpponent = createJSONTeam(GenericPiece.TEAM_OPPONENT);

			final JSONArray teams = new JSONArray();
			teams.put(teamPlayer);
			teams.put(teamOpponent);

			return teams;
		}


		/**
		 * 
		 * 
		 * @param whichTeam
		 * 
		 * 
		 * @return
		 * 
		 */
		private JSONArray createJSONTeam(final byte whichTeam)
		{
			final JSONArray team = new JSONArray();

			for (byte x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
			{
				for (byte y = 0; y < Board.LENGTH_VERTICAL; ++y)
				{
					final Position position = board.getPosition(x, y);

					if (position.hasPiece() && position.getPiece().isTeam(whichTeam))
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
	 * Can be looked at as a main method for classes that extend this one. This
	 * method is very important because it must returns which layout this
	 * fragment will need to inflate. Returning the needed layout is really the
	 * only thing that this method needs to do.
	 * 
	 * @return
	 * This method must return the Android int representation of its layout.
	 * For checkers, this method will return R.layout.checkers_game_layout. For
	 * chess, this method will return R.layout.chess_game_layout.
	 */
	protected abstract int onCreateView();


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
