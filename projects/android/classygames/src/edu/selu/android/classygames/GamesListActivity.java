package edu.selu.android.classygames;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.Util;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.Game;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class GamesListActivity extends SherlockListActivity
{


	private GamesListAdapter gamesAdapter;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);

		new AsyncGetFacebookIdentificationAndGCMRegister().execute();
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == LogoutActivity.LOGGED_OUT)
		{
			finish();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{
			case R.id.games_list_activity_actionbar_about:
				startActivity(new Intent(GamesListActivity.this, AboutActivity.class));
				return true;

			case R.id.games_list_activity_actionbar_logout:
				startActivityForResult(new Intent(GamesListActivity.this, LogoutActivity.class), 0);
				return true;

			case R.id.games_list_activity_actionbar_new_game:
				startActivity(new Intent(GamesListActivity.this, NewGameActivity.class));
				return true;

			case R.id.games_list_activity_actionbar_refresh:
				new AsyncPopulateGamesList().execute();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onResume()
	{
		super.onResume();
		Utilities.getFacebook().extendAccessTokenIfNeeded(GamesListActivity.this, null);
	}


	private final class AsyncGetFacebookIdentificationAndGCMRegister extends AsyncTask<Void, Void, Person>
	{


		private ProgressDialog progressDialog;


		@Override
		protected Person doInBackground(final Void... v)
		{
			Utilities.getFacebook().extendAccessTokenIfNeeded(GamesListActivity.this, null);
			Person facebookIdentity = new Person();

			try
			{
				final String request = Utilities.getFacebook().request("me");
				final JSONObject me = Util.parseJson(request);
				final long id = me.getLong("id");
				final String name = me.getString("name");

				if (id >= 0 && name != null && !name.isEmpty())
				{
					facebookIdentity.setId(id);
					facebookIdentity.setName(name);
				}
			}
			catch (final Exception e)
			{
				Log.e(Utilities.LOG_TAG, "Exception during Facebook request or parse.", e);
			}

			return facebookIdentity;
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			Utilities.setWhoAmI(facebookIdentity);

			// register for GCM
			Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
			registrationIntent.putExtra("app", PendingIntent.getBroadcast(GamesListActivity.this, 0, new Intent(), 0));
			registrationIntent.putExtra("sender", SecretConstants.GOOGLE_PROJECT_ID);
			GamesListActivity.this.startService(registrationIntent);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			new AsyncPopulateGamesList().execute();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GamesListActivity.this);
			progressDialog.setMessage(GamesListActivity.this.getString(R.string.games_list_activity_init_progressdialog_message));
			progressDialog.setTitle(R.string.games_list_activity_init_progressdialog_title);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}


	}


	private final class AsyncPopulateGamesList extends AsyncTask<Void, Void, ArrayList<Game>>
	{


		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<Game> doInBackground(final Void... v)
		{
			ArrayList<Game> games = new ArrayList<Game>();

			try
			{
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, Long.valueOf(Utilities.getWhoAmI().getId()).toString()));

				// make a call to the server and grab the return JSON result
				final String jsonString = ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAMES_ADDRESS, nameValuePairs);
				games = parseServerResults(jsonString);
			}
			catch (final Exception e)
			{
				Log.e(Utilities.LOG_TAG, e.getMessage());
			}

			return games;
		}


		@Override
		protected void onPostExecute(final ArrayList<Game> games)
		{
			gamesAdapter = new GamesListAdapter(GamesListActivity.this, R.layout.new_game_activity_listview_item, games);
			setListAdapter(gamesAdapter);
			gamesAdapter.notifyDataSetChanged();
			
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GamesListActivity.this);
			progressDialog.setMessage(GamesListActivity.this.getString(R.string.games_list_activity_games_progressdialog_message));
			progressDialog.setTitle(R.string.games_list_activity_games_progressdialog_title);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}


		private ArrayList<Game> parseServerResults(final String jsonString)
		{
			ArrayList<Game> games = new ArrayList<Game>();

			try
			{
				final JSONObject jsonData = new JSONObject(jsonString);
				final JSONObject jsonResult = jsonData.getJSONObject(ServerUtilities.POST_DATA_RESULT);
				final JSONObject gameData = jsonResult.optJSONObject(ServerUtilities.POST_DATA_SUCCESS);

				if (gameData != null)
				{
					try
					{
						final JSONArray turnYours = gameData.getJSONArray(ServerUtilities.POST_DATA_TURN_YOURS);
						games.add(new Game(Game.TURN_YOURS, Game.TYPE_SEPARATOR));
						final int turnYoursSize = turnYours.length();

						for (int i = 0; i < turnYoursSize; ++i)
						{
							try
							{
								// grab the current game's JSONObject
								final JSONObject jsonGame = turnYours.getJSONObject(i);
								final Game game = parseGame(jsonGame);

								if (game != null)
								{
									games.add(game);
								}
							}
							catch (final JSONException e)
							{

							}
						}
					}
					catch (final JSONException e)
					{
						Log.d(Utilities.LOG_TAG, "Player has no games that are his own turn.");
					}

					try
					{
						final JSONArray turnTheirs = gameData.getJSONArray(ServerUtilities.POST_DATA_TURN_THEIRS);
						games.add(new Game(Game.TURN_THEIRS, Game.TYPE_SEPARATOR));
						final int turnTheirsSize = turnTheirs.length();

						for (int i = 0; i < turnTheirsSize; ++i)
						{
							try
							{
								// grab the current game's JSONObject
								final JSONObject jsonGame = turnTheirs.getJSONObject(i);
								final Game game = parseGame(jsonGame);

								if (game != null)
								{
									games.add(game);
								}
							}
							catch (final JSONException e)
							{

							}
						}
					}
					catch (final JSONException e)
					{
						Log.d(Utilities.LOG_TAG, "Player has no games that are the other person's turn.");
					}
				}
				else
				{
					final String successMessage = jsonResult.optString(ServerUtilities.POST_DATA_SUCCESS);

					if (successMessage != null)
					{
						Log.d(Utilities.LOG_TAG, "Server returned successful message: " + successMessage);
					}
					else
					{
						final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);
						Log.d(Utilities.LOG_TAG, "Server returned error message: " + errorMessage);
					}
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Server returned message that was unable to be properly parsed.", e);
			}

			games.trimToSize();
			return games;
		}


		private Game parseGame(final JSONObject game)
		{
			try
			{
				final long id = game.getLong(ServerUtilities.POST_DATA_ID); // id of the user that we're challenging
				final String name = game.getString(ServerUtilities.POST_DATA_NAME); // name of the user that we're challenging
				final String gameId = game.getString(ServerUtilities.POST_DATA_GAME_ID); // id of the game that we're playing
				final long timestamp = game.getLong(ServerUtilities.POST_DATA_LAST_MOVE); // time of this game's last move

				// create a game from this data
				return new Game(timestamp, new Person(id, name), gameId);
			}
			catch (final JSONException e)
			{

			}

			return null;
		}


	}


	private class GamesListAdapter extends ArrayAdapter<Game>
	{


		private ArrayList<Game> games;


		public GamesListAdapter(final Context context, final int textViewResourceId, final ArrayList<Game> games)
		{
			super(context, textViewResourceId, games);
			this.games = games;
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.games_list_activity_listview_item, null);
			}

			final Game game = games.get(position);

			if (game != null)
			{
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewHolder viewHolder = new ViewHolder();

				if (game.isTypeGame())
				{
					layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = layoutInflater.inflate(R.layout.games_list_activity_listview_item, null);
					viewHolder = new ViewHolder();
					viewHolder.picture = (ImageView) convertView.findViewById(R.id.games_list_activity_listview_item_picture);

					if (viewHolder.picture != null)
					{
						// TODO
						// insert code to load Images here
					}

					viewHolder.name = (TextView) convertView.findViewById(R.id.games_list_activity_listview_item_name);
					if (viewHolder.name != null)
					{
						viewHolder.name.setText(game.getPerson().getName());
						viewHolder.name.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
					}

					viewHolder.time = (TextView) convertView.findViewById(R.id.games_list_activity_listview_item_time);
					if (viewHolder.time != null)
					{
						viewHolder.time.setText(game.getLastMoveTime());
					}	

					viewHolder.onClickListener = new OnClickListener()
					{
						@Override
						public void onClick(final View v)
						{
							Intent intent = new Intent(GamesListActivity.this, CheckersGameActivity.class);
							intent.putExtra(CheckersGameActivity.INTENT_DATA_GAME_ID, game.getId());
							intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID, game.getPerson().getId());
							intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME, game.getPerson().getName());

							// start the ConfirmGameActivity with a bit of extra data. We're passing it both
							// the id and the name of the facebook person that the user clicked on
							startActivity(intent);
						}
					};

					convertView.setOnClickListener(viewHolder.onClickListener);
				}
				else
				{
					if (game.isTurnYours())
					{
						convertView = layoutInflater.inflate(R.layout.games_list_activity_listview_turn_yours, null);
						viewHolder.picture = (ImageView) convertView.findViewById(R.drawable.turn_yours);
					}
					else
					{
						convertView = layoutInflater.inflate(R.layout.games_list_activity_listview_turn_theirs, null);
						viewHolder.picture = (ImageView) convertView.findViewById(R.drawable.turn_theirs);
					}
				}

				convertView.setTag(viewHolder);
			}

			return convertView;
		}


		/**
		 * made this li'l class while trying to optimize our listview. apparently it
		 * helps performance
		 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
		 */
		private class ViewHolder
		{


			ImageView picture;
			OnClickListener onClickListener;
			TextView name;
			TextView time;


		}


	}


}
