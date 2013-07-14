package com.charlesmadere.android.classygames.server;


import android.content.Context;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A class that will hit the Classy Games SendMove end point.
 */
public final class ServerApiSendMove extends ServerApiGame
{


	/**
	 * The game board to be sent to the server.
	 */
	private GenericBoard board;




	/**
	 * Creates a ServerApi object. This should be used to hit the SendMove
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param listeners
	 * A listener to call once we're done running code here.
	 * 
	 * @param game
	 * The Game object that this API call has to deal with.
	 * 
	 * @param board
	 * The GenericBoard object that is being sent to the server.
	 */
	public ServerApiSendMove(final Context context, final Listeners listeners, final Game game,
		final GenericBoard board)
	{
		super(context, listeners, game);
		this.board = board;
	}


	@Override
	protected int getDialogMessage()
	{
		return R.string.server_api_send_move_dialog_message;
	}


	@Override
	protected int getDialogTitle()
	{
		return R.string.send_move;
	}


	@Override
	protected int getProgressDialogMessage()
	{
		return R.string.server_api_send_move_progressdialog_message;
	}


	@Override
	protected String postToServer(final ApiData data, final Game game) throws IOException, JSONException
	{
		final String serverResponse;
		final JSONObject boardJSON = board.makeJSON();
		final String boardJSONString = boardJSON.toString();
		data.addKeyValuePair(Server.POST_DATA_BOARD, boardJSONString);

		if (Utilities.verifyValidString(game.getId()))
		{
			serverResponse = Server.postToServerNewMove(data);
		}
		else
		{
			serverResponse = Server.postToServerNewGame(data);
		}

		return serverResponse;
	}


}
