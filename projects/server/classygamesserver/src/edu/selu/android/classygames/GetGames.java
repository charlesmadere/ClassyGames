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

import edu.selu.android.classygames.utilities.Utilities;


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
	protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setContentType(Utilities.CONTENT_TYPE_JSON);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		response.setContentType(Utilities.CONTENT_TYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		final String id_parameter = request.getParameter(Utilities.POST_DATA_ID);

		if (id_parameter == null || id_parameter.isEmpty())
		// check for invalid inputs
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			final Long id = Long.valueOf(id_parameter);

			if (id.longValue() < 0)
			// check for invalid inputs
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
					sqlStatement.setLong(2, id.longValue());
					sqlStatement.setLong(3, id.longValue());

					// run the SQL statement and acquire any return information
					final ResultSet sqlResult = sqlStatement.executeQuery();

					if (sqlResult.next())
					// check to see that we got some SQL return data
					{
						Map<String, Object> jsonData = new LinkedHashMap<String, Object>();
						List<Map<String, Object>> turnYours = new LinkedList<Map<String, Object>>();
						List<Map<String, Object>> turnTheirs = new LinkedList<Map<String, Object>>();

						do
						// loop through all of SQL return data
						{
							final String game_id = sqlResult.getString(Utilities.DATABASE_TABLE_GAMES_COLUMN_ID);
							final Long user_creator = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR);
							final Long user_challenged = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGED);
							final Timestamp last_move = sqlResult.getTimestamp(Utilities.DATABASE_TABLE_GAMES_COLUMN_LAST_MOVE);

							// initialize a JSONObject. All of the current game's data will be stored here. At the end of this
							// loop iteration this JSONObject will be added to one of the above JSONArrays
							Map<String, Object> game = new LinkedHashMap<String, Object>();

							if (user_creator.longValue() == id.longValue())
							{
								game.put(Utilities.POST_DATA_ID, user_challenged);
								game.put(Utilities.POST_DATA_NAME, Utilities.grabUserName(sqlConnection, user_challenged));
							}
							else
							{
								game.put(Utilities.POST_DATA_ID, user_creator);
								game.put(Utilities.POST_DATA_NAME, Utilities.grabUserName(sqlConnection, user_creator));
							}

							game.put(Utilities.POST_DATA_GAME_ID, game_id);
							game.put(Utilities.POST_DATA_LAST_MOVE, last_move.getTime() / 1000);

							switch (sqlResult.getByte(Utilities.DATABASE_TABLE_GAMES_COLUMN_TURN))
							{
								case Utilities.DATABASE_TABLE_GAMES_TURN_CREATOR:
								// it's the creator's turn
									if (user_creator.longValue() == id.longValue())
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
									if (user_challenged.longValue() == id.longValue())
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
						printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_NO_ACTIVE_GAMES));
					}
				}
				catch (final SQLException e)
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
				}
				finally
				{
					Utilities.closeSQL(sqlConnection, sqlStatement);
				}
			}
		}
	}


}
