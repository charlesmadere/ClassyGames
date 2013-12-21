package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import org.json.JSONException;

import java.io.IOException;


/**
 * A class that will hit the SkipMove end point.
 */
public final class ServerApiSkipMove extends ServerApiGame
{


	/**
	 * Creates a ServerApi object. This should be used to hit the SkipMove
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param listeners
	 * A listener to call once we're done running code here.
	 * 
	 * @param game
	 * The game data to send to the server.
	 */
	public ServerApiSkipMove(final Context context, final Listeners listeners, final Game game)
	{
		super(context, listeners, game);
	}


	@Override
	protected String postToServer(final ApiData data, final Game game) throws IOException, JSONException
	{
		return Server.postToServerSkipMove(data);
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_skip_move_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.skip_move;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_skip_move_progressdialog_message;
	}


}
