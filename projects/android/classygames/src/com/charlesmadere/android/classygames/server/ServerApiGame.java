package com.charlesmadere.android.classygames.server;


import android.content.Context;
import android.util.Log;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import org.json.JSONException;

import java.io.IOException;


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
	protected ServerApiGame(final Context context, final Listeners listeners, final Game game)
	{
		super(context, listeners);
		this.game = game;
	}


	@Override
	protected String postToServer(final Person whoAmI)
	{
		String serverResponse = null;

		try
		{
			final ApiData data = new ApiData()
				.addKeyValuePair(Server.POST_DATA_USER_CREATOR, whoAmI.getId())
				.addKeyValuePair(Server.POST_DATA_USER_CHALLENGED, game.getPerson().getId())
				.addKeyValuePair(Server.POST_DATA_NAME, game.getPerson().getName())
				.addKeyValuePair(Server.POST_DATA_GAME_ID, game.getId());

			serverResponse = postToServer(data, game);
		}
		catch (final IOException e)
		{
			Log.e(LOG_TAG, "IOException error in ServerApiGame's doInBackground()!", e);
		}
		catch (final JSONException e)
		{
			Log.e(LOG_TAG, "JSONException error in ServerApiGame's doInBackground()!", e);
		}

		return serverResponse;
	}


	/**
	 * Performs the actual API call against the Classy Games server.
	 *
	 * @param data
	 * The actual data that needs to be sent to the server.
	 *
	 * @param game
	 * Holds data specific to the actual game that you're currently dealing with. Like the game's
	 * ID, the person that the current user is playing against...
	 *
	 * @return
	 * The String response as received from the server. This will be a JSON String.
	 *
	 * @throws IOException
	 * An IOException may occur when performing the HTTP POST request to the Classy Games server.
	 *
	 * @throws JSONException
	 * A JSONException may occur if this method performs any JSON encoding or parsing.
	 */
	protected abstract String postToServer(final ApiData data, final Game game)
		throws IOException, JSONException;


	@Override
	protected abstract int getDialogMessage();


	@Override
	protected abstract int getDialogTitle();


	@Override
	protected abstract int getProgressDialogMessage();


}
