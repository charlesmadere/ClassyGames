package edu.selu.android.classygames;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.GenericPiece;
import edu.selu.android.classygames.games.Position;
import edu.selu.android.classygames.models.Game;
import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public abstract class GenericGameFragment extends SherlockFragment
{


	protected final static String LOG_TAG = Utilities.LOG_TAG + " - GenericGameFragment";


	public final static String KEY_GAME_ID = "KEY_GAME_ID";
	public final static String KEY_PERSON_ID = "KEY_PERSON_ID";
	public final static String KEY_PERSON_NAME = "KEY_PERSON_NAME";


	/**
	 * Boolean indicating if the board is locked or not. Once the board has
	 * been locked it can only be locked by using undo.
	 */
	private boolean boardLocked = false;


	/**
	 * This game's Game object. This contains a whole bunch of necessary data
	 * such as the ID of the game as well as the Person object that the current
	 * Android user is playing against. <strong>DO NOT MODIFY THIS OBJECT ONCE
	 * IT HAS BEEN SET.</strong>
	 */
	protected Game game;


	/**
	 * JSON String downloaded from the server that represents the board.
	 */
	private String boardJSON;


	/**
	 * The actual logical representation of the board.
	 */
	protected GenericBoard board;


	/**
	 * Holds a handle to the currently running (if it's currently running)
	 * AsyncGetGame AsyncTask.
	 */
	private AsyncGetGame asyncGetGame;


	/**
	 * Variables that holds whether or not the asyncGetGame AsyncTask is
	 * currently running.
	 */
	private boolean isAsyncGetGameRunning = false;


	/**
	 * Holds a handle to the currently running (if it's currently running)
	 * AsyncSendMove AsyncTask.
	 */
	private AsyncSendMove asyncSendMove;


	/**
	 * Variables that holds whether or not the asyncSendMove AsyncTask is
	 * currently running.
	 */
	private boolean isAsyncSendMoveRunning = false;


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces and / or performs actions accordingly.
	 */
	protected OnClickListener onBoardClick;




	/**
	 * One of this class's callback methods. This is fired in the event that
	 * the user cancels the AsyncGetGame AsyncTask.
	 */
	private GenericGameFragmentOnAsyncGetGameCancelledListener genericGameFragmentOnAsyncGetGameCancelledListener;

	public interface GenericGameFragmentOnAsyncGetGameCancelledListener
	{
		public void genericGameFragmentOnAsyncGetGameCancelledListener();
	}


	/**
	 * One of this class's callback methods. This is fired in the event that
	 * an error was detected in some of the data needed to instantiate a game.
	 */
	private GenericGameFragmentOnDataErrorListener genericGameFragmentOnDataErrorListener;

	public interface GenericGameFragmentOnDataErrorListener
	{
		public void genericGameFragmentOnDataErrorListener();
	}


	/**
	 * One of this class's callback methods. This is fired whenever this
	 * fragment's onDestroyView() method is called.
	 */
	private GenericGameFragmentOnDestroyViewListener genericGameFragmentOnDestroyViewListener;

	public interface GenericGameFragmentOnDestroyViewListener
	{
		public void genericGameFragmentOnDestroyViewListener();
	}




	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		return inflater.inflate(onCreateView(), container, false);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final Bundle arguments = getArguments();

		if (arguments == null || arguments.isEmpty())
		{
			genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
		}
		else
		{
			final String gameId = arguments.getString(KEY_GAME_ID);
			final long personId = arguments.getLong(KEY_PERSON_ID);
			final String personName = arguments.getString(KEY_PERSON_NAME);

			if (Person.isIdValid(personId) && Person.isNameValid(personName))
			{
				onBoardClick = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						onBoardClick(v);
					}
				};

				final Person person = new Person(personId, personName);
				initViews();

				if (Game.isIdValid(gameId))
				{
					game = new Game(person, gameId);

					asyncGetGame = new AsyncGetGame();
					asyncGetGame.execute();
				}
				else
				{
					game = new Game(person);
					initNewBoard();
				}
			}
			else
			{
				genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
			}
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
			genericGameFragmentOnAsyncGetGameCancelledListener = (GenericGameFragmentOnAsyncGetGameCancelledListener) activity;
			genericGameFragmentOnDataErrorListener = (GenericGameFragmentOnDataErrorListener) activity;
			genericGameFragmentOnDestroyViewListener = (GenericGameFragmentOnDestroyViewListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		menu.clear();

		if (isAsyncGetGameRunning || isAsyncSendMoveRunning)
		{
			inflater.inflate(R.menu.generic_game_fragment_secondary, menu);
		}
		else
		{
			inflater.inflate(R.menu.generic_game_fragment_primary, menu);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		super.onDestroyView();

		genericGameFragmentOnDestroyViewListener.genericGameFragmentOnDestroyViewListener();
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

			case R.id.generic_game_fragment_actionbar_cancel:
				if (isAsyncGetGameRunning)
				{
					asyncGetGame.cancel(true);
				}
				else if (isAsyncSendMoveRunning)
				{
					asyncSendMove.cancel(true);
				}
				else
				{
					Log.e(LOG_TAG, "Cancel pressed while no AsyncTasks were running!");
				}
				break;

			case R.id.generic_game_fragment_actionbar_send_move:
				asyncSendMove = new AsyncSendMove();
				asyncSendMove.execute();
				break;

			case R.id.generic_game_fragment_actionbar_undo_move:
				undo();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onPrepareOptionsMenu(final Menu menu)
	{
		if (!isAsyncGetGameRunning && !isAsyncSendMoveRunning)
		{
			if (boardLocked)
			{
				menu.findItem(R.id.generic_game_fragment_actionbar_send_move).setEnabled(true);
				menu.findItem(R.id.generic_game_fragment_actionbar_undo_move).setEnabled(true);
			}
			else
			{
				menu.findItem(R.id.generic_game_fragment_actionbar_send_move).setEnabled(false);
				menu.findItem(R.id.generic_game_fragment_actionbar_undo_move).setEnabled(false);
			}
		}
	}


	@Override
	public void onResume()
	{
		super.onResume();

		if (game != null)
		{
			final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(getString(getTitle()) + " " + game.getPerson().getName());
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
	 * Creates a tag to be used in a findViewWithTag() operation.
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
	 * Creates a tag to be used in a findViewWithTag() operation.
	 * 
	 * <p><strong>Examples</strong><br />
	 * Coordinate c1 = new Coordinate(3, 5);<br />
	 * createTag(c1) <strong>returns</strong> "x3y5"<br />
	 * Coordinate c2 = new Coordinate(0, 0);<br />
	 * createTag(c2) <strong>returns</strong> "x0y0"<br /></p>
	 * 
	 * @param coordinate
	 * The Coordinate object to create the tag from.
	 * 
	 * @return
	 * Returns a tag made from the input Coordinate.
	 */
	protected String createTag(final Coordinate coordinate)
	{
		return createTag(coordinate.getX(), coordinate.getY());
	}


	/**
	 * Renders all of the game's pieces on the board by first clearing all of
	 * the existing pieces from it and then placing all of the current pieces.
	 */
	private void flush()
	{
		// clear all of the existing pieces from the board
		for (byte x = 0; x < board.getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < board.getLengthVertical(); ++y)
			{
				final String tag = createTag(x, y);

				// setting the ImageDrawable to null erases the current image
				// (if there is any) from this ImageDrawable
				((ImageButton) getView().findViewWithTag(tag)).setImageDrawable(null);
			}
		}

		// place all of the pieces back onto the board
		for (byte x = 0; x < board.getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < board.getLengthVertical(); ++y)
			{
				final Position position = board.getPosition(x, y);

				if (position.hasPiece())
				{
					// let each GameFragment class that extends from this class
					// handle it from here
					flush(position);
				}
			}
		}
	}


	/**
	 * Parses the passed in tag String for two numbers (those two numbers are
	 * the tag's coordinates) and returns those coordinates as a Coordinate
	 * object.
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
	 * Returns a Coordinate object containing the coordinates as specified in
	 * the given tag String. Has the possibility of returning a null coordinate
	 * if the passed in tag String is messed up.
	 */
	protected Coordinate getCoordinateFromTag(final String tag)
	{
		// Create a Coordinate object. This will be returned.
		Coordinate coordinate = null;

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
				final byte x = Byte.parseByte(sub);

				// create another substring from the tag String
				sub = tag.substring(endIndex + 1, tag.length());

				// Parse that substring into a byte. this value is the tag
				// String's Y value.
				final byte y = Byte.parseByte(sub);

				// create the Coordinate object out of the data that we found
				coordinate = new Coordinate(x, y);

				// Both coordinates have been found and stored. The loop can
				// exit now.
				bothCoordinatesFound = true;
			}

			// move to the next character in the tag String
			++i;
		}
		while (!bothCoordinatesFound);

		return coordinate;
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
	 * @param jsonResponse
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
	private String parseServerResponse(final String jsonResponse)
	{
		String parsedServerResponse = null;

		if (jsonResponse == null || jsonResponse.isEmpty())
		{
			Log.e(LOG_TAG, "Either null or empty String received from server on send move!");
		}
		else
		{
			try
			{
				final JSONObject jsonData = new JSONObject(jsonResponse);
				final JSONObject jsonResult = jsonData.getJSONObject(ServerUtilities.POST_DATA_RESULT);

				try
				{
					parsedServerResponse = jsonResult.getString(ServerUtilities.POST_DATA_SUCCESS);
				}
				catch (final JSONException e)
				{
					final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);
					Log.e(LOG_TAG, "Server returned error message: " + errorMessage);
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "JSON String is massively malformed.");
			}
		}

		return parsedServerResponse;
	}


	/**
	 * This method will initialize the game board as if this is an in progress
	 * game. This <strong>resumes</strong> an old game. Do not use this for a
	 * brand new game.
	 */
	private void resumeOldBoard()
	{
		if (boardJSON == null || boardJSON.isEmpty())
		{
			Log.e(LOG_TAG, "Tried to build the board from either a null or empty JSON String!");
			genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
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

		flush();
	}


	/**
	 * Undoes the user's last move on the board. Unlocks the board, allowing
	 * the user to make a different move on the board.
	 */
	private void undo()
	{
		if (boardLocked)
		{
			boardLocked = false;
			ActivityCompat.invalidateOptionsMenu(getSherlockActivity());
		}
	}




	private final class AsyncGetGame extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... params)
		{
			String serverResponse = null;

			if (!isCancelled())
			{
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, game.getId()));

				try
				{
					serverResponse = ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAME_ADDRESS, nameValuePairs);
				}
				catch (final IOException e)
				{
					Log.e(LOG_TAG, "IOException error in AsyncGetGame - doInBackground()!", e);
				}
			}

			return serverResponse;
		}


		@Override
		protected void onCancelled(final String serverResponse)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			isAsyncGetGameRunning = false;
			compatInvalidateOptionsMenu();
			genericGameFragmentOnAsyncGetGameCancelledListener.genericGameFragmentOnAsyncGetGameCancelledListener();
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			boardJSON = parseServerResponse(serverResponse);
			resumeOldBoard();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			isAsyncGetGameRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncGetGameRunning = true;
			compatInvalidateOptionsMenu();

			progressDialog = new ProgressDialog(getSherlockActivity());
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(getString(R.string.generic_game_fragment_getgame_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncGetGame.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.generic_game_fragment_getgame_progressdialog_title);
			progressDialog.show();
		}


	}




	private final class AsyncSendMove extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... params)
		{
			String serverResponse = null;

			if (!isCancelled())
			{
				try
				{
					final JSONObject jsonTeams = new JSONObject();
					jsonTeams.put("teams", createJSONTeams());

					final JSONObject jsonBoard = new JSONObject();
					jsonBoard.put("board", jsonTeams);

					if (!isCancelled())
					{
						final Person whoAmI = Utilities.getWhoAmI(getSherlockActivity());
						final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, whoAmI.getIdAsString()));
						nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_BOARD, jsonBoard.toString()));

						if (game.getId() == null || game.getId().isEmpty())
						{
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));

							if (!isCancelled())
							{
								try
								{
									serverResponse = ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_GAME_ADDRESS, nameValuePairs);
								}
								catch (final IOException e)
								{
									Log.e(LOG_TAG, "IOException error in AsyncSendMove - doInBackground()!", e);
								}
							}
						}
						else
						{
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, game.getId()));

							if (!isCancelled())
							{
								try
								{
									serverResponse = ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_MOVE_ADDRESS, nameValuePairs);
								}
								catch (final IOException e)
								{
									Log.e(LOG_TAG, "IOException error in AsyncSendMove - doInBackground()!", e);
								}
							}
						}
					}
				}
				catch (final JSONException e)
				{
					Log.e(LOG_TAG, "Error in creating JSON data to send to server.");
				}
			}

			return serverResponse;
		}


		@Override
		protected void onCancelled(final String serverResponse)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			isAsyncSendMoveRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			parseServerResponse(serverResponse);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			isAsyncSendMoveRunning = false;
			compatInvalidateOptionsMenu();
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncSendMoveRunning = true;
			compatInvalidateOptionsMenu();

			progressDialog = new ProgressDialog(getSherlockActivity());
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(getString(R.string.generic_game_fragment_sendmove_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncSendMove.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.generic_game_fragment_sendmove_progressdialog_title);
			progressDialog.show();
		}


		/**
		 * Creates a single JSONArray containing two JSONArrays: one JSONArray
		 * for each team on the game board. This method should never return
		 * null.
		 * 
		 * @return
		 * Returns a single JSONArray containing one JSONArray for each team on
		 * the game board.
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
		 * Creates a JSONArray for the given team. This method should never
		 * return null.
		 * 
		 * @param whichTeam
		 * The team that you want to create a JSONArray for.
		 * 
		 * @return
		 * Returns a JSONArray containing the given team.
		 */
		private JSONArray createJSONTeam(final byte whichTeam)
		{
			final JSONArray JSONTeam = new JSONArray();

			for (byte x = 0; x < board.getLengthHorizontal() && !isCancelled(); ++x)
			{
				for (byte y = 0; y < board.getLengthVertical() && !isCancelled(); ++y)
				{
					final Position position = board.getPosition(x, y);

					if (position.hasPiece())
					// this position has a piece in it
					{
						try
						{
							final JSONObject JSONPiece = createJSONPiece(whichTeam, position);

							if (JSONPiece != null)
							{
								JSONTeam.put(JSONPiece);
							}
						}
						catch (final JSONException e)
						{
							Log.e(LOG_TAG, "JSONException in createJSONTeam()! x: " + x + " y: " + y);
						}
					}
				}
			}

			return JSONTeam;
		}


	}




	/**
	 * Can be looked at as a main method for classes that extend this one. This
	 * method is very important because it must returns which layout this
	 * fragment will need to inflate. Returning the needed layout is really the
	 * only thing that this method needs to do. In fact, it's probably the only
	 * thing that this method should do. Stuff that's typically in an
	 * Activity's onCreate() method should instead be, for fragments, placed in
	 * the onActivityCreated() method.
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
	 * Attempts to create a JSONObject out of the given Position's GenericPiece
	 * object. It's possible that the given Position object has no GenericPiece
	 * at all however, and in that case this method will return null. There is
	 * another situation where null will be returned as well though: if this
	 * Position has a piece, but it doesn't belong to the team specified in the
	 * whichTeam parameter. 
	 * 
	 * @param whichTeam
	 * The team that you're currently trying to create a JSONArray for.
	 * 
	 * @param position
	 * The position that you're currently at on the game board.
	 * 
	 * @return
	 * If it was able to be created, this will return a JSONObject that
	 * represents the GenericPiece object at the given position on the game
	 * board. Be sure to check for a null response however.
	 * 
	 * @throws JSONException
	 * An error occurred when trying to create a JSONObject for the given
	 * position on the game board.
	 */
	protected abstract JSONObject createJSONPiece(final byte whichTeam, final Position position) throws JSONException;


	/**
	 * A Game specific implementation that looks at the given Position
	 * parameter and draws a piece on that position if / as necessary.
	 * 
	 * @param position
	 * The current Position object in the flush() loop.
	 */
	protected abstract void flush(final Position position);


	/**
	 * @return
	 * Returns the int value for the String to use as the title to display in
	 * the Action Bar.
	 */
	protected abstract int getTitle();


	/**
	 * Initialize the game board as if it's a <strong>brand new game</strong>.
	 */
	protected abstract void initNewBoard();


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
