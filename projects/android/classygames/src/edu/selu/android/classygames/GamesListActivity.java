package edu.selu.android.classygames;


import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.games.GenericGame;
import edu.selu.android.classygames.games.Person;
import edu.selu.android.classygames.games.checkers.Checkers;


public class GamesListActivity extends SherlockListActivity
{


	private ArrayList<GenericGame> gamesTurnTheirs;
	private ArrayList<GenericGame> gamesTurnYours;
	private GamesListAdapter gamesListAdapterTurnTheirs;
	private GamesListAdapter gamesListAdapterTurnYours;
	private ProgressDialog progressDialog;
	private Runnable viewGames;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		gamesTurnYours = new ArrayList<GenericGame>();
		gamesListAdapterTurnYours = new GamesListAdapter(this, R.layout.games_list_activity_listview_item, gamesTurnYours);
		setListAdapter(gamesListAdapterTurnYours);

		viewGames = new Runnable()
		{
			@Override
			public void run()
			{
				createGames();
			}
		};

		Thread thread = new Thread(null, viewGames, "MagentoBackground");
		thread.start();
		progressDialog = ProgressDialog.show(GamesListActivity.this, "Please wait...", "Retrieving data...");
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case R.id.actionbar_new_game:
				Utilities.easyToastAndLog(GamesListActivity.this, "NUEVO JUEGO!!");
				startActivity(new Intent(GamesListActivity.this, NewGameActivity.class));
				return true;

			case R.id.actionbar_refresh:
				Utilities.easyToastAndLog(GamesListActivity.this, "REFRESHENING");
				startActivity(new Intent(GamesListActivity.this, CheckersGameActivity.class));
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private void createGames()
	{
		try
		{
			gamesTurnYours = new ArrayList<GenericGame>();
			gamesTurnYours.add(new Checkers());
			gamesTurnYours.add(new Checkers(new Person("Bart")));
			gamesTurnYours.add(new Checkers());
			gamesTurnYours.add(new Checkers());
			gamesTurnYours.add(new Checkers());
			gamesTurnYours.add(new Checkers());
			Thread.sleep(5000);
			Log.i(Utilities.LOG_TAG, "Size: " + gamesTurnYours.size());
		}
		catch (Exception e)
		{
			Log.e(Utilities.LOG_TAG, "BACKGROUND_PROC " + e.getMessage());
		}

		runOnUiThread(returnRes);
	}


	private Runnable returnRes = new Runnable()
	{
		@Override
		public void run()
		{
			if (gamesTurnYours != null && gamesTurnYours.size() >= 1)
			{
				gamesListAdapterTurnYours.notifyDataSetChanged();

				for (int i = 0; i < gamesTurnYours.size(); ++i)
				{
					gamesListAdapterTurnYours.add(gamesTurnYours.get(i));
				}
			}

			progressDialog.dismiss();
			gamesListAdapterTurnYours.notifyDataSetChanged();
		}
	};


	private class GamesListAdapter extends ArrayAdapter<GenericGame>
	{
		private ArrayList<GenericGame> games;


		public GamesListAdapter(final Context context, final int textViewResourceId, final ArrayList<GenericGame> games)
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

			GenericGame game = games.get(position);

			if (game != null)
			{
				TextView name = (TextView) convertView.findViewById(R.id.games_list_activity_listview_item_name);
				TextView time = (TextView) convertView.findViewById(R.id.games_list_activity_listview_item_time);

				if (name != null)
				{
					name.setText(game.getPersonName());
				}

				if (time != null)
				{
					time.setText(game.getLastMoveTimeToString());
				}
			}

			return convertView;
		}
	}


}
