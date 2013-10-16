package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;

import java.io.IOException;


/**
 * A class that will hit the Classy Games RemoveRegId end point.
 */
public final class ServerApiUnregister extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the RemoveRegId
	 * end point. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	public ServerApiUnregister(final Context context, final Listeners listeners)
	{
		super(context, listeners);
	}


	/**
	 * Creates a ServerApi object. This should be used to hit the RemoveRegId
	 * end point. This constructor also allows you to specify whether or not
	 * the user should see a ProgressDialog popup while this ServerApi object
	 * is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param showProgressDialog
	 * Set this to true if you want the user to see a ProgressDialog while this
	 * ServerApi object is running.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	public ServerApiUnregister(final Context context, final boolean showProgressDialog, final Listeners listeners)
	{
		super(context, showProgressDialog, listeners);
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_unregister_from_server_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.unregister_from_notifications;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_unregister_from_server_progressdialog_message;
	}


	@Override
	protected String postToServer(final Person whoAmI)
	{
		String serverResponse = null;

		try
		{
			final ApiData data = new ApiData()
				.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId());

			serverResponse = Server.postToServerRemoveRegId(data);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException error in ServerApiGCMUnregister - postToServer()!", e);
		}

		return serverResponse;
	}


}
