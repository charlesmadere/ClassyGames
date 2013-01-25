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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.GenericPiece;
import edu.selu.android.classygames.games.Position;
import edu.selu.android.classygames.games.checkers.Board;
import edu.selu.android.classygames.games.checkers.Piece;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public abstract class GenericGameFragment extends SherlockFragment
{


	protected final static String LOG_TAG = Utilities.LOG_TAG + " - GenericGameFragment";


	/**
	 * Boolean indicating if the board is locked or not. Once the board has
	 * been locked it can only be locked by using undo.
	 */
	protected boolean boardLocked = false;


	/**
	 * A Person object that represents the Facebook friend that the current
	 * Android user is playing against. This object has the possibility of
	 * being null. <strong>DO NOT MODIFY THIS OBJECT ONCE IT HAS BEEN SET.
	 * </strong> If a GenericGameFragment has been instantiated and this object
	 * is null, then that means that this game is an in progress game and must
	 * be resumed. If this object is not null, then that means that this game
	 * is a brand new game.
	 */
	protected Person person;


	/**
	 * This game's Game object. This contains a whole bunch of necessary data
	 * such as the ID of the game as well as the Person object that the current
	 * Android user is playing against. <strong>DO NOT MODIFY THIS OBJECT ONCE
	 * IT HAS BEEN SET.</strong> If a GenericGameFragment has been instantiated
	 * and this object is null, then that means that this game is a brand new
	 * game. If this object is not null, then that means that this game is an
	 * in progress game and must be resumed (by downloading the game board from
	 * the server).
	 */
	protected Game game;


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


	/**
	 * One of this class's callback methods. This is fired in the event that
	 * an error was detected in some of the data needed to instantiate a game.
	 */
	private GenericGameFragmentOnDataErrorListener genericGameFragmentOnDataErrorListener;

	public interface GenericGameFragmentOnDataErrorListener
	{
		public void genericGameFragmentOnDataErrorListener();
	}


	private GenericGameFragmentOnDestroyViewListener genericGameFragmentOnDestroyViewListener;

	public interface GenericGameFragmentOnDestroyViewListener
	{
		public void genericGameFragmentOnDestroyViewListener();
	}




	/**
	 * <strong>NEVER USE THIS!</strong> Actually I'm not quite telling the
	 * truth: the only class that should ever use this constructor is the
	 * EmptyGameFragment class. But still, nothing else should ever use this.
	 */
	protected GenericGameFragment()
	{

	}


	/**
	 * <p><strong>Please be sure that you're using the proper constructor for
	 * the type of game that you're trying to create!</strong> The
	 * GenericGameFragment class has two constructors - it's very important to
	 * use the right one.</p>
	 * 
	 * <p>Use this constructor if you're creating a brand new game. This means
	 * that the user has gone to the NewGameFragment in the app and selected a
	 * Facebook friend to challenge. The board will be a completely default
	 * board and everything for this case.</p>
	 * 
	 * @param person
	 * The Person object that the current user is playing against. This object
	 * will be checked for validity. If this object is not valid then this
	 * class will refuse to instantiate fully.
	 */
	protected GenericGameFragment(final Person person)
	{
		this.person = person;
	}


	/**
	 * <p><strong>Please be sure that you're using the proper constructor for
	 * the type of game that you're trying to create!</strong> The
	 * GenericGameFragment class has two constructors - it's very important to
	 * use the right one.</p>
	 * 
	 * <p>Use this constructor if you're resuming an existing game. This means
	 * that the user has selected a game from the GamesListFragment. The board
	 * will have to be downloaded from the server and then parsed so that the
	 * existing game can be resumed.</p>
	 * 
	 * @param game
	 * The Game object that the current user selected in the GamesListFragment.
	 * This object will be checked for validity. If this object is not valid
	 * then this class will refuse to instantiate fully.
	 */
	protected GenericGameFragment(final Game game)
	{
		this.game = game;
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

		if (person == null)
		{
			if (game == null || !game.isValid())
			{
				genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
			}
			else
			{
				onBoardClick = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						onBoardClick(v);
					}
				};

//				initViews();
//				new AsyncGetGame().execute();
			}
		}
		else if (game == null)
		{
			if (person == null || !person.isValid())
			{
				genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
			}
			else
			{
				onBoardClick = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						onBoardClick(v);
					}
				};

				initViews();
				initNewBoard();
			}
		}
		else
		{
			genericGameFragmentOnDataErrorListener.genericGameFragmentOnDataErrorListener();
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
		inflater.inflate(R.menu.generic_game_fragment, menu);
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

			case R.id.generic_game_fragment_actionbar_send_move:
				new AsyncSendMove().execute();
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


	@Override
	public void onResume()
	{
		super.onResume();

		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		if (person == null)
		{
			actionBar.setTitle(getString(getTitle()) + " " + game.getPerson().getName());
		}
		else if (game == null)
		{
			actionBar.setTitle(getString(getTitle()) + " " + person.getName());
		}
		else
		{
			actionBar.setTitle(R.string.generic_game_fragment_title);
		}
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
			getSherlockActivity().invalidateOptionsMenu();
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
					Log.e(Utilities.LOG_TAG, "IOException error in AsyncGetGame - doInBackground()!", e);
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
		}


		@Override
		protected void onPreExecute()
		{
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

						if (game == null)
						{
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, person.getIdAsString()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, person.getName()));

							try
							{
								serverResponse = ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_GAME_ADDRESS, nameValuePairs);
							}
							catch (final IOException e)
							{
								Log.e(LOG_TAG, "IOException error in AsyncSendMove - doInBackground()!", e);
							}
						}
						else if (person == null)
						{
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));
							nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, game.getId()));

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
			final JSONArray jsonTeam = new JSONArray();

			for (byte x = 0; x < Board.LENGTH_HORIZONTAL && !isCancelled(); ++x)
			{
				for (byte y = 0; y < Board.LENGTH_VERTICAL && !isCancelled(); ++y)
				{
					final Position position = board.getPosition(x, y);

					if (position.hasPiece())
					// this position has a piece in it
					{
						final Piece piece = (Piece) position.getPiece();

						if (piece.isTeam(whichTeam))
						// this piece is of the given team 
						{
							try
							{
								final JSONArray jsonCoordinate = new JSONArray();
								jsonCoordinate.put(x);
								jsonCoordinate.put(y);

								final JSONObject jsonPiece = new JSONObject();
								jsonPiece.put("coordinate", jsonCoordinate);
								jsonPiece.put("type", piece.getType());

								jsonTeam.put(jsonPiece);
							}
							catch (final JSONException e)
							{
								Log.e(LOG_TAG, "Error in createJSONTeam(): x = " + x + ", y = " + y);
							}
						}
					}
				}
			}

			return jsonTeam;
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
