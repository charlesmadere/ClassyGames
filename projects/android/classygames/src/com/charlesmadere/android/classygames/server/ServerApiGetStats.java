package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Person;

import java.io.IOException;


/**
 * A class that will hit the Classy Games GetStats end point.
 */
public final class ServerApiGetStats extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the GetStats
	 * server end point.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listener to call once we're done running code here.
	 */
	public ServerApiGetStats(final Context context, final ServerApi.Listeners listeners)
	{
		super(context, listeners, false);
	}


	@Override
	protected String postToServer(final Person whoAmI)
	{
		String serverResponse = null;

		try
		{
			final ApiData data = new ApiData()
				.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId());

			serverResponse = Server.postToServerGetStats(data);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException error in ServerApiGetStats - postToServer()!", e);
		}

		return serverResponse;
	}


}
