package edu.selu.android.classygames;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.LruCache;
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
import com.koushikdutta.urlimageviewhelper.DiskLruCache;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class NewGameActivity extends SherlockListActivity
{


	private DiskLruCache diskCache;
	private LruCache<Long, Bitmap> memoryCache;
	private PeopleAdapter peopleAdapter;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		// setup cache size for loading drawable images
		final int memClass = ((ActivityManager) NewGameActivity.this.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

		final int cacheSize = ImageCache.getCacheSize(memClass);

		memoryCache = new LruCache<Long, Bitmap> (cacheSize)
		{
			protected int sizeOf(final Long key, final Bitmap bitmap)
			{
				if (android.os.Build.VERSION.SDK_INT >= 12)
				// if the running version of Android is 3.1 (Honeycomb) or later
				{
					return bitmap.getByteCount();
				}
				else
				{
					return bitmap.getRowBytes() * bitmap.getHeight();
				}
			}
		};

		// try loading diskCache

		try
		{
			File cacheDir = getCacheDir(this, ImageCache.DISK_CACHE_SUBDIR);
			diskCache = DiskLruCache.open(cacheDir, ImageCache.APP_VERSION, ImageCache.VALUE_COUNT, ImageCache.DISK_CACHE_SIZE);
		} 
		catch (IOException e) 
		{
			Log.e(Utilities.LOG_TAG, "DiskCache instantiate failed: " + e);
		}

		new AsyncPopulateFacebookFriends().execute();
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.new_game_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case android.R.id.home:
				finish();
				return true;

			case R.id.new_game_activity_actionbar_refresh:
				// clear the cached friends list as the user decided to manually refresh his friends list
				NewGameActivity.this.getPreferences(MODE_PRIVATE).edit().clear().commit();

				new AsyncPopulateFacebookFriends().execute();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onResume()
	{
		super.onResume();
		Utilities.getFacebook().extendAccessTokenIfNeeded(NewGameActivity.this, null);
	}


	private final class AsyncPopulateFacebookFriends extends AsyncTask<Void, Integer, ArrayList<Person>>
	{


		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<Person> doInBackground(final Void... v)
		{
			ArrayList<Person> people = new ArrayList<Person>();

			// attempt to acquire the cached list of friends
			final SharedPreferences preferences = NewGameActivity.this.getPreferences(MODE_PRIVATE);

			@SuppressWarnings("unchecked")
			final Map<String, Long> map = (Map<String, Long>) preferences.getAll();

			if (map == null || map.isEmpty())
			// there was no cached list of friends
			{
				try
				{
					final String request = Utilities.getFacebook().request("me/friends");
					final JSONObject response = Util.parseJson(request);
					final JSONArray friends = response.getJSONArray("data");
					final int friendsLength = friends.length();
					publishProgress(friendsLength);

					for (int i = 0; i < friendsLength; ++i)
					{
						final JSONObject friend = friends.getJSONObject(i);
						final long id = friend.getLong("id");
						people.add(new Person(id, friend.getString("name")));

						publishProgress(i, 0);
					}
				}
				catch (final Exception e)
				{
					Log.e(Utilities.LOG_TAG, e.getMessage());
				}
			}
			else
			// there was a cached list of friends
			{
				final Set<String> set = map.keySet();
				publishProgress(map.size());

				int count = 0;
				for (Iterator<String> i = set.iterator(); i.hasNext(); ++count)
				{
					final String name = i.next();

					if (name != null && !name.isEmpty())
					{
						final Person person = new Person(map.get(name).longValue(), name);
						people.add(person);
					}

					publishProgress(count, 0);
				}
			}

			people.trimToSize();
			Collections.sort(people, new FacebookFriendsSorter());

			return people;
		}


		@Override
		protected void onPostExecute(final ArrayList<Person> people)
		{
			peopleAdapter = new PeopleAdapter(NewGameActivity.this, R.layout.new_game_activity_listview_item, people);
			setListAdapter(peopleAdapter);
			peopleAdapter.notifyDataSetChanged();

			final int peopleSize = people.size();
			if (peopleSize >= 1)
			{
				// cache friends list
				SharedPreferences.Editor editor = NewGameActivity.this.getPreferences(MODE_PRIVATE).edit();

				// make sure to clear out the existing friends list
				editor.clear();

				for (int i = 0; i < peopleSize; ++i)
				{
					final Person person = people.get(i);
					editor.putLong(person.getName(), person.getId());
				}

				editor.commit();
			}

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(NewGameActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(NewGameActivity.this.getString(R.string.new_game_activity_progressdialog_message));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle(R.string.new_game_activity_progressdialog_title);
			progressDialog.show();

			Utilities.getFacebook().extendAccessTokenIfNeeded(NewGameActivity.this, null);
		}


		@Override
		protected void onProgressUpdate(final Integer... i)
		{
			switch (i.length)
			{
				case 1:
					progressDialog.setMax(i[0].intValue());
					break;

				case 2:
					progressDialog.setProgress(i[0].intValue());
					break;
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

			final Person person = people.get(position);

			if (person != null)
			{
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.picture = (ImageView) convertView.findViewById(R.id.new_game_activity_listview_item_picture);
				viewHolder.picture.setImageResource(R.drawable.fb_placeholder);
				{
					Bitmap diskImage = ImageCache.getBitmapFromDiskCache(person.getId(), diskCache);
					Bitmap memoryImage = memoryCache.get(person.getId());
					viewHolder.picture.setTag(person.getId());
					
					if(memoryImage != null)
					{
						viewHolder.picture.setImageBitmap(memoryImage);
					}
					else if(diskImage != null)
					{
						viewHolder.picture.setImageBitmap(diskImage);
					}
					else
					{
						new AsyncPopulatePictures(viewHolder).execute(person);
					}
				}

				viewHolder.name = (TextView) convertView.findViewById(R.id.new_game_activity_listview_item_name);
				if (viewHolder.name != null)
				{
					viewHolder.name.setText(person.getName());
					viewHolder.name.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
				}

				viewHolder.onClickListener = new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Intent intent = new Intent(NewGameActivity.this, ConfirmGameActivity.class);
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID, person.getId());
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME, person.getName());

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


		private final class AsyncPopulatePictures extends AsyncTask<Person, Long, Drawable>
		{


			private Bitmap bitmap;
			private Drawable drawable;
			private String path;
			private ViewHolder viewHolder;


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
					drawable = Utilities.loadImageFromWebOperations(Utilities.FACEBOOK_GRAPH_API_URL + person[0].getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_SQUARE_SSL);
					bitmap = ((BitmapDrawable) drawable).getBitmap();
					ImageCache.addBitmapToCache(person[0].getId(),bitmap, memoryCache, diskCache);

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


	public static File getCacheDir(Context context, String uniqueName) 
	{
		//Check if storage is built in or mounted from sd, try to use mounted first
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}


}
