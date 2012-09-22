package edu.selu.android.classygames;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.Util;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

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
					final String request = Utilities.getFacebook().request("me/friends");
					final JSONObject response = Util.parseJson(request);
					final JSONArray friends = response.getJSONArray("data");

					final int friendsLength = friends.length();
					for (int i = 0; i < friendsLength; ++i)
					{
						final JSONObject friend = friends.getJSONObject(i);
						final long id = friend.getLong("id");
						people.add(new Person(id, friend.getString("name")));

//						UrlImageViewHelper.loadUrlDrawable(NewGameActivity.this, "https://graph.facebook.com/" + id + "/picture?return_ssl_resources=1");
					}

					people.trimToSize();

					// TODO: sort the arraylist of facebook friends into alphabetical order. currently it's
					// sorted by id, which is how facebook delivers the data to us.
				}
				catch (final Exception e)
				{
					Log.e(Utilities.LOG_TAG, e.getMessage());
				}

				runOnUiThread(populateFacebookFriends);
			}
		};

		progressDialog = ProgressDialog.show(NewGameActivity.this, "Facebook is working...", "Retrieving all of your Facebook friends...");
		new Thread(null, runnable, "RetrieveFacebookFriends").start();
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
		private ViewHolder viewHolder;


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

			final Person person = people.get(position);

			/*if (person != null)
			{
				TextView textView = (TextView) convertView.findViewById(R.id.new_game_activity_listview_item);

				if (textView != null)
				{
					textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					textView.setText(person.getName());
				}
			}*/

			if (person != null)
			{
				viewHolder = new ViewHolder();
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.new_game_activity_listview_item_picture);
				if (viewHolder.picture != null)
				{
					UrlImageViewHelper.setUrlDrawable(viewHolder.picture, "https://graph.facebook.com/" + person.getId() + "/picture?return_ssl_resources=1");
				}

				viewHolder.name = (TextView) convertView.findViewById(R.id.new_game_activity_listview_item_name);
				if (viewHolder.name != null)
				{
					viewHolder.name.setText(person.getName());
				}

				viewHolder.onClickListener = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Utilities.easyToastAndLog(NewGameActivity.this, "\"" + person.getName() + "\" \"" + person.getId() + "\"");
					}
				};

				convertView.setOnClickListener(viewHolder.onClickListener);
				convertView.setTag(viewHolder);
			}

			return convertView;
		}


		/**
		 * made this li'l class while trying to optimize our listview
		 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
		 * apparently it helps performance
		 *
		 */
		private class ViewHolder
		{


			public ImageView picture;
			public OnClickListener onClickListener;
			public TextView name;


		}


	}


}
