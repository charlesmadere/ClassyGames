package edu.selu.android.classygames;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import edu.selu.android.classygames.games.Person;


public class NewGameActivity extends SherlockListActivity
{


	private ArrayList<Person> people;
	private PeopleAdapter peopleAdapter;
	private ProgressDialog progressDialog;
	private Runnable runnable;
	private Thread thread;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		people = new ArrayList<Person>();
		peopleAdapter = new PeopleAdapter(this, R.layout.new_game_activity_listview_item,people);
		setListAdapter(peopleAdapter);
		
		runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Utilities.ensureFacebookIsNotNull();
					AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(Utilities.facebook);
					mAsyncRunner.request("/me/friends", new FriendsRequestListener());
				}
				catch (Exception e)
				{
					Utilities.easyToastAndLogError(NewGameActivity.this, "Failed retrieving your Facebook friends.");
				}
			}
		};

		Thread thread = new Thread(null, runnable, "Populator");
		thread.start();
		progressDialog = ProgressDialog.show(NewGameActivity.this, "Please wait...", "Loading Friends...");
	}
	
	private Runnable returnRes = new Runnable()
	{
		@Override
		public void run()
		{
			if (people != null && people.size() >= 1)
			{
				peopleAdapter.notifyDataSetChanged();
				
				for (int i = 0; i < people.size(); ++i)
				{
					peopleAdapter.add(people.get(i));
				}
			}
			
			progressDialog.dismiss();
			peopleAdapter.notifyDataSetChanged();
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


	private class FriendsRequestListener extends BaseRequestListener
	{


		@Override
		public void onComplete(final String response, final Object state)
		{
			try
			{
				final JSONArray friends = Util.parseJson(response).getJSONArray("data");

	            final int friendsLength = friends.length();
	            for (int i = 0; i < friendsLength; ++i)
	            {
	            	JSONObject friend = friends.getJSONObject(i);
	            	people.add(new Person(friend.getInt("id"), friend.getString("name")));
	            }
	            runOnUiThread(returnRes);
			}
			catch (FacebookError e)
			{
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
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
