package com.charlesmadere.android.classygames.server;


import android.content.Context;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;


public class ServerApiRegisterForNotifications extends ServerApi
{


	public ServerApiRegisterForNotifications(final Context context, final Game game, final ServerApi.OnCompleteListener onCompleteListener)
	{
		super(context, game, onCompleteListener);
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		return null;
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_register_for_notifications_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.server_api_register_for_notifications_dialog_title;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_register_for_notifications_proressdialog_message;
	}


}
