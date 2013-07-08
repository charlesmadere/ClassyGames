package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A class that will hit game related Classy Games end points.
 */
public abstract class ServerApiGame extends ServerApi
{


	/**
	 * The Game object that this API call has to deal with. Information about
	 * the given game will be sent to the server, such as the game's ID as well
	 * as who the current user is playing against.
	 */
	private Game game;




	/**
	 * Creates a ServerApi object. This should be used to hit game related
	 * server end points. If this constructor is used, then the user will see a
	 * ProgressDialog popup while this ServerApi object is running.
	 *
	 * @param context
	 * The Context of the class that you're creating this object from.
	 *
	 * @param listeners
	 * A set of listener to call once we're done running code here.
	 *
	 * @param game
	 * The game data to send to the server.
	 */
	protected ServerApiGame(final Context context, final ServerApiListeners listeners, final Game game)
	{
		super(context, listeners);
		this.game = game;
	}


	@Override
	protected String doInBackground(final Person whoAmI)
	{
		String serverResponse = null;

		if (Utilities.verifyValidString(game.getId()))
		{
			final String serverEndPoint = getServerEndPoint();

			try
			{
				final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair(Server.POST_DATA_USER_CREATOR, whoAmI.getIdAsString()));
				nameValuePairs.add(new BasicNameValuePair(Server.POST_DATA_USER_CHALLENGED, game.getPerson().getIdAsString()));
				nameValuePairs.add(new BasicNameValuePair(Server.POST_DATA_NAME, game.getPerson().getName()));
				nameValuePairs.add(new BasicNameValuePair(Server.POST_DATA_GAME_ID, game.getId()));

				serverResponse = Server.postToServer(serverEndPoint, nameValuePairs);
			}
			catch (final IOException e)
			{
				Log.e(LOG_TAG, "IOException error in ServerApiGame to " + serverEndPoint +  " - doInBackground()!", e);
			}
		}

		return serverResponse;
	}


	@Override
	protected abstract int getDialogMessage();


	@Override
	protected abstract int getDialogTitle();


	@Override
	protected abstract int getProgressDialogMessage();


	/**
	 * @return
	 * Returns the server address that this Classy Games API end point needs to
	 * hit.
	 */
	protected abstract String getServerEndPoint();


}
