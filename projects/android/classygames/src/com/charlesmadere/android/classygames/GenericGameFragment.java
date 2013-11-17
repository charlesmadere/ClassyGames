package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.models.games.GenericPiece;
import com.charlesmadere.android.classygames.models.games.Position;
import com.charlesmadere.android.classygames.server.*;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.charlesmadere.android.classygames.views.BoardView;
import com.charlesmadere.android.classygames.views.PositionView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public abstract class GenericGameFragment extends SherlockFragment
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GenericGameFragment";


	private final static String PREFERENCES_NAME = "GenericGameFragment_Preferences";
	private final static String KEY_GAME_ID = "KEY_GAME_ID";
	private final static String KEY_WHICH_GAME = "KEY_WHICH_GAME";
	private final static String KEY_PERSON = "KEY_PERSON";
	private final static String BUNDLE_BOARD_JSON = "BUNDLE_BOARD_JSON";


	/**
	 * This game's Game object. This contains a whole bunch of necessary data
	 * such as the ID of the game as well as the Person object that the current
	 * Android user is playing against. <strong>DO NOT MODIFY THIS OBJECT ONCE
	 * IT HAS BEEN SET.</strong>
	 */
	protected Game game;


	/**
	 * JSONObject downloaded from the server that represents the board.
	 */
	private JSONObject boardJSON;


	/**
	 * The actual logical representation of the board.
	 */
	protected GenericBoard board;


	/**
	 * Holds a handle to the currently running (if it's currently running)
	 * AsyncGetGame AsyncTask. This could be null.
	 */
	private AsyncGetGame asyncGetGame;


	/**
	 * Holds a handle to a currently running (if it's currently running)
	 * ServerApi object.
	 */
	private ServerApi serverApiTask;


	/**
	 * Callback interface for the ServerApi class.
	 */
	private ServerApi.Listeners serverApiListeners;


	/**
	 * The position on the game board that the user just now selected.
	 */
	private PositionView positionSelectedCurrent;


	/**
	 * The position on the game board that the user selected last time.
	 */
	private PositionView positionSelectedPrevious;


	/**
	 * The BoardView layout element as seen on the device's screen.
	 */
	private BoardView boardView;


	/**
	 * Views that will be shown when downloading the game's data from the
	 * Classy Games server.
	 */
	private LinearLayout loading;
	private TextView loadingText;




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
		 * This is fired during this Fragment's onCreateOptionsMenu() method.
		 * Checks to see if the current device is considered by Android to be
		 * small. This be basically every phone.
		 * 
		 * @return
		 * Returns true if the current device is small.
		 */
		public boolean isDeviceSmall();


		/**
		 * This is fired in the event that an error was detected with some of
		 * the data needed to instantiate a game.
		 */
		public void onGetGameDataError();


		/**
		 * This is fired if the AsyncGetGame AsyncTask gets cancelled.
		 */
		public void onGetGameCancelled();


		/**
		 * This is fired when a move (or new game) is finished being sent to
		 * the server.
		 */
		public void onServerApiTaskFinished();


	}




	protected static Bundle prepareArguments(final String gameId, final byte whichGame, final Person person)
	{
		final Bundle arguments = new Bundle();
		arguments.putString(KEY_GAME_ID, gameId);
		arguments.putByte(KEY_WHICH_GAME, whichGame);
		arguments.putSerializable(KEY_PERSON, person);

		return arguments;
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
		return inflater.inflate(getGameView(), container, false);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final Bundle arguments = getArguments();

		if (arguments == null || arguments.isEmpty())
		// Check the arguments given to this Fragment. This Fragment requires
		// information regarding the game that it is going to load. If no data
		// is found, then this Fragment has encountered an error and must
		// terminate itself.
		{
			listeners.onGetGameDataError();
		}
		else
		{
			// Grabs data that was given to this Fragment. This data will then
			// be checked for validity.
			final String gameId = arguments.getString(KEY_GAME_ID);
			final byte whichGame = arguments.getByte(KEY_WHICH_GAME);
			final Person person = (Person) arguments.getSerializable(KEY_PERSON);

			if (Game.isWhichGameValid(whichGame) && person.isValid())
			// Check the data for validity. Note that we are not checking the
			// gameId String for validity. This is because it is possible for a
			// game to not have an ID. Brand new games do not have a game ID.
			{
				serverApiListeners = new ServerApi.Listeners()
				{
					@Override
					public void onCancel()
					{
						serverApiTask = null;
					}


					@Override
					public void onComplete(final String serverResponse)
					{
						serverApiTask = null;
						listeners.onServerApiTaskFinished();
					}


					@Override
					public void onDismiss()
					{
						serverApiTask = null;
					}
				};

				final View view = getView();
				boardView = (BoardView) view.findViewById(R.id.generic_game_fragment_board);
				loading = (LinearLayout) view.findViewById(R.id.generic_game_fragment_loading);
				loadingText = (TextView) view.findViewById(R.id.generic_game_fragment_loading_textview);

				boardView.setAllPositionViewOnClickListeners(new View.OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						if (positionSelectedCurrent == null)
						{
							positionSelectedCurrent = (PositionView) v;
							onBoardClick(positionSelectedCurrent);
						}
						else
						{
							positionSelectedPrevious = positionSelectedCurrent;
							positionSelectedCurrent = (PositionView) v;

							if (positionSelectedPrevious == positionSelectedCurrent)
							// The player has clicked the same position on
							// the board twice in a row. This is the
							// deselect action.
							{
								clearSelectedPositions();
							}
							else
							{
								onBoardClick(positionSelectedPrevious, positionSelectedCurrent);
							}
						}
					}
				});

				if (Game.isIdValid(gameId))
				// Check to see if we were given a valid game ID. We will only
				// have a valid game ID if we are trying to recreate an already
				// existing game. A brand new game will not have a game ID (and
				// will therefore fail this if statement).
				{
					game = new Game(person, whichGame, gameId);

					if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_BOARD_JSON))
					{
						try
						{
							boardJSON = new JSONObject(savedInstanceState.getString(BUNDLE_BOARD_JSON));

							loadPieceResources();
							resumeOldBoard();
							flush();
						}
						catch (final JSONException e)
						{
							getGame();
						}
					}
					else
					{
						getGame();
					}
				}
				else
				// We were not given a valid game ID. That means that we are
				// going to instantiate a brand new game.
				{
					game = new Game(person);

					try
					{
						loadPieceResources();
						initNewBoard();
						flush();
					}
					catch (final JSONException e)
					{
						listeners.onGetGameDataError();
					}
				}
			}
			else
			{
				listeners.onGetGameDataError();
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
		listeners = (Listeners) activity;
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		// Attempt to remove menu items from other fragments that may be
		// visible, as when a game is showing on screen they either make no
		// sense or could lead to unpredictable results.
		menu.removeItem(R.id.game_fragment_activity_menu_settings);
		menu.removeItem(R.id.games_list_fragment_menu_refresh);

		if (listeners.isDeviceSmall())
		{
			menu.removeItem(R.id.game_fragment_activity_menu_my_stats);
			menu.removeItem(R.id.game_fragment_activity_menu_new_game);
		}

		inflater.inflate(R.menu.generic_game_fragment, menu);

		// Code below enables / disables the send move and undo move Action Bar
		// buttons as necessary.
		if (asyncGetGame == null)
		{
			final MenuItem sendMoveMenuItem = menu.findItem(R.id.generic_game_fragment_menu_send_move);
			final MenuItem undoMoveMenuItem = menu.findItem(R.id.generic_game_fragment_menu_undo_move);

			if (board == null)
			{
				sendMoveMenuItem.setEnabled(false);
				undoMoveMenuItem.setEnabled(false);
			}
			else
			{
				final boolean hasMoveBeenMade = board.hasMoveBeenMade();
				sendMoveMenuItem.setEnabled(hasMoveBeenMade);
				undoMoveMenuItem.setEnabled(hasMoveBeenMade);
			}
		}

		// Here we only allow the Skip Move or Forfeit Game Action Bar buttons
		// to be shown if the game we're displaying in this Fragment has a game
		// ID.
		final Bundle arguments = getArguments();
		final String gameId = arguments.getString(KEY_GAME_ID);

		if (!Utilities.validString(gameId) || !Game.isIdValid(gameId))
		{
			menu.removeItem(R.id.generic_game_fragment_menu_skip_move);
			menu.removeItem(R.id.generic_game_fragment_menu_forfeit_game);
		}

		// load any menu items as added by the classes that extend this one
		createOptionsMenu(menu, inflater);

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onDestroyView()
	{
		cancelRunningAnyAsyncTask();
		super.onDestroyView();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.generic_game_fragment_menu_forfeit_game:
				forfeitGame();
				break;

			case R.id.generic_game_fragment_menu_send_move:
				sendMove();
				break;

			case R.id.generic_game_fragment_menu_skip_move:
				skipMove();
				break;

			case R.id.generic_game_fragment_menu_undo_move:
				undoMove();
				break;

			default:
			// If we hit this point then that means that the options item that
			// was selected is not any of the above choices. That means that it
			// was probably a menu item created by one of the classes that
			// extend this one. So this will check that class's menu items.
				return optionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onSaveInstanceState(final Bundle outState)
	{
		if (Game.isIdValid(game.getId()) && boardJSON != null)
		{
			final String boardJSONString = boardJSON.toString();

			if (Utilities.validString(boardJSONString))
			{
				outState.putString(BUNDLE_BOARD_JSON, boardJSONString);
			}
		}

		super.onSaveInstanceState(outState);
	}




	/**
	 * Attempts to cancel the currently running AsyncGetGame AsyncTask.
	 * 
	 * @return
	 * Returns true if the AsyncTask was cancelled.
	 */
	private boolean cancelRunningAsyncGetGame()
	{
		boolean cancelled = false;

		if (asyncGetGame != null)
		{
			asyncGetGame.cancel(true);
			cancelled = true;
		}

		return cancelled;
	}


	/**
	 * Attempts to cancel the currently running ServerApiTask.
	 * 
	 * @return
	 * Returns true if the ServerApiTask was cancelled.
	 */
	private boolean cancelRunningServerApiTask()
	{
		boolean cancelled = false;

		if (serverApiTask != null)
		{
			serverApiTask.cancel();
			cancelled = true;
		}

		return cancelled;
	}


	/**
	 * Cancels the currently running AsyncTask (if any).
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (!cancelRunningAsyncGetGame())
		{
			cancelRunningServerApiTask();
		}
	}


	/**
	 * Clears both selected positions variables. (They are both set to null.)
	 * The background image on both of them is also reset.
	 */
	protected void clearSelectedPositions()
	{
		if (positionSelectedPrevious != null)
		{
			positionSelectedPrevious.unselect();
			positionSelectedPrevious = null;
		}

		if (positionSelectedCurrent != null)
		{
			positionSelectedCurrent.unselect();
			positionSelectedCurrent = null;
		}
	}


	/**
	 * Renders all of the game's pieces on the board by first clearing all of
	 * the existing pieces from it and then placing all of the current pieces.
	 */
	protected void flush()
	{
		// clear all pieces from the board
		for (byte x = 0; x < board.getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < board.getLengthVertical(); ++y)
			{
				final PositionView positionView = boardView.getPosition(x, y);
				positionView.setImageDrawable(null);
			}
		}

		// place all pieces back onto the board
		for (byte x = 0; x < board.getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < board.getLengthVertical(); ++y)
			{
				final Position position = board.getPosition(x, y);

				if (position.hasPiece())
				{
					final GenericPiece piece = position.getPiece();
					final PositionView positionView = boardView.getPosition(x, y);

					// Let each GenericGameFragment class that extends from
					// this one handle the rest of the flush from here.
					flush(piece, positionView);
				}
			}
		}
	}


	/**
	 * Forfeits this game by asking the user if they're sure that they want to
	 * through a dialog box.
	 */
	private void forfeitGame()
	{
		if (Utilities.validString(game.getId()) && !isAnAsyncTaskRunning())
		{
			serverApiTask = new ServerApiForfeitGame(getSherlockActivity(), serverApiListeners, game);
			serverApiTask.execute();
		}
	}


	/**
	 * If the AsyncGetGame AsyncTask is not already running, then this will
	 * execute it.
	 */
	private void getGame()
	{
		if (asyncGetGame == null)
		{
			asyncGetGame = new AsyncGetGame();
			asyncGetGame.execute();
		}
	}


	/**
	 * @return
	 * Returns true if either the AsyncGetGame AsyncTask is running or if a
	 * ServerApi is running.
	 */
	private boolean isAnAsyncTaskRunning()
	{
		return asyncGetGame != null || serverApiTask != null;
	}


	/**
	 * Loads in the images to be used for the game pieces as shown on the game
	 * board. Checks user's preferences to see which image files to load.
	 */
	private void loadPieceResources()
	{
		final String blue = getString(R.string.blue);
		final String green = getString(R.string.green);
		final String orange = getString(R.string.orange);
		final String pink = getString(R.string.pink);

		final String defaultOpponentsColor = getDefaultOpponentsPieceColor();
		final String defaultPlayersColor = getDefaultPlayersPieceColor();

		final int opponentsColorKey = getSettingsKeyForOpponentsPieceColor();
		final int playersColorKey = getSettingsKeyForPlayersPieceColor();
		final String opponentsColorKeyString = getString(opponentsColorKey);
		final String playersColorKeyString = getString(playersColorKey);

		final SharedPreferences sPreferences = Utilities.getPreferences(getSherlockActivity());

		// Read in the colors that the player has selected to use for their
		// pieces. If the user has not set a color, the playerColor and
		// opponentColor Strings will both be set to the game's default color.
		String opponentsColor = sPreferences.getString(opponentsColorKeyString, defaultOpponentsColor);
		String playersColor = sPreferences.getString(playersColorKeyString, defaultPlayersColor);

		boolean recheckColorSettings;

		do
		{
			recheckColorSettings = false;

			if (opponentsColor.equalsIgnoreCase(playersColor))
			// Check to see if the color that the player has set for their own
			// color is the same as the one that they set for the opponent's color.
			// This if statement will validate as true if that is the case.
			{
				opponentsColor = defaultOpponentsColor;
				playersColor = defaultPlayersColor;

				// Change the value as saved in the user's preferences to the
				// default colors. This fixes the conflicting color issue.
				sPreferences.edit()
					.putString(opponentsColorKeyString, defaultOpponentsColor)
					.putString(playersColorKeyString, defaultPlayersColor)
					.commit();
			}

			// The code below will load BitmapDrawables for game pieces into
			// memory. This is done so that later when we draw these game pieces
			// onto the board, that draw process can be done very quickly as all of
			// the picture data will have already been loaded.

			final Resources res = getResources();

			if (opponentsColor.equalsIgnoreCase(blue))
			{
				loadBluePieceResources(res, false);
			}
			else if (opponentsColor.equalsIgnoreCase(green))
			{
				loadGreenPieceResources(res, false);
			}
			else if (opponentsColor.equalsIgnoreCase(orange))
			{
				loadOrangePieceResources(res, false);
			}
			else if (opponentsColor.equalsIgnoreCase(pink))
			{
				loadPinkPieceResources(res, false);
			}
			else
			{
				// If we've gotten to this point then that means that the user
				// setting for the opponent's piece color is corrupted or
				// malformed or something. So let's clear that setting back to
				// the default and load in the opponent's default piece colors.
				opponentsColor = "";
				playersColor = "";

				recheckColorSettings = true;
			}

			if (playersColor.equalsIgnoreCase(blue))
			{
				loadBluePieceResources(res, true);
			}
			else if (playersColor.equalsIgnoreCase(green))
			{
				loadGreenPieceResources(res, true);
			}
			else if (playersColor.equalsIgnoreCase(orange))
			{
				loadOrangePieceResources(res, true);
			}
			else if (playersColor.equalsIgnoreCase(pink))
			{
				loadPinkPieceResources(res, true);
			}
			else
			{
				// If we've gotten to this point then that means that the user
				// setting for the player's piece color is corrupted or
				// malformed or something. So let's clear that setting back to
				// the default and load in the player's default piece colors.
				opponentsColor = "";
				playersColor = "";

				recheckColorSettings = true;
			}
		}
		while (recheckColorSettings);
	}


	public boolean onBackPressed()
	{
		if (isAnAsyncTaskRunning())
		{
			cancelRunningAnyAsyncTask();
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Reads in a JSON response String as received from the web server and
	 * pulls the needed information out of it. If there is an error during this
	 * process, null is returned.
	 * 
	 * @param serverResponse
	 * The JSON response String as received from the web server. This method
	 * <strong>does</strong> check to see if this passed in String is either
	 * null or empty. In that case, the method will immediately log that error
	 * and then return null.
	 * 
	 * @return
	 * Returns a JSONObject containing only the necessary game information.
	 * But, if there is an error in the parsing process, this method will log
	 * some stuff and then return null.
	 */
	private JSONObject parseServerResponse(final String serverResponse)
	{
		JSONObject parsedServerResponse = null;

		if (Utilities.validString(serverResponse))
		{
			try
			{
				final JSONObject jsonData = new JSONObject(serverResponse);
				final JSONObject jsonResult = jsonData.getJSONObject(Server.POST_DATA_RESULT);

				try
				{
					final String successMessage = jsonResult.getString(Server.POST_DATA_SUCCESS);
					Log.i(LOG_TAG, "Server returned success message: " + successMessage);

					parsedServerResponse = new JSONObject(successMessage);
				}
				catch (final JSONException e)
				{
					try
					{
						final String errorMessage = jsonResult.getString(Server.POST_DATA_ERROR);
						Log.e(LOG_TAG, "Server returned error message: " + errorMessage);
					}
					catch (final JSONException e1)
					{
						Log.e(LOG_TAG, "Server response is nothing much.");
					}
				}
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "Server response is massively malformed.");
			}
		}
		else
		{
			Log.e(LOG_TAG, "Either null or empty String was attempted to be parsed!");
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
		if (boardJSON == null)
		{
			Log.e(LOG_TAG, "Tried to build the board from a null JSONObject!");
			listeners.onGetGameDataError();
		}
		else
		{
			try
			{
				resumeOldBoard(boardJSON);
			}
			catch (final JSONException e)
			{
				Log.e(LOG_TAG, "resumeOldBoard(): boardJSON is massively malformed.", e);
				listeners.onGetGameDataError();
			}
		}
	}


	/**
	 * If the AsyncSendMove AsyncTask is not already running, then this will
	 * execute it.
	 */
	private void sendMove()
	{
		if (!isAnAsyncTaskRunning())
		{
			final boolean askUserToExecute = Utilities.checkIfSettingIsEnabled(getSherlockActivity(),
				R.string.settings_key_ask_before_sending_move, true);

			serverApiTask = new ServerApiSendMove(getSherlockActivity(), serverApiListeners, game, board);
			serverApiTask.execute(askUserToExecute);
		}
	}


	/**
	 * Skips the user's turn by asking the user if they're sure that they want
	 * to using a dialog box.
	 */
	private void skipMove()
	{
		if (Utilities.validString(game.getId()) && !isAnAsyncTaskRunning())
		{
			serverApiTask = new ServerApiSkipMove(getSherlockActivity(), serverApiListeners, game);
			serverApiTask.execute();
		}
	}


	/**
	 * Undoes the user's last move on the board. Unlocks the board, allowing
	 * the user to make a different move on the board.
	 */
	private void undoMove()
	{
		if (board.isBoardLocked() || board.hasMoveBeenMade())
		{
			clearSelectedPositions();

			try
			{
				board.reset();
			}
			catch (final JSONException e)
			{
				listeners.onGetGameDataError();
			}

			flush();
			getSherlockActivity().supportInvalidateOptionsMenu();
		}
	}




	/**
	 * An AsyncTask that will download the current state of the game board from
	 * the server.
	 */
	private final class AsyncGetGame extends AsyncTask<Void, Void, String>
	{


		private SherlockFragmentActivity fragmentActivity;


		private AsyncGetGame()
		{
			fragmentActivity = getSherlockActivity();
		}


		@Override
		protected String doInBackground(final Void... params)
		{
			String serverResponse = null;

			if (!isCancelled())
			{
				final SharedPreferences sPreferences = fragmentActivity.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
				final String boardJSON = sPreferences.getString(game.getId(), null);

				if (Utilities.validString(boardJSON))
				{
					serverResponse = boardJSON;
				}
				else if (Utilities.checkForNetworkConnectivity(fragmentActivity))
				{
					final ApiData data = new ApiData()
						.addKeyValuePair(Server.POST_DATA_ID, game.getId());

					try
					{
						serverResponse = Server.postToServerGetGame(data);

						// store the just now downloaded instance of the board
						sPreferences.edit()
							.putString(game.getId(), serverResponse)
							.commit();
					}
					catch (final IOException e)
					{
						Log.e(LOG_TAG, "IOException error in AsyncGetGame - doInBackground()!", e);
					}
				}
			}

			return serverResponse;
		}


		private void cancelled()
		{
			setRunningState(false);
			listeners.onGetGameCancelled();
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final String serverResponse)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			boardJSON = parseServerResponse(serverResponse);

			loading.setVisibility(View.GONE);
			boardView.setVisibility(View.VISIBLE);

			loadPieceResources();
			resumeOldBoard();
			flush();

			setRunningState(false);
		}


		@Override
		protected void onPreExecute()
		{
			setRunningState(true);

			boardView.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			loadingText.setText(getString(getLoadingText(), game.getPerson().getName()));
		}


		/**
		 * Use this method to reset the options menu. This should only be used when
		 * an AsyncTask is running (or has just finished).
		 * 
		 * @param isRunning
		 * True if the AsyncTask is just starting to run, false if it's just
		 * finished.
		 */
		private void setRunningState(final boolean isRunning)
		{
			if (!isRunning)
			{
				asyncGetGame = null;
			}

			fragmentActivity.supportInvalidateOptionsMenu();
		}


	}




	/**
	 * Clears all saved Board data that was retrieved from the Classy Games
	 * server.
	 *
	 * @param context
	 * The Context of the Activity or Fragment that you're calling this method
	 * from.
	 */
	public static void clearCachedBoards(final Context context)
	{
		context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().commit();
	}




	/**
	 * This method is run at the end of the Android onCreateOptionsMenu method.
	 * Use this method to add menu items to this Fragment's menu.
	 *
	 * @param menu
	 * The Menu object as given by Android.
	 *
	 * @param inflater
	 * The MenuInflater object as given by Android. Use this to inflate a menu
	 * XML file.
	 */
	protected abstract void createOptionsMenu(final Menu menu, final MenuInflater inflater);


	/**
	 * This method is run at the end of the Android onOptionsItemSelected
	 * method if the selected MenuItem was not already found. Use this to check
	 * to see which MenuItem was selected by the user.
	 *
	 * @param item
	 * The selected MenuItem as given by Android.
	 *
	 * @return
	 * Returns true if the selected MenuItem was found. Returns
	 * super.onOptionsItemSelected(item) if the MenuItem was not found.
	 */
	protected abstract boolean optionsItemSelected(final MenuItem item);


	/**
	 * A Game specific implementation that looks at the given PositionView and
	 * Position parameter and draws a piece on that position if / as necessary.
	 * This method will only be run for positions on the board that have a
	 * piece in them.
	 *
	 * @param piece
	 * The GenericPiece object that is located at this position on the board.
	 *
	 * @param positionView
	 * The current BoardView's PositionView object in the flush() loop.
	 */
	protected abstract void flush(final GenericPiece piece, final PositionView positionView);


	/**
	 * @return
	 * Returns the actual String for the players's default piece color.
	 */
	protected abstract String getDefaultPlayersPieceColor();


	/**
	 * @return
	 * Returns the actual String for the opponent's default piece color.
	 */
	protected abstract String getDefaultOpponentsPieceColor();


	/**
	 * @return
	 * Returns the int value for the XML layout to use.
	 */
	protected abstract int getGameView();


	/**
	 * @return
	 * Returns the int value for the XML String to use as the text to display
	 * as a loading message when the AsyncGetGame AsyncTask is running.
	 */
	protected abstract int getLoadingText();


	/**
	 * @return
	 * Returns the int string ID for the player's settings key.
	 */
	protected abstract int getSettingsKeyForPlayersPieceColor();


	/**
	 * @return
	 * Returns the int string ID for the opponent's settings key.
	 */
	protected abstract int getSettingsKeyForOpponentsPieceColor();


	/**
	 * Initialize the game board as if it's a <strong>brand new game</strong>.
	 * 
	 * @throws JSONException
	 * This should never happen. But still, I guess it still possibly could. So
	 * definitely prepare for that situation in some way. If this Exception is
	 * thrown then it can be assumed that the game we were trying to initialize
	 * is corrupt. A corrupt game should <b>never, ever</b> be played. So this
	 * should trigger the GenericGameFragment to destroy itself to prevent
	 * some sort of malformity from hitting the Classy Games server.
	 */
	protected abstract void initNewBoard() throws JSONException;


	/**
	 * Loads in the images used for the game pieces as shown on the game board.
	 *
	 * @param res
	 * A handle to the results of a call to the getResources() method.
	 *
	 * @param isPlayersColor
	 * This value will be true if this color is what should be loaded for the
	 * player's pieces. This value will be false if this color is what should
	 * be loaded for the opponent's pieces.
	 */
	protected abstract void loadBluePieceResources(final Resources res, final boolean isPlayersColor);


	/**
	 * Loads in the images used for the game pieces as shown on the game board.
	 *
	 * @param res
	 * A handle to the results of a call to the getResources() method.
	 *
	 * @param isPlayersColor
	 * This value will be true if this color is what should be loaded for the
	 * player's pieces. This value will be false if this color is what should
	 * be loaded for the opponent's pieces.
	 */
	protected abstract void loadGreenPieceResources(final Resources res, final boolean isPlayersColor);


	/**
	 * Loads in the images used for the game pieces as shown on the game board.
	 *
	 * @param res
	 * A handle to the results of a call to the getResources() method.
	 *
	 * @param isPlayersColor
	 * This value will be true if this color is what should be loaded for the
	 * player's pieces. This value will be false if this color is what should
	 * be loaded for the opponent's pieces.
	 */
	protected abstract void loadOrangePieceResources(final Resources res, final boolean isPlayersColor);


	/**
	 * Loads in the images used for the game pieces as shown on the game board.
	 *
	 * @param res
	 * A handle to the results of a call to the getResources() method.
	 *
	 * @param isPlayersColor
	 * This value will be true if this color is what should be loaded for the
	 * player's pieces. This value will be false if this color is what should
	 * be loaded for the opponent's pieces.
	 */
	protected abstract void loadPinkPieceResources(final Resources res, final boolean isPlayersColor);


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces and / or performs actions accordingly. This method will only be
	 * called if there is no previous position on the board that the user
	 * clicked on.
	 *
	 * @param positionCurrent
	 * The PositionView object that was just now clicked on.
	 */
	protected abstract void onBoardClick(final PositionView positionCurrent);


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces and / or performs actions accordingly.
	 * 
	 * @param positionPrevious
	 * The PositionView object that was previously clicked on.
	 *
	 * @param positionCurrent
	 * The PositionView object that was just now clicked on.
	 */
	protected abstract void onBoardClick(final PositionView positionPrevious, final PositionView positionCurrent);


	/**
	 * Creates a Board object out of the given JSON String.
	 * 
	 * @param boardJSON
	 * The JSONObject that represents the game board as was received from the
	 * Classy Games server.
	 */
	protected abstract void resumeOldBoard(final JSONObject boardJSON) throws JSONException;




}
