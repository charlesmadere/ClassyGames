package edu.selu.android.classygames;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService
{


	@Override
	protected void onDeletedMessages(final Context context, final int total)
	{

	}


	@Override
	protected void onError(final Context context, final String arg1)
	{

	}


	@Override
	protected void onMessage(final Context context, final Intent intent)
	{
		final Bundle bundle = intent.getExtras();

		if (bundle != null)
		{
			// TODO doSomeMagic(bundle)
		}
	}


	@Override
	protected void onRegistered(final Context context, final String regId)
	{
		// TODO send the regId variable to the server
		
	}


	@Override
	protected void onUnregistered(final Context context, final String arg1)
	{

	}


}
