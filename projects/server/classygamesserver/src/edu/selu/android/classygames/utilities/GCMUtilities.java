package edu.selu.android.classygames.utilities;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;


public class GCMUtilities
{


	private final static int RETRY_ATTEMPTS = 5;


	/**
	 * Sends a Google Cloud Message (GCM) to the user specified by the user_id parameter.
	 * Some of the code here was taken from this guide:
	 * https://developer.android.com/guide/google/gcm/gs.html#server-app
	 * 
	 * @param sqlConnection
	 * An existing connection to the SQL database as this code makes no attempt to either
	 * open or close the connection.
	 * 
	 * @param user_id
	 * The ID of the user that you want to send a Google Cloud Message to.
	 * 
	 * 
	 */
	public static void sendMessage(final Connection sqlConnection, final String game_id, final Long user_id, final String user_name, final Byte gameType)
	{
		final String reg_id = grabUserRegId(sqlConnection, user_id);

		if (reg_id != null && !reg_id.isEmpty())
		// ensure that we were able to grab a valid regId for the user
		{
			final Sender sender = new Sender(SecretConstants.GOOGLE_API_KEY);

			// build the message that will be sent to the client device
			// https://developer.android.com/guide/google/gcm/server-javadoc/index.html
			final Message message = new Message.Builder()
				.addData(Utilities.POST_DATA_GAME_ID, game_id)
				.addData(Utilities.POST_DATA_ID, user_id.toString())
				.addData(Utilities.POST_DATA_NAME, user_name)
				.addData(Utilities.POST_DATA_TYPE, gameType.toString())
				.build();

			try
			{
				final Result result = sender.send(message, reg_id, RETRY_ATTEMPTS);
				final String messageId = result.getMessageId();

				if (messageId != null && !messageId.isEmpty())
				{
					final String canonicalRegId = result.getCanonicalRegistrationId();

					if (canonicalRegId != null && !canonicalRegId.isEmpty())
					// same device has more than one registration ID: update database. Replace
					// the existing regId with this new one
					{
						Utilities.updateUserRegId(sqlConnection, reg_id, user_id.longValue());
					}
				}
				else
				{
					final String errorCodeName = result.getErrorCodeName();

					if (errorCodeName.equals(Constants.ERROR_NOT_REGISTERED))
					// application has been removed from device - unregister database
					{
						Utilities.removeUserRegId(sqlConnection, user_id);
					}
				}
			}
			catch (final IOException e)
			{

			}
		}
	}


	/**
	 * Test method. This shouldn't be used once development is complete. Directly sends a bad GCM
	 * message to the given user.
	 */
	public static void sendMessage(final Connection sqlConnection, final Long user_id, final String user_name)
	{
		sendMessage(sqlConnection, "abcd", user_id, user_name, Utilities.POST_DATA_TYPE_NEW_GAME);
	}


	/**
	 * Finds and then returns a user's reg_id. 
	 * 
	 * @param sqlConnection
	 * An existing connection to the database. This method will make no attempt to either
	 * open or close the connection.
	 * 
	 * @param user_id
	 * ID of the user that you want to find a reg_id for.
	 * 
	 * @return
	 * Returns the reg_id of the user that you want as a String. If the user could not be
	 * found, null is returned.
	 */
	private static String grabUserRegId(final Connection sqlConnection, final Long user_id)
	{
		PreparedStatement sqlStatement = null;
		String reg_id = null;

		try
		{
			// prepare a SQL statement to be run on the database
			final String sqlStatementString = "SELECT " + Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID + " FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
			sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

			// prevent SQL injection by inserting data this way
			sqlStatement.setLong(1, user_id.longValue());

			// run the SQL statement and acquire any return information
			final ResultSet sqlResult = sqlStatement.executeQuery();

			if (sqlResult.next())
			// user with specified id was found in the database
			{
				reg_id = sqlResult.getString(Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID);
			}
		}
		catch (final SQLException e)
		{

		}
		finally
		{
			Utilities.closeSQLStatement(sqlStatement);
		}

		return reg_id;
	}


}
