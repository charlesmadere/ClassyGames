package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;


/**
 * A class that will hit the SkipMove end point.
 */
public class ServerApiSkipMove extends ServerApiGame
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
	public ServerApiSkipMove(final Context context, final ServerApiListeners listeners, final Game game)
	{
		super(context, listeners, game);
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


	@Override
	protected String getServerEndPoint()
	{
		return Server.ADDRESS_SKIP_MOVE;
	}


}
