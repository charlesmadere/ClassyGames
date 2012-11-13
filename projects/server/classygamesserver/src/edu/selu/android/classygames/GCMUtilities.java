package edu.selu.android.classygames;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;


public class GCMUtilities
{


	public final static int RETRY_ATTEMPTS = 5;


	/**
	 * Sends a Google Cloud Message (GCM) to the user specified by the user_id parameter.
	 * 
	 * @param user_id
	 * The ID of the user that you want to send the Google Cloud Message to.
	 */
	public static void sendMessage(final long user_id)
	{
		final String reg_id = grabUsersRegId(user_id);

		if (reg_id != null && !reg_id.isEmpty())
		{
			Sender sender = new Sender(SecretConstants.GOOGLE_API_KEY);
			Message message = new Message.Builder().build();
			// TODO
		}
	}


	private static String grabUsersRegId(final long user_id)
	{
		Connection sqlConnection = null;
		PreparedStatement sqlStatement = null;
		String reg_id = null;

		try
		{
			sqlConnection = Utilities.getSQLConnection();

			// prepare a SQL statement to be run on the database
			final String sqlStatementString = "SELECT " + Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID + " FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
			sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

			// prevent SQL injection by inserting data this way
			sqlStatement.setLong(1, user_id);

			// run the SQL statement and acquire any return information
			final ResultSet sqlResult = sqlStatement.executeQuery();

			if (sqlResult.next())
			// user with specified id was found in the database
			{
				reg_id = sqlResult.getString(Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID);
			}
		}
		catch (final ClassNotFoundException e)
		{

		}
		catch (final SQLException e)
		{

		}
		finally
		{
			Utilities.closeSQL(sqlConnection, sqlStatement);
		}

		return reg_id;
	}


}
