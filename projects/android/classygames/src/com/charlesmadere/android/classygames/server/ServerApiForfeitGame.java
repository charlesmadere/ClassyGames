package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A class that will hit the Classy Games ForfeitGame end point.
 */
public final class ServerApiForfeitGame extends ServerApiGame
{


	/**
	 * Creates a ServerApi object. This should be used to hit the ForfeitGame
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
	public ServerApiForfeitGame(final Context context, final Listeners listeners, final Game game)
	{
		super(context, listeners, game);
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_forfeit_game_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.forfeit_game;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_forfeit_game_progressdialog_message;
	}


	@Override
	protected String postToServer(final ApiData data, final Game game)
		throws IOException, JSONException
	{
		return Server.postToServerForfeitGame(data);
	}


}
