package edu.selu.android.classygames;


import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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

import edu.selu.android.classygames.games.GenericGame;
import edu.selu.android.classygames.games.Person;
import edu.selu.android.classygames.games.checkers.Checkers;


public class GamesListActivity extends SherlockListActivity
{


	private GamesListAdapter gamesAdapter;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);
		
		new AsyncPopulateGamesList().execute();
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
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


	private final class AsyncPopulateGamesList extends AsyncTask<Void, Void, ArrayList<GenericGame>>
	{


		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<GenericGame> doInBackground(final Void... v)
		{
			ArrayList<GenericGame> games = new ArrayList<GenericGame>();

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

				games.trimToSize();
			}
			catch (final Exception e)
			{
				Log.e(Utilities.LOG_TAG, e.getMessage());
			}

			return games;
		}


		@Override
		protected void onPostExecute(final ArrayList<GenericGame> games)
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
			progressDialog.setMessage("Loading all of your games...");
			progressDialog.setTitle(R.string.games_list_activity_progressdialog_title);
			progressDialog.show();
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
			typeface = Typeface.createFromAsset(getAssets(), Utilities.FONTS_BLUE_HIGHWAY_D);
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

			if (game != null)
			{
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.games_list_activity_listview_item_picture);
				if (viewHolder.picture != null)
				{

				}

				if (typeface == null)
				{
					typeface = Typeface.createFromAsset(getAssets(), Utilities.FONTS_BLUE_HIGHWAY_D);
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


		private class ViewHolder
		/**
		 * made this li'l class while trying to optimize our listview. apparently it
		 * helps performance
		 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
		 *
		 */
		{


			ImageView picture;
			OnClickListener onClickListener;
			TextView name;
			TextView time;


		}


	}


}
