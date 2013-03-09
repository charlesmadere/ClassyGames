package com.charlesmadere.android.classygames.models;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class ForfeitGame
{


	/**
	 * 
	 */
	private AsyncForfeitGame asyncForfeitGame;


	/**
	 * 
	 */
	private Context context;


	/**
	 * 
	 */
	private Game game;


	/**
	 * 
	 */
	private OnForfeitGameCompleteListener onForfeitGameCompleteListener;




	/**
	 * 
	 * 
	 * @param context
	 * 
	 * 
	 * @param game
	 * 
	 * 
	 * @param board
	 * 
	 * 
	 * @param onSkipMoveCompleteListener
	 * 
	 */
	public ForfeitGame(final Context context, final Game game, final OnForfeitGameCompleteListener onForfeitGameCompleteListener)
	{
		this.context = context;
		this.onForfeitGameCompleteListener = onForfeitGameCompleteListener;
		this.game = game;
	}


	public interface OnForfeitGameCompleteListener
	{
		public void onForfeitGameComplete();
	}


	/**
	 * 
	 */
	public void begin()
	{
		asyncForfeitGame = new AsyncForfeitGame(context);
		asyncForfeitGame.execute();
	}


	/**
	 * 
	 */
	public void cancel()
	{
		if (asyncForfeitGame != null)
		{
			asyncForfeitGame.cancel(true);
		}
	}




	/**
	 * An AsyncTask that will forfeit this game.
	 */
	private final class AsyncForfeitGame extends AsyncTask<Void, Void, String>
	{


		private Context context;
		private ProgressDialog progressDialog;


		AsyncForfeitGame(final Context context)
		{
			this.context = context;
		}


		@Override
		protected String doInBackground(final Void... params)
		{
			String serverResponse = null;

			if (!isCancelled() && Utilities.verifyValidString(game.getId()))
			{
				try
				{
					final Person whoAmI = Utilities.getWhoAmI(context);

					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, whoAmI.getIdAsString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, game.getId()));

					serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_FORFEIT_GAME, nameValuePairs);
				}
				catch (final IOException e)
				{
					Log.e(Utilities.LOG_TAG, "IOException error in AsyncSkipMove - doInBackground()!", e);
				}
			}

			return serverResponse;
		}


		private void cancelled()
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			onForfeitGameCompleteListener.onForfeitGameComplete();
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
			Log.i(Utilities.LOG_TAG, "Skip move server response: " + serverResponse);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			onForfeitGameCompleteListener.onForfeitGameComplete();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(context.getString(R.string.forfeit_game_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncForfeitGame.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.progressdialog_title_generic);
			progressDialog.show();
		}


	}




}
