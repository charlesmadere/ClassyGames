package edu.selu.android.classygames;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService
{


	public GCMIntentService()
	{
		super(SecretConstants.GOOGLE_PROJECT_ID);
	}


	@Override
	protected void onDeletedMessages(final Context context, final int total)
	{

	}


	@Override
	protected void onError(final Context context, final String errorId)
	{

	}


	@Override
	protected void onMessage(final Context context, final Intent intent)
	{
		final Bundle bundle = intent.getExtras();

		if (bundle == null)
		{
			Log.e(Utilities.LOG_TAG, "GCM message was received but it was malformed.");
		}
		else
		{
			Log.i(Utilities.LOG_TAG, "GCM message was received.");
		}
	}


	@Override
	protected void onRegistered(final Context context, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Device registered with reg_id of \"" + reg_id + "\".");
		ServerUtilities.contextBroadcast(context, context.getString(R.string.server_registration_success));
		ServerUtilities.GCMRegister(context, GamesListActivity.person, reg_id);
	}


	@Override
	protected void onUnregistered(final Context context, final String reg_id)
	{

	}


}
