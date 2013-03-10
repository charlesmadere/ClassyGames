package com.charlesmadere.android.classygames.server;


import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;


/**
 * 
 */
public class ServerApiForfeitGame extends ServerApi
{


	/**
	 * 
	 * 
	 * @param context
	 * 
	 * 
	 * @param game
	 * 
	 * 
	 * @param onCompleteListener
	 * 
	 */
	public ServerApiForfeitGame(final Context context, final Game game, final ServerApi.OnCompleteListener onCompleteListener)
	{
		super(context, game, onCompleteListener);
	}


	@Override
	protected ArrayList<NameValuePair> createNameValuePairs()
	{
		return null;
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_forfeit_game_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.server_api_forfeit_game_dialog_title;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_forfeit_game_progressdialog_message;
	}


}
