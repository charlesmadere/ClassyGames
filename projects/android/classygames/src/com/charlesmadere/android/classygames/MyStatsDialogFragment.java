package com.charlesmadere.android.classygames;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.charlesmadere.android.classygames.utilities.Utilities;


/**
 * This Fragment shows the user their stats and scores and stuff.
 */
public class MyStatsDialogFragment extends SherlockDialogFragment
{


	public final static String PREFERENCES_NAME = "MyProfileFragment_Preferences";
	private final static String LOG_TAG = Utilities.LOG_TAG + " - MyStatsDialogFragment";
	private final static String KEY_CHECKERS_LOSES = "KEY_CHECKERS_LOSES";
	private final static String KEY_CHECKERS_WINS = "KEY_CHECKERS_WINS";
	private final static String KEY_CHESS_LOSES = "KEY_CHESS_LOSES";
	private final static String KEY_CHESS_WINS = "KEY_CHESS_WINS";


	private AsyncGetStats asyncGetStats;
	private SharedPreferences sPreferences;
	private Editor sPreferencesEditor;
	private LinearLayout stats;
	private TextView checkersLosses;
	private TextView checkersWins;
	private TextView chessLosses;
	private TextView chessWins;
	private TextView gamesPlayed;
	private TextView loading;
	private View view;




	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		return inflater.inflate(R.layout.my_stats_dialog_fragment, container, false);
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		findViews();
		getPreferences();

		final int checkersLosses = sPreferences.getInt(KEY_CHECKERS_LOSES, -1);
		final int checkersWins = sPreferences.getInt(KEY_CHECKERS_WINS, -1);
		final int chessLosses = sPreferences.getInt(KEY_CHESS_LOSES, -1);
		final int chessWins = sPreferences.getInt(KEY_CHESS_WINS, -1);

		if (checkersLosses >= 0 && checkersWins >= 0 && chessLosses >= 0 && chessWins >= 0)
		{
			flipViews();
			flushViews(checkersLosses, checkersWins, chessLosses, chessWins);
		}
		else
		{
			asyncGetStats = new AsyncGetStats(getPreferencesEditor());
			asyncGetStats.execute();
		}
	}


	/**
	 * Cancels the currently running AsyncTask (if any).
	 */
	public void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			asyncGetStats.cancel(true);
			asyncGetStats = null;
		}
	}


	private void findViews()
	{
		if (view == null || checkersLosses == null || checkersWins == null || chessLosses == null
			|| chessWins == null || gamesPlayed == null || loading == null)
		{
			view = getView();
			stats = (LinearLayout) view.findViewById(R.id.my_stats_dialog_fragment_linearlayout_stats);
			checkersLosses = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_checkers_losses);
			checkersWins = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_checkers_wins);
			chessLosses = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_chess_losses);
			chessWins = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_chess_wins);
			gamesPlayed = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_games_played);
			loading = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_loading);
		}
	}


	private void flipViews()
	{
		findViews();

		if (stats.getVisibility() == View.GONE || gamesPlayed.getVisibility() == View.GONE
			|| loading.getVisibility() == View.VISIBLE)
		{
			stats.setVisibility(View.VISIBLE);
			gamesPlayed.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
		}
		else
		{
			stats.setVisibility(View.GONE);
			gamesPlayed.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
		}
	}


	private void flushViews(final int checkersLosses, final int checkersWins, final int chessLosses,
		final int chessWins)
	{
		if (checkersLosses == 1)
		{
			this.checkersLosses.setText(getString(R.string.one_loss));
		}
		else
		{
			this.checkersLosses.setText(getString(R.string.x_losses, checkersLosses));
		}

		if (checkersWins == 1)
		{
			this.checkersWins.setText(getString(R.string.one_win));
		}
		else
		{
			this.checkersWins.setText(getString(R.string.x_wins, checkersWins));
		}

		if (chessLosses == 1)
		{
			this.chessLosses.setText(getString(R.string.one_loss));
		}
		else
		{
			this.chessLosses.setText(getString(R.string.x_losses, chessLosses));
		}

		if (chessWins == 1)
		{
			this.chessWins.setText(getString(R.string.one_win));
		}
		else
		{
			this.chessWins.setText(getString(R.string.x_wins, chessWins));
		}

		final int gamesPlayed = checkersLosses + checkersWins + chessLosses + chessWins;

		if (gamesPlayed == 1)
		{
			this.gamesPlayed.setText(getString(R.string.one_game_played));
		}
		else
		{
			this.gamesPlayed.setText(getString(R.string.x_games_played, gamesPlayed));
		}
	}


	private SharedPreferences getPreferences()
	{
		if (sPreferences == null)
		{
			sPreferences = getSherlockActivity().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		}

		return sPreferences;
	}


	private Editor getPreferencesEditor()
	{
		if (sPreferencesEditor == null)
		{
			sPreferencesEditor = getPreferences().edit();
		}

		return sPreferencesEditor;
	}


	/**
	 * @return
	 * Returns true if the AsyncGetStats AsyncTask is running.
	 */
	public boolean isAnAsyncTaskRunning()
	{
		return asyncGetStats != null;
	}




	private final class AsyncGetStats extends AsyncTask<Void, Void, String>
	{


		private Editor sPreferencesEditor;


		private AsyncGetStats(final Editor sPreferencesEditor)
		{
			this.sPreferencesEditor = sPreferencesEditor;
		}


		@Override
		protected String doInBackground(final Void... params)
		{
			return null;
		}


		private void cancelled()
		{

		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final String serverResponse)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			flipViews();
			flushViews(10, 1, 0, 2);

			asyncGetStats = null;
		}


		@Override
		protected void onPreExecute()
		{
			sPreferencesEditor.clear();
			sPreferencesEditor.commit();
		}


	}




}
