package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.gcm.GCMManager;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;

import java.io.IOException;


public final class ServerApiRegister extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listeners to call once we're done running code here.
	 */
	public ServerApiRegister(final Context context, final Listeners listeners)
	{
		super(context, listeners);
	}


	/**
	 * Creates a ServerApi object. This should be used to hit the NewRegId end
	 * point. This constructor also allows you to specify whether or not the
	 * user should see a ProgressDialog popup while this ServerApi object is
	 * running.
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
	public ServerApiRegister(final Context context, final boolean showProgressDialog, final Listeners listeners)
	{
		super(context, showProgressDialog, listeners);
	}


	@Override
	protected String postToServer(final Person whoAmI) throws IOException
	{
		final String registrationId = GCMManager.getRegistrationId(getContext());

		final ApiData data = new ApiData()
			.addKeyValuePair(Server.POST_DATA_ID, whoAmI.getId())
			.addKeyValuePair(Server.POST_DATA_NAME, whoAmI.getName());

		if (Utilities.validString(registrationId))
		// This if statement will validate as true if there was no issue when
		// attempting to grab this user's GCM registration ID.
		{
			data.addKeyValuePair(Server.POST_DATA_REG_ID, registrationId);
		}

		Server.postToServerNewRegId(data);

		return registrationId;
	}


}
