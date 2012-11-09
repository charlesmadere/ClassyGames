package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class GetGame
 */
public class GetGame extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetGame()
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

		final String id = request.getParameter(Utilities.POST_DATA_ID);

		if (id == null || id.equals(Utilities.BLANK))
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

				// prepare a SQL statement to be run on the database
				final String sqlStatementString = "SELECT " + Utilities.DATABASE_TABLE_GAMES_COLUMN_BOARD + " FROM " + Utilities.DATABASE_TABLE_GAMES + " WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_ID + " = ?";
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

				// prevent SQL injection by inserting user data this way
				sqlStatement.setString(1, id);

				// run the SQL statement and acquire any return information
				final ResultSet sqlResult = sqlStatement.executeQuery();

				if (sqlResult.next())
				// game with specified id was found in the database, send the board's data to the client
				{
					printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
				}
				else
				// we could not find a game with specified id in the database. this should never happen
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_FIND_GAME_WITH_SPECIFIED_ID));
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
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


}