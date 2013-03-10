package com.charlesmadere.android.classygames.server;


import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;


/**
 * 
 */
public abstract class ServerApi
{


	/**
	 * 
	 */
	private ServerApiTask serverApiTask;


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
	private OnCompleteListener onCompleteListener;


	/**
	 * 
	 */
	public interface OnCompleteListener
	{
		/**
		 * 
		 * 
		 * @param wasCompleted
		 * 
		 */
		public void onComplete(final boolean wasCompleted);
	}




	/**
	 * 
	 * 
	 * @param context
	 * 
	 * 
	 * @param game
	 * 
	 * 
	 * @param onCompleteListener
	 * 
	 */
	public ServerApi(final Context context, final Game game, final OnCompleteListener onCompleteListener)
	{
		this.context = context;
		this.game = game;
		this.onCompleteListener = onCompleteListener;
	}


	/**
	 * Cancels the currently running AsyncTask (if it is currently running).
	 */
	public void cancel()
	{
		if (serverApiTask != null)
		{
			serverApiTask.cancel(true);
		}
	}


	/**
	 * Begins the execution of this ServerApi code. 
	 */
	public void execute()
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(context)
			.setMessage(getDialogMessage())
			.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(final DialogInterface dialog, final int which)
				{
					dialog.dismiss();
					onCompleteListener.onComplete(false);
				}
			})
			.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(final DialogInterface dialog, final int which)
				{
					dialog.dismiss();

					serverApiTask = new ServerApiTask(context);
					serverApiTask.execute();
				}
			})
			.setTitle(getDialogTitle());
	
		builder.show();
	}




	/**
	 * 
	 */
	private final class ServerApiTask extends AsyncTask<Void, Void, String>
	{


		private Context context;
		private ProgressDialog progressDialog;


		private ServerApiTask(final Context context)
		{
			this.context = context;
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

		}


		@Override
		protected void onPreExecute()
		{

		}


	}




	/**
	 * 
	 * 
	 * @return
	 * 
	 */
	protected abstract ArrayList<NameValuePair> createNameValuePairs();


	/**
	 * @return
	 * 
	 */
	protected abstract int getDialogMessage();


	/**
	 * @return
	 * 
	 */
	protected abstract int getDialogTitle();


	/**
	 * @return
	 * 
	 */
	protected abstract int getProgressDialogMessage();


}
