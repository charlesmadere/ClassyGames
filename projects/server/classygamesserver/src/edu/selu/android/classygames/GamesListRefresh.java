package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Servlet implementation class GamesListRefresh
 */
public class GamesListRefresh extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GamesListRefresh()
	{
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();
		printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// final String jsonData = "{\"id\":\"10443780\"}";
		final String jsonData = request.getParameter(Utilities.JSON_DATA);

		long id = 0;

		try
		{
			final JSONObject json = (JSONObject) new JSONParser().parse(jsonData);
			id = Long.parseLong((String) json.get(Utilities.JSON_DATA_ID));
		}
		catch (final ParseException e)
		{

		}

		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		if (id >= 1)
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			final String MySQLConnectionString = Utilities.getMySQLConnectionString();

			if (MySQLConnectionString == null)
			{
				printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CREATE_CONNECTION_STRING));
			}
			else
			{
				try
				{
					// connect to the MySQL database
					sqlConnection = DriverManager.getConnection(Utilities.getMySQLConnectionString());

					// prepare a SQL statement to be run on the MySQL database
					final String sqlStatementString = "SELECT * FROM " + Utilities.DATABASE_TABLE_GAMES + " WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR + " = ? OR " + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGER + " = ?";
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

					// prevent SQL injection by querying for user data this way (this is safe)
					sqlStatement.setLong(0, id);
					sqlStatement.setLong(1, id);

					// run the SQL statement
					sqlStatement.executeUpdate();

					printWriter.print(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_DATABASE_QUERIED));
				}
				catch (final SQLException e)
				{
					printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
				}
				finally
				// it's best to release SQL resources in reverse order of their creation
				// https://dev.mysql.com/doc/refman/5.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
				{
					if (sqlStatement != null)
					{
						try
						{
							sqlStatement.close();
						}
						catch (final SQLException e)
						{

						}

						sqlStatement = null;
					}

					if (sqlConnection != null)
					{
						try
						{
							sqlConnection.close();
						}
						catch (final SQLException e)
						{

						}

						sqlConnection = null;
					}
				}
			}
		}
		else
		{
			printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_EMPTY_OR_MALFORMED));
		}
	}


}
