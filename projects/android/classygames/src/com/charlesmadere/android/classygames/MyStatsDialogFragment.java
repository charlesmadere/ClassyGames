package com.charlesmadere.android.classygames;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.server.ServerApi;
import com.charlesmadere.android.classygames.server.ServerApiGetStats;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This Fragment shows the user their stats and scores and stuff.
 */
public final class MyStatsDialogFragment extends SherlockDialogFragment
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - MyStatsDialogFragment";
	private final static String PREFERENCES_NAME = "MyProfileFragment_Preferences";
	private final static String KEY_CHECKERS_LOSES = "KEY_CHECKERS_LOSES";
	private final static String KEY_CHECKERS_WINS = "KEY_CHECKERS_WINS";
	private final static String KEY_CHESS_LOSES = "KEY_CHESS_LOSES";
	private final static String KEY_CHESS_WINS = "KEY_CHESS_WINS";


	private ServerApi serverTask;
	private SharedPreferences sPreferences;
	private Editor sPreferencesEditor;
	private LinearLayout stats;
	private TextView checkersLoses;
	private TextView checkersWins;
	private TextView chessLoses;
	private TextView chessWins;
	private TextView gamesPlayed;
	private LinearLayout loading;
	private View view;


	private Listeners listeners;


	public interface Listeners
	{
		/**
		 * This method is fired whenever there was an error when trying to
		 * parse data as received from the server when performing the GetStats
		 * server api call.
		 *
		 * @param e
		 * The Exception that was raised which caused this error.
		 */
		public void onGetStatsDataError(final Exception e);
	}




	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		return inflater.inflate(R.layout.my_stats_dialog_fragment, container, false);
	}


	@Override
	@SuppressWarnings("deprecation")
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		findViews();

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			final BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_subtle_gray);
			background.setAntiAlias(true);
			background.setDither(true);
			background.setFilterBitmap(true);
			background.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			view.setBackgroundDrawable(background);
		}

		getPreferences();

		final int checkersLoses = sPreferences.getInt(KEY_CHECKERS_LOSES, -1);
		final int checkersWins = sPreferences.getInt(KEY_CHECKERS_WINS, -1);
		final int chessLoses = sPreferences.getInt(KEY_CHESS_LOSES, -1);
		final int chessWins = sPreferences.getInt(KEY_CHESS_WINS, -1);

		if (checkersLoses >= 0 && checkersWins >= 0 && chessLoses >= 0 && chessWins >= 0)
		{
			flipViews();
			flushViews(checkersLoses, checkersWins, chessLoses, chessWins);
		}
		else
		{
			getPreferencesEditor().clear().commit();

			serverTask = new ServerApiGetStats(getSherlockActivity(), new ServerApi.Listeners()
			{
				@Override
				public void onCancel()
				{
					serverTask = null;
				}


				@Override
				public void onComplete(final String serverResponse)
				{
					parseServerResponse(serverResponse);
					serverTask = null;
				}


				@Override
				public void onDismiss()
				{
					serverTask = null;
				}
			});

			serverTask.execute(false);
		}
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);
		listeners = (Listeners) activity;
	}


	@Override
	public void onDestroyView()
	{
		cancelRunningServerTask();
		super.onDestroyView();
	}


	/**
	 * Cancels the currently running AsyncTask (if it's currently running).
	 */
	private void cancelRunningServerTask()
	{
		if (isServerTaskRunning())
		{
			serverTask.cancel();
		}
	}


	private void findViews()
	{
		if (view == null || checkersLoses == null || checkersWins == null || chessLoses == null
			|| chessWins == null || gamesPlayed == null || loading == null)
		{
			view = getView();
			stats = (LinearLayout) view.findViewById(R.id.my_stats_dialog_fragment_linearlayout_stats);
			checkersLoses = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_checkers_loses);
			checkersWins = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_checkers_wins);
			chessLoses = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_chess_loses);
			chessWins = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_chess_wins);
			gamesPlayed = (TextView) view.findViewById(R.id.my_stats_dialog_fragment_textview_games_played);
			loading = (LinearLayout) view.findViewById(R.id.my_stats_dialog_fragment_textview_loading);
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


	private void flushViews(final int[] checkers, final int[] chess)
	{
		flushViews(checkers[0], checkers[1], chess[0], chess[1]);
	}


	private void flushViews(final int checkersLoses, final int checkersWins, final int chessLoses,
		final int chessWins)
	{
		final Resources resources = getResources();
		this.checkersLoses.setText(resources.getQuantityString(R.plurals.x_loses, checkersLoses, checkersLoses));
		this.checkersWins.setText(resources.getQuantityString(R.plurals.x_wins, checkersWins, checkersWins));
		this.chessLoses.setText(resources.getQuantityString(R.plurals.x_loses, chessLoses, chessLoses));
		this.chessWins.setText(resources.getQuantityString(R.plurals.x_wins, chessWins, chessWins));

		final int gamesPlayed = checkersLoses + checkersWins + chessLoses + chessWins;
		this.gamesPlayed.setText(resources.getQuantityString(R.plurals.x_games_played, gamesPlayed, gamesPlayed));
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


	private boolean isServerTaskRunning()
	{
		return serverTask != null;
	}


	public boolean onBackPressed()
	{
		if (isServerTaskRunning())
		{
			cancelRunningServerTask();
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * Reads in the server response and gets the user's win and lose data out
	 * of it.
	 *
	 * @param serverResponse
	 * The raw String data as received from the Classy Games server.
	 */
	private void parseServerResponse(final String serverResponse)
	{
		if (Utilities.validString(serverResponse))
		{
			try
			{
				final JSONObject jsonResult = new JSONObject(serverResponse);
				final JSONObject jsonStatsData = jsonResult.optJSONObject(Server.POST_DATA_SUCCESS);

				if (jsonStatsData == null)
				{
					final String successMessage = jsonResult.optString(Server.POST_DATA_SUCCESS);

					if (Utilities.validString(successMessage))
					{
						Log.d(LOG_TAG, "Server returned success message: " + successMessage);
					}
					else
					{
						final String errorMessage = jsonResult.getString(Server.POST_DATA_ERROR);
						Log.e(LOG_TAG, "Server returned error message: " + errorMessage);
					}
				}
				else
				{
					final int [] checkers = parseLosesAndWins(jsonStatsData, Server.POST_DATA_CHECKERS);
					final int [] chess = parseLosesAndWins(jsonStatsData, Server.POST_DATA_CHESS);
					flushViews(checkers, chess);
				}
			}
			catch (final JSONException e)
			{
				serverTask = null;
				listeners.onGetStatsDataError(e);
			}
		}
		else
		{
			serverTask = null;
			listeners.onGetStatsDataError(new JSONException("JSON String was massively malformed!"));
		}
	}


	private int[] parseLosesAndWins(final JSONObject statsData, final String game)
		throws JSONException
	{
		final JSONObject gameStats = statsData.getJSONObject(game);
		final int loses = gameStats.getInt(Server.POST_DATA_LOSES);
		final int wins = gameStats.getInt(Server.POST_DATA_WINS);
		return new int [] { loses, wins };
	}




	/**
	 * Clears all saved stats data that was retrieved from the Classy Games
	 * server.
	 *
	 * @param context
	 * The Context of the Activity or Fragment that you're calling this method
	 * from.
	 */
	public static void clearCachedStats(final Context context)
	{
		context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().commit();
	}


}
