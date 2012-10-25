package edu.selu.android.classygames;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.Util;

import edu.selu.android.classygames.games.Person;


public class NewGameActivity extends SherlockListActivity
{


	private PeopleAdapter peopleAdapter;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		new AsyncPopulateFacebookFriends().execute();
	}


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

	
	private final class AsyncPopulateFacebookFriends extends AsyncTask<Void, Long, ArrayList<Person>>
	{


		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<Person> doInBackground(final Void... v)
		{
			ArrayList<Person> people = new ArrayList<Person>();

			try
			{
				final String request = Utilities.getFacebook().request("me/friends");
				final JSONObject response = Util.parseJson(request);
				final JSONArray friends = response.getJSONArray("data");
				final int friendsLength = friends.length();
				publishProgress((long) friendsLength);

				for (int i = 0; i < friendsLength; ++i)
				{
					final JSONObject friend = friends.getJSONObject(i);
					final long id = friend.getLong("id");
					people.add(new Person(id, friend.getString("name")));

					publishProgress((long) i, id);
				}

				people.trimToSize();
				Collections.sort(people, new FacebookFriendsSorter());
			}
			catch (final Exception e)
			{
				Log.e(Utilities.LOG_TAG, e.getMessage());
			}

			return people;
		}


		@Override
		protected void onPostExecute(final ArrayList<Person> people)
		{
			peopleAdapter = new PeopleAdapter(NewGameActivity.this, R.layout.new_game_activity_listview_item, people);
			setListAdapter(peopleAdapter);
			peopleAdapter.notifyDataSetChanged();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(NewGameActivity.this);
			progressDialog.setMessage(NewGameActivity.this.getString(R.string.new_game_activity_progressdialog_message));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle(R.string.new_game_activity_progressdialog_title);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			
			progressDialog.show();
		}


		@Override
		protected void onProgressUpdate(final Long... l)
		{
			switch (l.length)
			{
				case 1:
					progressDialog.setMax(l[0].intValue());
					break;

				case 2:
					progressDialog.setProgress(l[0].intValue());
					break;
			}
		}


	}


	private class PeopleAdapter extends ArrayAdapter<Person>
	{


		private ArrayList<Person> people;
		private Typeface typeface;


		public PeopleAdapter(final Context context, final int textViewResourceId, final ArrayList<Person> people)
		{
			super(context, textViewResourceId, people);

			this.people = people;
			typeface = Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D);
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

			if (person != null)
			{
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.new_game_activity_listview_item_picture);
				//TODO: fix image sizes
				//viewHolder.picture.setScaleType(ScaleType.CENTER_CROP);
				viewHolder.picture.setImageResource(R.drawable.fb_placeholder);
				{
					viewHolder.picture.setTag(person.getId());
					new AsyncPopulatePictures(viewHolder).execute(person);
				}

				if (typeface == null)
				{
					typeface = Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D);
				}

				viewHolder.name = (TextView) convertView.findViewById(R.id.new_game_activity_listview_item_name);
				if (viewHolder.name != null)
				{
					viewHolder.name.setText(person.getName());
					viewHolder.name.setTypeface(typeface);
				}

				viewHolder.onClickListener = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Intent intent = new Intent(NewGameActivity.this, ConfirmGameActivity.class);
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_ID, person.getId());
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_NAME, person.getName());

						// start the ConfirmGameActivity with a bit of extra data. We're passing it both
						// the id and the name of the facebook person that the user clicked on
						startActivity(intent);
					}
				};

				convertView.setOnClickListener(viewHolder.onClickListener);
				convertView.setTag(viewHolder);
			}

			return convertView;
		}


		private final class AsyncPopulatePictures extends AsyncTask<Person, Long ,Drawable>
		{


			private Drawable drawable;
			private ViewHolder viewHolder;
			private String path;


			AsyncPopulatePictures(final ViewHolder viewHolder)
			{
				super();
				this.viewHolder = viewHolder;
				path = this.viewHolder.picture.getTag().toString();
			}


			@Override
			protected Drawable doInBackground(final Person... person) 
			{
				if (!viewHolder.picture.getTag().toString().equals(path))
				{
					this.cancel(true);
					return null;
				}
				else
				{
					try
					{
						drawable = Utilities.loadImageFromWebOperations(Utilities.FACEBOOK_GRAPH_API_URL + person[0].getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SQUARE_SSL);
					}
					catch(Exception e)
					{
						Log.e("Classy Games", "Image Load Failed: " + e);
					}

					return drawable;
				}
			}


			@Override
			protected void onPostExecute(final Drawable result)
			{
				viewHolder.picture.setImageDrawable(result);
			}


		}


		/**
		 * made this li'l class while trying to optimize our listview. apparently using
		 * something like this helps performance
		 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
		 *
		 */
		private class ViewHolder
		{


			public ImageView picture;
			public OnClickListener onClickListener;
			public TextView name;
			

		}


	}


	private class FacebookFriendsSorter implements Comparator<Person>
	{


		@Override
		public int compare(final Person geo, final Person jarrad)
		{
			return geo.getName().compareToIgnoreCase(jarrad.getName());
		}


	}


}
