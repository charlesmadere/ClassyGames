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
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragment;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericBoard;
import edu.selu.android.classygames.games.checkers.Board;
import edu.selu.android.classygames.games.checkers.Piece;
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
	protected String gameId = null;


	/**
	 * Object representing the living person that I am playing against.
	 */
	protected Person personChallenged = null;


	/**
	 * JSON String downloaded from the server that represents the board.
	 */
	protected String boardJSON = null;


	/**
	 * The actual logical representation of the board.
	 */
	protected GenericBoard board = null;




	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{

		return super.onCreateView(inflater, container, savedInstanceState);
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


	@Override
	public void onClick(final View v)
	{
		if (!boardLocked)
		// only continue if the board is currently unlocked
		{
			onBoardClick(v);
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
			buildBoard();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GameFragment.this);
			progressDialog.setMessage(GameFragment.this.getString(R.string.checkers_game_fragment_getgame_progressdialog_message));
			progressDialog.setTitle(R.string.checkers_game_fragment_getgame_progressdialog_title);
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
				JSONArray teams = createJSONTeams();
				JSONObject board = new JSONObject();
				board.put("teams", teams);

				JSONObject object = new JSONObject();
				object.put("board", board);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				try
				{
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, Long.valueOf(personChallenged.getId()).toString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, personChallenged.getName()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, Long.valueOf(Utilities.getWhoAmI(GameFragment.this).getId()).toString()));
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

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			finish();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(CheckersGameFragment.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(CheckersGameFragment.this.getString(R.string.game_fragment_sendmove_progressdialog_message));
			progressDialog.setTitle(R.string.game_fragment_sendmove_progressdialog_title);
			progressDialog.show();

			CheckersGameFragment.this.setResult(GamesListFragmentActivity.NEED_TO_REFRESH);
		}


		private JSONArray createJSONTeams()
		{
			JSONArray teamGreen = createJSONTeam(true);
			JSONArray teamOrange = createJSONTeam(false);

			JSONArray teams = new JSONArray();
			teams.put(teamGreen);
			teams.put(teamOrange);

			return teams;
		}


		private JSONArray createJSONTeam(final boolean isPlayerGreen)
		{
			JSONArray team = new JSONArray();

			for (int x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
			{
				for (int y = 0; y < Board.LENGTH_VERTICAL; ++y)
				{
					if (!buttons[x][y].isEmpty() && buttons[x][y].isPlayerGreen() == isPlayerGreen)
					// this position has a piece in it that is of the given team color
					{
						try
						{
							JSONArray coordinate = new JSONArray();
							coordinate.put(x);
							coordinate.put(y);

							JSONObject piece = new JSONObject();
							piece.put("coordinate", coordinate);

							if( isKing(buttons[x][y]) == true )
							{
								piece.put("type", 2);
							}
							else
							{
								piece.put("type", 1);
							}

							team.put(piece);
						}
						catch (final JSONException e)
						{
							Log.e(Utilities.LOG_TAG, "Error in createJSONTeam, x = " + x + ", y = " + y);
						}
					}
				}
			}

			return team;
		}


	}


	private String parseServerResponse(final String jsonString)
	{
		if (jsonString == null || jsonString.isEmpty())
		{
			Log.e(Utilities.LOG_TAG, "Empty string received from server on send move!");
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
						return errorMessage;
					}
					catch (final JSONException e1)
					{
						Log.e(Utilities.LOG_TAG, "Data returned from server contained no error message.");
					}
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Couldn't grab result object from server response.");
			}
		}

		return null;
	}


	private void buildBoard()
	{
		if (boardJSON != null && !boardJSON.isEmpty())
		{
			final JSONObject JSON = new JSONObject(boardJSON);
			final JSONObject boardJSON = JSON.getJSONObject("board");
			final JSONArray teams = boardJSON.getJSONArray("teams");

			if (teams.length() == 2)
			{
				
			}
			else
			{
				Log.e(LOG_TAG, "JSON String has improper number of teams!");
			}

			for (int i = 0; i < teams.length(); ++i)
			{
				final JSONArray team = teams.getJSONArray(i);

				for (int j = 0; j < team.length(); ++j)
				{
					final JSONObject piece = team.getJSONObject(j);
					final int type = piece.getInt("type");
					JSONArray coordinates = piece.getJSONArray("coordinate");

					if (coordinates.length() == 2)
					{
						final Coordinate coordinate = new Coordinate(coordinates.getInt(0), coordinates.getInt(1));

						if (board.isPositionValid(coordinate))
						{
							buttons[x][y].setEmpty(false);

							if (i == 0)
							{
								buttons[x][y].setPlayerGreen(true);
								buttons[x][y].setImageResource(greenNormal);
								
								if (type == Piece.TYPE_NORMAL)
								{
									buttons[x][y].setCrown(false);
								}
								else if (type == Piece.TYPE_KING)
								{
									buttons[x][y].setCrown(true);
									buttons[x][y].setImageResource(greenKing);
								}
							}
							else
							{
								buttons[x][y].setPlayerGreen(false);
								buttons[x][y].setImageResource(orangeNormal);
								
								if (type == Piece.TYPE_NORMAL)
								{
									buttons[x][y].setCrown(false);
								}
								else if (type == Piece.TYPE_KING)
								{
									buttons[x][y].setCrown(true);
									buttons[x][y].setImageResource(orangeKing);
								}
							}
						}
						else
						{
							Log.e(Utilities.LOG_TAG, "Coordinate outside proper range: " + coordinate + ".");
						}
					}
				}
			}
		}
		else
		{
			Log.e(Utilities.LOG_TAG, "Tried to build a board from either a null or empty JSON String!");
		}
	}


	/**
	 * Checks to see which position on the board was clicked and then moves
	 * pieces accordingly.
	 * 
	 * @param v
	 * The View object that was clicked.
	 */
	protected abstract void onBoardClick(final View v);


	/**
	 * Initializes the game board as seen on the device's screen.
	 */
	protected abstract void initBoard();


	/**
	 * Initializes the game's pieces <strong>as if it's a brand new game
	 * </strong>. This method should <strong>only ever be used if it is a brand
	 * new game</strong>.
	 */
	protected abstract void initPieces();


}
