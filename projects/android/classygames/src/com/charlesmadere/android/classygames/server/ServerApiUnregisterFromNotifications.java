package com.charlesmadere.android.classygames.server;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;


/**
 * A class that will hit the Classy Games RemoveRegId end point.
 */
public class ServerApiUnregisterFromNotifications extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the RemoveRegId
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param onCompleteListener
	 * A listener to call once we're done running code here.
	 */
	public ServerApiUnregisterFromNotifications(final Context context, final ServerApi.ServerApiListeners onCompleteListener)
	{
		super(context, onCompleteListener);
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		String serverResponse = null;

		try
		{
			final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, whoAmI.getIdAsString()));

			serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_REMOVE_REG_ID, nameValuePairs);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException error in AsyncForfeitGame - doInBackground()!", e);
		}

		return serverResponse;
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


}
