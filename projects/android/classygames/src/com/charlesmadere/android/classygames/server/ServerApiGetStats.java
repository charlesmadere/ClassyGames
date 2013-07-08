package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.models.Person;


public class ServerApiGetStats extends ServerApi
{


	protected ServerApiGetStats(final Context context, final ServerApiListeners listeners)
	{
		super(context, listeners, false);
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		return null;
	}


	@Override
	protected int getDialogMessage()
	{
		return 0;
	}


	@Override
	protected int getDialogTitle()
	{
		return 0;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return 0;
	}


}
