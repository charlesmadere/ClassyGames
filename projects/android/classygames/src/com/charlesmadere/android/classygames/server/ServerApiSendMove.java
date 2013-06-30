package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
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
public class ServerApiSendMove extends ServerApi
{


	/**
	 * The Game object that this API call has to deal with.
	 */
	private Game game;


	/**
	 * The game board to send to the server.
	 */
	private GenericBoard board;




	/**
	 * Creates a ServerApi object. This should be used to hit the SendMove
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param onCompleteListener
	 * A listener to call once we're done running code here.
	 * 
	 * @param game
	 * The Game object that this API call has to deal with.
	 * 
	 * @param board
	 * The GenericBoard object that is being sent to the server.
	 */
	public ServerApiSendMove(final Context context, final ServerApi.ServerApiListeners onCompleteListener, final Game game, final GenericBoard board)
	{
		super(context, onCompleteListener);

		this.game = game;
		this.board = board;
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		String serverResponse = null;

		try
		{
			final JSONObject boardJSON = board.makeJSON();
			final String boardJSONString = boardJSON.toString();

			final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, whoAmI.getIdAsString()));
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_BOARD, boardJSONString));
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));

			if (Utilities.verifyValidString(game.getId()))
			{
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, game.getId()));

				serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_NEW_MOVE, nameValuePairs);
			}
			else
			{
				serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_NEW_GAME, nameValuePairs);
			}
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "JSONException error in AsyncSendMove - doInBackground()!", e);
		}
		catch (final JSONException e)
		{
			Log.e(LOG_TAG, "IOException error in AsyncSendMove - doInBackground()!", e);
		}

		return serverResponse;
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


}
