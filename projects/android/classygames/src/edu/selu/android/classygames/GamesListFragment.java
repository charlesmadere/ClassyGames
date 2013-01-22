package edu.selu.android.classygames;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class GamesListFragment extends SherlockListFragment
{


	/**
	 * One of this class's callback methods. This is fired whenever one of the
	 * games in the user's list of games is clicked on.
	 */
	private GamesListFragmentOnGameSelectedListener gamesListFragmentOnGameSelectedListener;

	public interface GamesListFragmentOnGameSelectedListener
	{
		public void gameListFragmentOnGameSelected(final Game game);
	}


	/**
	 * One of this class's callback methods. This is fired whenever the new
	 * game button in the action bar is clicked.
	 */
	private GamesListFragmentOnNewGameSelectedListener gamesListFragmentOnNewGameSelectedListener;

	public interface GamesListFragmentOnNewGameSelectedListener
	{
		public void gamesListFragmentOnNewGameSelected();
	}




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.games_list_fragment, container, false);
	}


	@Override
	public void onAttach(final Activity activity)
	// This makes sure that the Activity containing this Fragment has
	// implemented the callback interface. If the callback interface has not
	// been implemented, an exception is thrown.
	{
		super.onAttach(activity);

		try
		{
			gamesListFragmentOnGameSelectedListener = (GamesListFragmentOnGameSelectedListener) activity;
			gamesListFragmentOnNewGameSelectedListener = (GamesListFragmentOnNewGameSelectedListener) activity;
		}
		catch (final ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement listeners!");
		}
	}


	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		inflater.inflate(R.menu.games_list_fragment, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.games_list_fragment_actionbar_about:
				startActivity(new Intent(getSherlockActivity(), AboutActivity.class));
				break;

			case R.id.games_list_fragment_actionbar_new_game:
				// notify the parent Activity that the new game button in the
				// action bar has been clicked
				gamesListFragmentOnNewGameSelectedListener.gamesListFragmentOnNewGameSelected();
				break;

			case R.id.games_list_fragment_actionbar_refresh:
				Utilities.easyToast(getSherlockActivity(), "refresh!");
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	public void onResume()
	{
		super.onResume();

		final ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setTitle(R.string.games_list_fragment_title);
	}


	@Override
	public void onStart()
	{
		super.onStart();

		if (getFragmentManager().findFragmentById(R.id.central_fragment_activity_fragment_container) == null)
		// When in two-pane layout, set the ListView to highlight the selected
		// list item. This is done during onStart because at this point the
		// ListView is definitely available. Consult the Android Activity
		// Lifecycle to find out a bit more:
		// https://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle
		{
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}




	private final class AsyncPopulateGamesList extends AsyncTask<Void, Integer, ArrayList<Game>>
	{


		private ProgressDialog progressDialog;


		@Override
		protected ArrayList<Game> doInBackground(final Void... params)
		{
			final ArrayList<Game> games = new ArrayList<Game>();
			final Person whoAmI = Utilities.getWhoAmI(getSherlockActivity());

			// create the data that will be 
			final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, whoAmI.getIdAsString()));

			try
			{
				// Make a call to the Classy Games server API and store it's
				// JSON response. Note that we're also sending it the
				// nameValuePairs variable that we just created. The server
				// requires we send it some information in order for us to get
				// a meaningful response back.
				final String json = ServerUtilities.postToServer(ServerUtilities.SERVER_GET_GAMES_ADDRESS, nameValuePairs);

				publishProgress(1);

				// TODO
			}
			catch (final IOException e)
			{
				Log.e(Utilities.LOG_TAG, e.getLocalizedMessage());
			}

			publishProgress(4);

			return games;
		}


		@Override
		protected void onCancelled(final ArrayList<Game> games)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPostExecute(final ArrayList<Game> games)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(getSherlockActivity(), ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMax(4);
			progressDialog.setMessage(getSherlockActivity().getString(R.string.games_list_fragment_getgames_progressdialog_message));
			progressDialog.setTitle(R.string.games_list_fragment_getgames_progressdialog_title);
			progressDialog.show();
		}


		@Override
		protected void onProgressUpdate(final Integer... values)
		{

		}


	}




}
