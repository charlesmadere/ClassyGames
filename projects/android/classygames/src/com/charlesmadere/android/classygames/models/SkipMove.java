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


public class SkipMove
{


	/**
	 * 
	 */
	private AsyncSkipMove asyncSkipMove;


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
	private OnSkipMoveCompleteListener onSkipMoveCompleteListener;




	/**
	 * 
	 * 
	 * @param context
	 * 
	 * 
	 * @param game
	 * 
	 * 
	 * @param onSkipMoveCompleteListener
	 * 
	 */
	public SkipMove(final Context context, final Game game, final OnSkipMoveCompleteListener onSkipMoveCompleteListener)
	{
		this.context = context;
		this.game = game;
		this.onSkipMoveCompleteListener = onSkipMoveCompleteListener;
	}


	public interface OnSkipMoveCompleteListener
	{
		public void onSkipMoveComplete();
	}


	/**
	 * 
	 */
	public void begin()
	{
		asyncSkipMove = new AsyncSkipMove(context);
		asyncSkipMove.execute();
	}


	/**
	 * 
	 */
	public void cancel()
	{
		if (asyncSkipMove != null)
		{
			asyncSkipMove.cancel(true);
		}
	}




	/**
	 * An AsyncTask that will skip the user's turn.
	 */
	private final class AsyncSkipMove extends AsyncTask<Void, Void, String>
	{


		private Context context;
		private ProgressDialog progressDialog;


		AsyncSkipMove(final Context context)
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

					serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_SKIP_MOVE, nameValuePairs);
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

			onSkipMoveCompleteListener.onSkipMoveComplete();
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

			onSkipMoveCompleteListener.onSkipMoveComplete();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(true);
			progressDialog.setMessage(context.getString(R.string.skip_move_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncSkipMove.this.cancel(true);
				}
			});

			progressDialog.setTitle(R.string.progressdialog_title_generic);
			progressDialog.show();
		}


	}




}
