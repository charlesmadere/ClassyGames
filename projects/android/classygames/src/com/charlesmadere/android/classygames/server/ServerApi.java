package com.charlesmadere.android.classygames.server;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Build;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;


/**
 * A class that will hit an end point on the Classy Games server with some
 * given data.
 */
public abstract class ServerApi
{


	protected final static String LOG_TAG = Utilities.LOG_TAG + " - ServerApi";




	/**
	 * A reference to this class's AsyncTask (if it's currently running).
	 */
	private ServerApiTask serverApiTask;


	/**
	 * Stores whether or not the user should see a ProgressDialog popup while
	 * this ServerApi object is running.
	 */
	private boolean showProgressDialog;


	/**
	 * The Context of the class that this ServerApi class is being called from.
	 */
	private Context context;


	/**
	 * Object that allows us to run any of the methods that are defined in the
	 * Listeners interface.
	 */
	private Listeners listeners;


	/**
	 * An interface that will be used once we're done running code here.
	 */
	public interface Listeners
	{


		/**
		 * If this class's ServerApiTask AsyncTask gets cancelled then this
		 * method will be run.
		 */
		public void onCancel();


		/**
		 * Once this ServerApi class has finished its duty this method will
		 * run. If the ServerApiTask AsyncTask was cancelled, or if the user
		 * dismissed or selected "No" on the AlertDialog that this class
		 * prompts with, then this method will never be run.
		 *
		 * @param serverResponse
		 * The JSON response as received from the Classy Games.
		 */
		public void onComplete(final String serverResponse);


		/**
		 * If, on the AlertDialog that this class prompts with, the user
		 * selects either "No" or dismisses it, then this method will be run.
		 */
		public void onDismiss();


	}




	/**
	 * Creates a ServerApi object. This should be used to hit certain server
	 * end points. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param listeners
	 * A set of listener to call once we're done running code here.
	 */
	protected ServerApi(final Context context, final Listeners listeners)
	{
		this.context = context;
		this.listeners = listeners;
		showProgressDialog = true;
	}


	/**
	 * Creates a ServerApi object. This should be used to hit certain server
	 * end points. This constructor also allows you to specify whether or not
	 * the user should see a ProgressDialog popup while this ServerApi object
	 * is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 *
	 * @param showProgressDialog
	 * Set this to true if you want the user to see a ProgressDialog while this
	 * ServerApi object is running.
	 */
	protected ServerApi(final Context context, final Listeners listeners, final boolean showProgressDialog)
	{
		this.context = context;
		this.listeners = listeners;
		this.showProgressDialog = showProgressDialog;
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

		listeners.onCancel();
	}


	/**
	 * Begins the execution of this ServerApi code.
	 * 
	 * @param askUserToExecute
	 * True if you want to ask the user to confirm the execution of this
	 * ServerApi code. False if you want the code without asking the user
	 * anything; this will immediately begin the ServerApi code.
	 */
	public void execute(final boolean askUserToExecute)
	{
		if (askUserToExecute)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setMessage(getDialogMessage())
				.setNegativeButton(R.string.no, new OnClickListener()
				{
					@Override
					public void onClick(final DialogInterface dialog, final int which)
					{
						dialog.dismiss();
						listeners.onCancel();
					}
				})
				.setOnCancelListener(new OnCancelListener()
				{
					@Override
					public void onCancel(final DialogInterface dialog)
					{
						dialog.dismiss();
						listeners.onDismiss();
					}
				})
				.setPositiveButton(R.string.yes, new OnClickListener()
				{
					@Override
					public void onClick(final DialogInterface dialog, final int which)
					{
						dialog.dismiss();
						executeTask();
					}
				})
				.setTitle(getDialogTitle());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
			{
				builder.setOnDismissListener(new OnDismissListener()
				{
					@Override
					public void onDismiss(final DialogInterface dialog)
					{
						dialog.dismiss();
						listeners.onDismiss();
					}
				});
			}

			builder.show();
		}
		else
		{
			executeTask();
		}
	}


	/**
	 * Begins the execution of this ServerApi code. Will first ask the user if
	 * they do in fact want to perform this server call.
	 */
	public void execute()
	{
		execute(true);
	}


	/**
	 * Starts the execution of the ServerApiTask AsyncTask.
	 */
	private void executeTask()
	{
		serverApiTask = new ServerApiTask(context, showProgressDialog);
		serverApiTask.execute();
	}


	/**
	 * This method is run at the very beginning of the onPostExecute() method.
	 *
	 * @param serverResponse
	 * The raw data as received from the Classy Games server.
	 */
	protected void finishUp(final String serverResponse)
	{

	}




	/**
	 * An AsyncTask that will query the Classy Games server.
	 */
	private final class ServerApiTask extends AsyncTask<Void, Void, String>
	{


		private boolean showProgressDialog;
		private Context context;
		private ProgressDialog progressDialog;


		private ServerApiTask(final Context context, final boolean showProgressDialog)
		{
			this.context = context;
			this.showProgressDialog = showProgressDialog;
		}


		@Override
		protected String doInBackground(final Void... v)
		{
			String serverResponse = null;

			if (!isCancelled() && Utilities.checkForNetworkConnectivity(context))
			{
				final Person whoAmI = Utilities.getWhoAmI(context);
				serverResponse = postToServer(whoAmI);
			}

			return serverResponse;
		}


		private void cancelled()
		{
			serverApiTask = null;

			if (progressDialog != null)
			{
				progressDialog.dismiss();
			}

			listeners.onCancel();
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
			finishUp(serverResponse);

			if (progressDialog != null)
			{
				progressDialog.dismiss();
			}

			serverApiTask = null;
			listeners.onComplete(serverResponse);
		}


		@Override
		protected void onPreExecute()
		{
			if (showProgressDialog)
			{
				progressDialog = new ProgressDialog(context);
				progressDialog.setCancelable(true);
				progressDialog.setCanceledOnTouchOutside(true);
				progressDialog.setMessage(context.getString(getProgressDialogMessage()));

				progressDialog.setOnCancelListener(new OnCancelListener()
				{
					@Override
					public void onCancel(final DialogInterface dialog)
					{
						ServerApiTask.this.cancel(true);
					}
				});

				progressDialog.setTitle(getDialogTitle());
				progressDialog.show();
			}
		}


	}




	/**
	 * @return
	 * The R.string.* value for the message to show in the dialog box. This
	 * should ask the user something like "are you sure you want to blah blah?"
	 */
	protected int getDialogMessage()
	{
		return R.string.are_you_sure_that_you_want_to_send_this_data_to_the_classy_games_servers;
	}


	/**
	 * @return
	 * The R.string.* value for the title to show in the dialog box.
	 */
	protected int getDialogTitle()
	{
		return R.string.server_exchange;
	}


	/**
	 * @return
	 * The R.string.* value for the message to show in the progress dialog box.
	 */
	protected int getProgressDialogMessage()
	{
		return R.string.sending_data_to_the_classy_games_servers;
	}


	/**
	 * The heart of the AsyncTask's code.
	 *
	 * @param whoAmI
	 * A Person object that represents the user of this Android device.
	 *
	 * @return
	 * A String that contains the responding result from the server or null if
	 * a problem happened.
	 */
	protected abstract String postToServer(final Person whoAmI);


}
