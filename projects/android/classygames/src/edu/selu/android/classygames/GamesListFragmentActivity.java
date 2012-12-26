package edu.selu.android.classygames;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.Util;
import com.koushikdutta.urlimageviewhelper.DiskLruCache;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.SecretConstants;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class GamesListFragmentActivity extends SherlockFragmentActivity
{


	private boolean justReturnedHere = false;
	private DiskLruCache diskCache;
	private LruCache<Long, Bitmap> memoryCache;
	private GamesListAdapter gamesAdapter;


	public final static int NEED_TO_REFRESH = 1;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_fragment_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);

		// setup cache size for loading drawable images
		final int memClass = ((ActivityManager) GamesListFragmentActivity.this.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

		final int cacheSize = ImageCache.getCacheSize(memClass);

		memoryCache = new LruCache<Long, Bitmap> (cacheSize)
		{
			@SuppressLint("NewApi")
			protected int sizeOf(final Long key, final Bitmap bitmap)
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
				// if the running version of Android is API Level 12 and higher (Honeycomb 3.1 and up)
				// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
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
		catch (final IOException e) 
		{
			Log.e(Utilities.LOG_TAG, "DiskCache instantiate failed.", e);
		}
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode)
		{
			case NEED_TO_REFRESH:
				justReturnedHere = true;
				break;

			case LogoutFragment.LOGGED_OUT:
				finish();
				break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_fragment_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{
			case R.id.games_list_fragment_activity_actionbar_about:
				startActivity(new Intent(GamesListFragmentActivity.this, AboutFragment.class));
				return true;

			case R.id.games_list_fragment_activity_actionbar_logout:
				startActivityForResult(new Intent(GamesListFragmentActivity.this, LogoutFragment.class), 0);
				return true;

			case R.id.games_list_fragment_activity_actionbar_new_game:
				startActivityForResult(new Intent(GamesListFragmentActivity.this, NewGameActivity.class), 0);
				return true;

			case R.id.games_list_fragment_activity_actionbar_refresh:
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

		if (Utilities.getWhoAmI(this) == null)
		{
			new AsyncGetFacebookIdentificationAndGCMRegister().execute();
		}
		else if (justReturnedHere || gamesAdapter == null || gamesAdapter.isEmpty())
		{
			justReturnedHere = false;
			new AsyncPopulateGamesList().execute();
		}
	}


	private final class AsyncGetFacebookIdentificationAndGCMRegister extends AsyncTask<Void, Void, Person>
	{


		private ProgressDialog progressDialog;


		@Override
		protected Person doInBackground(final Void... v)
		{
			// TODO
			// convert the below facebook call into the new, non deprecated stuff

			try
			{
				final String request = Utilities.getFacebook().request("me");
				final JSONObject me = Util.parseJson(request);
				final long id = me.getLong("id");
				final String name = me.getString("name");

				if (id >= 0 && name != null && !name.isEmpty())
				{
					return new Person(id, name);
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "JSONException during Facebook request or parse.", e);
			}
			catch (final MalformedURLException e)
			{
				Log.e(Utilities.LOG_TAG, "MalformedURLException during Facebook request or parse.", e);
			}
			catch (final IOException e)
			{
				Log.e(Utilities.LOG_TAG, "IOException during Facebook request or parse.", e);
			}

			return null;
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			Utilities.setWhoAmI(GamesListFragmentActivity.this, facebookIdentity);

			// register for GCM
			Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
			registrationIntent.putExtra("app", PendingIntent.getBroadcast(GamesListFragmentActivity.this, 0, new Intent(), 0));
			registrationIntent.putExtra("sender", SecretConstants.GOOGLE_PROJECT_ID);
			GamesListFragmentActivity.this.startService(registrationIntent);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			new AsyncPopulateGamesList().execute();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GamesListFragmentActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(GamesListFragmentActivity.this.getString(R.string.games_list_fragment_activity_init_progressdialog_message));
			progressDialog.setTitle(R.string.games_list_fragment_activity_init_progressdialog_title);
			progressDialog.show();

			Utilities.getFacebook().extendAccessTokenIfNeeded(GamesListFragmentActivity.this, null);
		}


	}


	private final class AsyncPopulateGamesList extends AsyncTask<Void, Integer, ArrayList<Game>>
	{


		private byte toastToShow;
		private final static byte TOAST_NONE = 0;
		private final static byte TOAST_NO_GAMES = 1;
		private final static byte TOAST_SERVER_ERROR = 2;
		private final static byte TOAST_SERVER_RESPONSE_ERROR = 3;

		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<Game> doInBackground(final Void... v)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (final InterruptedException e)
			{
				Log.e(Utilities.LOG_TAG, "AsyncPopulateGamesList interrupted when trying to sleep!", e);
			}

			ArrayList<Game> games = new ArrayList<Game>();

			try
			{
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, Long.valueOf(Utilities.getWhoAmI(GamesListFragmentActivity.this).getId()).toString()));

				// make a call to the server and grab the return JSON result
				final String jsonString = ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAMES_ADDRESS, nameValuePairs);

				publishProgress(1);
				games = parseServerResults(jsonString);
			}
			catch (final IOException e)
			{
				Log.e(Utilities.LOG_TAG, e.getMessage());
				toastToShow = TOAST_SERVER_RESPONSE_ERROR;
			}

			publishProgress(4);

			return games;
		}


		@Override
		protected void onPostExecute(final ArrayList<Game> games)
		{
			gamesAdapter = new GamesListAdapter(GamesListFragmentActivity.this, R.layout.new_game_fragment_listview_item, games);
			setListAdapter(gamesAdapter);
			gamesAdapter.notifyDataSetChanged();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			switch (toastToShow)
			{
				case TOAST_NO_GAMES:
					Utilities.easyToast(GamesListFragmentActivity.this, R.string.games_list_fragment_activity_getgames_no_games);
					break;

				case TOAST_SERVER_ERROR:
					Utilities.easyToast(GamesListFragmentActivity.this, R.string.games_list_activity_getgames_error);
					break;

				case TOAST_SERVER_RESPONSE_ERROR:
					Utilities.easyToast(GamesListFragmentActivity.this, R.string.games_list_activity_getgames_response_error);
					break;
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(GamesListFragmentActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMax(4);
			progressDialog.setMessage(GamesListFragmentActivity.this.getString(R.string.games_list_activity_getgames_progressdialog_message));
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle(R.string.games_list_activity_getgames_progressdialog_title);
			progressDialog.show();

			// cancel all notifications
			((NotificationManager) GamesListFragmentActivity.this.getSystemService(NOTIFICATION_SERVICE)).cancelAll();

			toastToShow = TOAST_NONE;
		}


		@Override
		protected void onProgressUpdate(final Integer... i)
		{
			progressDialog.setProgress(i[0].intValue());
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
					ArrayList<Game> turn = parseTurn(gameData, ServerUtilities.POST_DATA_TURN_YOURS, Game.TURN_YOURS);
					if (turn != null && !turn.isEmpty() && turn.size() >= 1)
					{
						games.addAll(turn);
					}

					publishProgress(2);

					turn = parseTurn(gameData, ServerUtilities.POST_DATA_TURN_THEIRS, Game.TURN_THEIRS);
					if (turn != null && !turn.isEmpty() && turn.size() >= 1)
					{
						games.addAll(turn);
					}

					publishProgress(3);
				}
				else
				{
					final String successMessage = jsonResult.optString(ServerUtilities.POST_DATA_SUCCESS);

					if (successMessage != null && !successMessage.isEmpty())
					{
						toastToShow = TOAST_NO_GAMES;
						Log.d(Utilities.LOG_TAG, "Server returned successful message: " + successMessage);
					}
					else
					{
						Utilities.easyToast(GamesListFragmentActivity.this, GamesListFragmentActivity.this.getString(R.string.games_list_fragment_activity_getgames_error));
						final String errorMessage = jsonResult.getString(ServerUtilities.POST_DATA_ERROR);

						if (errorMessage != null && !errorMessage.isEmpty())
						{
							toastToShow = TOAST_SERVER_ERROR;
							Log.e(Utilities.LOG_TAG, "Server returned error message: " + errorMessage);
						}
					}
				}
			}
			catch (final JSONException e)
			{
				toastToShow = TOAST_SERVER_ERROR;
				Log.e(Utilities.LOG_TAG, "Server returned message that was unable to be properly parsed.", e);
			}

			games.trimToSize();
			return games;
		}


		private ArrayList<Game> parseTurn(final JSONObject gameData, final String postDataTurn, final boolean whichTurn)
		{
			try
			{
				final JSONArray turn = gameData.getJSONArray(postDataTurn);
				final int turnSize = turn.length();

				if (turnSize >= 1)
				// ensure that we have at least one game in this list
				{
					ArrayList<Game> turnGames = new ArrayList<Game>();
					turnGames.add(new Game(whichTurn, Game.TYPE_SEPARATOR));

					for (int i = 0; i < turnSize; ++i)
					{
						try
						{
							// grab the current game's JSONObject
							final JSONObject jsonGame = turn.getJSONObject(i);
							final Game game = parseGame(jsonGame, whichTurn);

							if (game != null)
							{
								turnGames.add(game);
							}
						}
						catch (final JSONException e)
						{
							Log.e(Utilities.LOG_TAG, "Error parsing turn game data!");
						}
					}

					turnGames.trimToSize();
					Collections.sort(turnGames, new GamesListSorter());

					return turnGames;
				}
			}
			catch (final JSONException e)
			{
				Log.d(Utilities.LOG_TAG, "Player has no games that are his own turn.");
			}

			return null;
		}


		private Game parseGame(final JSONObject game, final boolean whichTurn)
		{
			try
			{
				final long id = game.getLong(ServerUtilities.POST_DATA_ID); // id of the user that we're challenging
				final String name = game.getString(ServerUtilities.POST_DATA_NAME); // name of the user that we're challenging
				final String gameId = game.getString(ServerUtilities.POST_DATA_GAME_ID); // id of the game that we're playing
				final long timestamp = game.getLong(ServerUtilities.POST_DATA_LAST_MOVE); // time of this game's last move

				// create a game from this data
				return new Game(timestamp, new Person(id, name), gameId, whichTurn);
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Error parsing individual game data!");
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
			final Game game = games.get(position);

			if (game != null)
			{
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ViewHolder viewHolder = new ViewHolder();

				if (game.isTypeGame())
				{
					convertView = layoutInflater.inflate(R.layout.games_list_fragment_activity_listview_item, null);

					Bitmap diskImage = ImageCache.getBitmapFromDiskCache(game.getPerson().getId(), diskCache);
					Bitmap memoryImage = memoryCache.get(game.getPerson().getId());

					viewHolder.picture = (ImageView) convertView.findViewById(R.id.games_list_fragment_activity_listview_item_picture);
					if (viewHolder.picture != null)
					{
						viewHolder.picture.setImageResource(R.drawable.fb_placeholder);
						viewHolder.picture.setTag(game.getPerson().getId());

						if (memoryImage != null)
						{
							viewHolder.picture.setImageBitmap(memoryImage);
						}
						else if (diskImage != null)
						{
							viewHolder.picture.setImageBitmap(diskImage);
						}
						else
						{
							new AsyncPopulatePictures(viewHolder).execute(game.getPerson());
						}
					}

					viewHolder.name = (TextView) convertView.findViewById(R.id.games_list_fragment_activity_listview_item_name);
					if (viewHolder.name != null)
					{
						viewHolder.name.setText(game.getPerson().getName());
						viewHolder.name.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
					}

					viewHolder.time = (TextView) convertView.findViewById(R.id.games_list_fragment_activity_listview_item_time);
					if (viewHolder.time != null)
					{
						viewHolder.time.setText(game.getTimestampFormatted());
					}

					if (game.isTurnYours())
					{
						viewHolder.onClickListener = new OnClickListener()
						{
							@Override
							public void onClick(final View v)
							{
								Intent intent = new Intent(GamesListFragmentActivity.this, CheckersGameFragment.class);
								intent.putExtra(CheckersGameFragment.INTENT_DATA_GAME_ID, game.getId());
								intent.putExtra(CheckersGameFragment.INTENT_DATA_PERSON_CHALLENGED_ID, game.getPerson().getId());
								intent.putExtra(CheckersGameFragment.INTENT_DATA_PERSON_CHALLENGED_NAME, game.getPerson().getName());

								// start the ConfirmGameActivity with a bit of extra data. We're passing it both
								// the id and the name of the facebook person that the user clicked on
								startActivityForResult(intent, 0);
							}
						};

						convertView.setOnClickListener(viewHolder.onClickListener);
					}
					else if( !game.isTurnYours() )
					{
						convertView.setOnClickListener(null);
					}
				}
				else
				{
					if (game.isTurnYours())
					{
						convertView = layoutInflater.inflate(R.layout.games_list_fragment_activity_listview_turn_yours, null);
						viewHolder.picture = (ImageView) convertView.findViewById(R.drawable.turn_yours);
						convertView.setOnClickListener(null);
					}
					else
					{
						convertView = layoutInflater.inflate(R.layout.games_list_fragment_activity_listview_turn_theirs, null);
						viewHolder.picture = (ImageView) convertView.findViewById(R.drawable.turn_theirs);
						convertView.setOnClickListener(null);
					}
				}

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

	}


	/**
	 * made this li'l class while trying to optimize our listview. apparently it
	 * helps performance
	 * https://developer.android.com/training/improving-layouts/smooth-scrolling.html
	 */
	static class ViewHolder
	{


		ImageView picture;
		OnClickListener onClickListener;
		TextView name;
		TextView time;


	}


	private class GamesListSorter implements Comparator<Game>
	{
		@Override
		public int compare(final Game one, final Game two)
		{
			return (int) (two.getTimestamp() - one.getTimestamp());
		}
	}


	public static File getCacheDir(Context context, String uniqueName) 
	{
		//Check if storage is built in or mounted from sd, try to use mounted first
		final String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}


}