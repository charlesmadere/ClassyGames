package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;

import java.io.IOException;


/**
 * A class that will hit the Classy Games RemoveRegId end point.
 */
public final class ServerApiUnregisterFromNotifications extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the RemoveRegId
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param listeners
	 * A listener to call once we're done running code here.
	 */
	public ServerApiUnregisterFromNotifications(final Context context, final Listeners listeners)
	{
		super(context, listeners);
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
			final ApiData data = new ApiData();
			data.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId());
			serverResponse = Server.postToServerRemoveRegId(data);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException error in ServerApiUnregisterFromNotifications - postToServer()!", e);
		}

		return serverResponse;
	}


}
