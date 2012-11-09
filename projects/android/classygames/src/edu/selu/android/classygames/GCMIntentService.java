package edu.selu.android.classygames;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;


/**
 * <p>For more information on this class please read
 * <a href="https://developer.android.com/guide/google/gcm/client-javadoc/index.html">https://developer.android.com/guide/google/gcm/client-javadoc/index.html</a></p>
 */
public class GCMIntentService extends GCMBaseIntentService
{


	public GCMIntentService()
	{
		super(SecretConstants.GOOGLE_PROJECT_ID);
	}


	/**
	 * <p>Called when the GCM server tells pending messages have been deleted because the device was idle.</p>
	 */
	@Override
	protected void onDeletedMessages(final Context context, final int total)
	{

	}


	/**
	 * <p>Called on registration or unregistration error.</p>
	 */
	@Override
	protected void onError(final Context context, final String errorId)
	{

	}


	/**
	 * <p>Called when a cloud message has been received.</p>
	 */
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


	/**
	 * <p>Called after a device has been registered.</p>
	 */
	@Override
	protected void onRegistered(final Context context, final String reg_id)
	{
		Log.i(Utilities.LOG_TAG, "Device registered with reg_id of \"" + reg_id + "\".");
		ServerUtilities.contextBroadcast(context, context.getString(R.string.server_registration_success));
		ServerUtilities.GCMRegister(context, GamesListActivity.person, reg_id);
	}


	/**
	 * <p>Called after a device has been unregistered.</p>
	 */
	@Override
	protected void onUnregistered(final Context context, final String reg_id)
	{

	}


}
