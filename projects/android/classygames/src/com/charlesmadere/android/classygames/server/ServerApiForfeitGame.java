package com.charlesmadere.android.classygames.server;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


/**
 * A class that will hit the Classy Games ForfeitGame end point.
 */
public class ServerApiForfeitGame extends ServerApi
{


	/**
	 * Creates a ServerApi object. This should be used to hit the ForfeitGame
	 * server end point.
	 * 
	 * @param context
	 * The Context of the class that you're creating this object from.
	 * 
	 * @param game
	 * The Game object that this API call has to deal with.
	 * 
	 * @param onCompleteListener
	 * A listener to call once we're done running code here.
	 */
	public ServerApiForfeitGame(final Context context, final Game game, final ServerApi.ServerApiListeners onCompleteListener)
	{
		super(context, game, onCompleteListener);
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

				serverResponse = ServerUtilities.postToServer(ServerUtilities.ADDRESS_FORFEIT_GAME, nameValuePairs);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException error in AsyncForfeitGame - doInBackground()!", e);
			}
		}

		return serverResponse;
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


}
