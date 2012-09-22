package edu.selu.android.classygames;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.Util;

import edu.selu.android.classygames.games.Person;


public class NewGameActivity extends SherlockListActivity
{


	private ArrayList<Person> people;
	private PeopleAdapter peopleAdapter;
	private ProgressDialog progressDialog;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		people = new ArrayList<Person>();
		peopleAdapter = new PeopleAdapter(NewGameActivity.this, R.layout.new_game_activity_listview_item, people);
		setListAdapter(peopleAdapter);

		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Utilities.ensureFacebookIsNotNull();
					final JSONArray friends = Util.parseJson(Utilities.facebook.request("me/friends")).getJSONArray("data");

					final int friendsLength = friends.length();
					for (int i = 0; i < friendsLength; ++i)
					{
						final JSONObject friend = friends.getJSONObject(i);
						people.add(new Person(friend.getInt("id"), friend.getString("name")));
					}
				}
				catch (Exception e)
				{
					Utilities.easyToastAndLogError(NewGameActivity.this, "Failed retrieving your Facebook friends. " + e.getMessage());
				}

				runOnUiThread(populateFacebookFriends);
			}
		};

		progressDialog = ProgressDialog.show(NewGameActivity.this, "Please wait...", "Retrieving data...");
		new Thread(null, runnable, "acquireFacebookFriends").start();
	}


	private Runnable populateFacebookFriends = new Runnable()
	{
		@Override
		public void run()
		{
			peopleAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}
	};

	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private class PeopleAdapter extends ArrayAdapter<Person>
	{


		private ArrayList<Person> people;


		public PeopleAdapter(final Context context, final int textViewResourceId, final ArrayList<Person> people)
		{
			super(context, textViewResourceId, people);

			this.people = people;
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.new_game_activity_listview_item, null);
			}

			Person person = people.get(position);

			if (person != null)
			{
				ImageView picture = (ImageView) convertView.findViewById(R.id.new_game_activity_listview_item_picture);
				TextView name = (TextView) convertView.findViewById(R.id.new_game_activity_listview_item_name);

				if (picture != null)
				{

				}

				if (name != null)
				{
					name.setText(person.getName());
				}
			}

			return convertView;
		}
	}


}
