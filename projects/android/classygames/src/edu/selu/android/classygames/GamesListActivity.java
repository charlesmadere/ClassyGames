package edu.selu.android.classygames;


import java.util.ArrayList;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import edu.selu.android.classygames.games.GenericGame;
import edu.selu.android.classygames.games.Person;
import edu.selu.android.classygames.games.checkers.Checkers;


public class GamesListActivity extends SherlockListActivity
{


	private ArrayList<GenericGame> games;
	private GamesListAdapter gamesAdapter;
	private ProgressDialog progressDialog;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);
		
		games = new ArrayList<GenericGame>();
		gamesAdapter = new GamesListAdapter(this, R.layout.games_list_activity_listview_item, games);
		setListAdapter(gamesAdapter);

		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					// TODO: this code will eventually be replaced by an actual call to our
					// server. This call will ask the server for a games list

					games.add(new Checkers());
					games.add(new Checkers(new Person("Bart")));
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
					games.add(new Checkers());
				}
				catch (final Exception e)
				{
					Log.e(Utilities.LOG_TAG, e.getMessage());
				}

				runOnUiThread(populateGames);
			}
		};

		progressDialog = ProgressDialog.show(GamesListActivity.this, "Classy Games is working...", "Retrieving all of your games...");
		new Thread(null, runnable, "RetrieveGames").start();
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
			case R.id.actionbar_about:
				startActivity(new Intent(GamesListActivity.this, AboutActivity.class));
				return true;

			case R.id.actionbar_logout:
				startActivityForResult(new Intent(GamesListActivity.this, LogoutActivity.class), 0);
				return true;

			case R.id.actionbar_new_game:
				startActivity(new Intent(GamesListActivity.this, NewGameActivity.class));
				return true;

			case R.id.actionbar_refresh:
				startActivity(new Intent(GamesListActivity.this, CheckersGameActivity.class));
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private Runnable populateGames = new Runnable()
	{
		@Override
		public void run()
		{
			gamesAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}
	};


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == LogoutActivity.LOGGED_OUT)
		{
			finish();
		}
	}


	private class GamesListAdapter extends ArrayAdapter<GenericGame>
	{


		private ArrayList<GenericGame> games;
		private Typeface typeface;


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

			final GenericGame game = games.get(position);
			typeface = Typeface.createFromAsset(getAssets(), "fonts/blue_highway_d.ttf");

			if (game != null)
			{
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.games_list_activity_listview_item_picture);
				if (viewHolder.picture != null)
				{

				}

				viewHolder.name = (TextView) convertView.findViewById(R.id.games_list_activity_listview_item_name);
				if (viewHolder.name != null)
				{
					viewHolder.name.setText(game.getPerson().getName());
					viewHolder.name.setTypeface(typeface);
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
						Utilities.easyToastAndLog(GamesListActivity.this, "\"" + game.getPerson().getName() + "\" \"" + game.getLastMoveTime().toString() + "\"");
					}
				};

				convertView.setOnClickListener(viewHolder.onClickListener);
				convertView.setTag(viewHolder);
			}

			return convertView;
		}


		/**
		 * made this li'l class while trying to optimize our listview. apparently it
		 * helps performance
		 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
		 *
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
