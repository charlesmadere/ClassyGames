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

import edu.selu.android.classygames.utilities.GCMUtilities;
import edu.selu.android.classygames.utilities.Utilities;



/**
 * Servlet implementation class NewRegId
 */
public class NewRegId extends HttpServlet
{


	private final static long serialVersionUID = 1L;
	private static int part = 0;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewRegId()
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
		final String name = request.getParameter(Utilities.POST_DATA_NAME);
		final String reg_id = request.getParameter(Utilities.POST_DATA_REG_ID);

		if (id.longValue() < 0 || name == null || name.isEmpty() || reg_id == null || reg_id.isEmpty())
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
				part = 0;
				sqlConnection = Utilities.getSQLConnection();
				part = 1;

				// prepare a SQL statement to be run on the database
				String sqlStatementString = "SELECT * FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
				part = 2;
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);
				part = 3;

				// prevent SQL injection by inserting data this way
				sqlStatement.setLong(1, id.longValue());
				part = 4;

				// run the SQL statement and acquire any return information
				final ResultSet sqlResult = sqlStatement.executeQuery();
				part = 5;

				if (sqlResult.next())
				// the id already exists in the table therefore it's data needs to be updated
				{
					part = 6;
					// prepare a SQL statement to be run on the database
					sqlStatementString = "UPDATE " + Utilities.DATABASE_TABLE_USERS + " SET " + Utilities.DATABASE_TABLE_USERS_COLUMN_NAME + " = ?, " + Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID + " = ? WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
					part = 7;
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);
					part = 8;

					// prevent SQL injection by inserting data this way
					sqlStatement.setString(1, name);
					part = 9;
					sqlStatement.setString(2, reg_id);
					part = 10;
					sqlStatement.setLong(3, id.longValue());
					part = 11;
				}
				else
				// id does not already exist in the table. let's insert it
				{
					part = 12;
					// prepare a SQL statement to be run on the database
					sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_USERS + " " + Utilities.DATABASE_TABLE_USERS_FORMAT + " " + Utilities.DATABASE_TABLE_USERS_VALUES;
					part = 13;
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);
					part = 14;

					// prevent SQL injection by inserting data this way
					sqlStatement.setLong(1, id.longValue());
					part = 15;
					sqlStatement.setString(2, name);
					part = 16;
					sqlStatement.setString(3, reg_id);
					part = 17;
				}

				part = 18;
				// run the SQL statement
				sqlStatement.executeUpdate();
				part = 19;

				printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
				part = 20;

				// TODO
				// remove this soon
				GCMUtilities.sendMessage(sqlConnection, id, name);
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT + " p: " + part + " " + e.getMessage()));
			}
			finally
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


}
