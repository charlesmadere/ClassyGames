package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class GetGames
 */
public class GetGames extends HttpServlet
{


	private final static long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetGames()
	{
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(Utilities.CONTENT_TYPE_JSON);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(Utilities.CONTENT_TYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		final Long id = new Long(request.getParameter(Utilities.POST_DATA_ID));

		if (id < 0)
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			try
			{
				sqlConnection = Utilities.getSQLConnection();

				// prepare a SQL statement to be run on the MySQL database
				String sqlStatementString = "SELECT * FROM " + Utilities.DATABASE_TABLE_GAMES + " WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED + " = ? AND (" + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR + " = ? OR " + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGED + " = ?)";
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

				// prevent SQL injection by inserting data this way
				sqlStatement.setByte(1, Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE);
				sqlStatement.setLong(2, id);
				sqlStatement.setLong(3, id);

				// run the SQL statement and acquire any return information
				final ResultSet sqlResult = sqlStatement.executeQuery();

				if (sqlResult.next())
				// check to see that we got some SQL return data
				{
					Map<Object, Object> jsonData = new LinkedHashMap<Object, Object>();
					List<Map<Object, Object>> turnYours = new LinkedList<Map<Object, Object>>();
					List<Map<Object, Object>> turnTheirs = new LinkedList<Map<Object, Object>>();

					do
					// loop through all of SQL return data
					{
						final String game_id = sqlResult.getString(Utilities.DATABASE_TABLE_GAMES_COLUMN_ID);
						final Long user_creator = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR);
						final Long user_challenged = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGED);
						final Timestamp last_move = sqlResult.getTimestamp(Utilities.DATABASE_TABLE_GAMES_COLUMN_LAST_MOVE);

						// initialize a JSONObject. All of the current game's data will be stored here. At the end of this
						// loop iteration this JSONObject will be added to one of the above JSONArrays
						Map<Object, Object> game = new LinkedHashMap<Object, Object>();

						Long user_id = new Long(0);
						String user_name = null;

						if (user_creator.longValue() == id)
						{
							user_id = user_challenged;
							user_name = findUserName(sqlConnection, user_challenged);
						}
						else
						{
							user_id = user_creator;
							user_name = findUserName(sqlConnection, user_creator);
						}

						// create JSON data that will be sent back to the client device
						game.put(Utilities.POST_DATA_ID, user_id);
						game.put(Utilities.POST_DATA_NAME, user_name);
						game.put(Utilities.POST_DATA_GAME_ID, game_id);
						game.put(Utilities.POST_DATA_LAST_MOVE, last_move.getTime() / 1000);

						switch (sqlResult.getByte(Utilities.DATABASE_TABLE_GAMES_COLUMN_TURN))
						{
							case Utilities.DATABASE_TABLE_GAMES_TURN_CREATOR:
							// it's the creator's turn
								if (user_creator.longValue() == id)
								{
									turnYours.add(game);
								}
								else
								{
									turnTheirs.add(game);
								}
								break;

							case Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED:
							// it's the challenger's turn
								if (user_challenged.longValue() == id)
								{
									turnYours.add(game);
								}
								else
								{
									turnTheirs.add(game);
								}
								break;
						}
					}
					while (sqlResult.next());

					jsonData.put(Utilities.POST_DATA_TURN_YOURS, turnYours);
					jsonData.put(Utilities.POST_DATA_TURN_THEIRS, turnTheirs);

					printWriter.write(Utilities.makePostDataSuccess(jsonData));
				}
				else
				// we did not get any SQL return data
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_SUCCESS_NO_ACTIVE_GAMES));
				}
			}
			catch (final ClassNotFoundException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_LOAD));
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
			}
			finally
			// it's best to release SQL resources in reverse order of their creation
			// https://dev.mysql.com/doc/refman/5.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


	/**
	 * <p>Query the database for a user who's ID matches the input's.</p>
	 * 
	 * @param sqlConnection
	 * Your existing database Connection object. Must already be connected, as this method makes no attempt
	 * at doing so.
	 * 
	 * @param user
	 * The ID of the user you're searching for as a long.
	 * 
	 * @return
	 * The name of the user that you queried for as a String.
	 */
	private String findUserName(final Connection sqlConnection, final long user)
	{
		try
		{
			// prepare a SQL statement to be run on the MySQL database
			final String sqlStatementString = "SELECT " + Utilities.DATABASE_TABLE_USERS_COLUMN_NAME + " FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
			PreparedStatement sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

			// prevent SQL injection by inserting data this way
			sqlStatement.setLong(1, user);

			// run the SQL statement and acquire any return information
			final ResultSet sqlResult = sqlStatement.executeQuery();

			// close this PreparedStatement as it's no longer needed
			Utilities.closeSQLStatement(sqlStatement);

			return sqlResult.getString(Utilities.DATABASE_TABLE_USERS_COLUMN_NAME);
		}
		catch (final SQLException e)
		{
			return Utilities.APP_NAME;
		}
	}


}
