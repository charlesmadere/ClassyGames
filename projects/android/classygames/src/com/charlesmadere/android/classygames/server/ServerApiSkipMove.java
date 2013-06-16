package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A class that will hit the SkipMove end point.
 */
public class ServerApiSkipMove extends ServerApi
{


	/**
	 * The Game object that this API call has to deal with.
	 */
	private Game game;




	/**
	 * Creates a ServerApi object. This should be used to hit the SkipMove
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
	 */
	public ServerApiSkipMove(final Context context, final ServerApi.ServerApiListeners onCompleteListener, final Game game)
	{
		super(context, onCompleteListener);

		this.game = game;
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		String serverResponse = null;

		if (Utilities.verifyValidString(game.getId()))
		{
			try
			{
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, whoAmI.getIdAsString()));
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, game.getPerson().getName()));
				nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, game.getId()));

				serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_SKIP_MOVE, nameValuePairs);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException error in AsyncSkipMove - doInBackground()!", e);
			}
		}

		return serverResponse;
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
