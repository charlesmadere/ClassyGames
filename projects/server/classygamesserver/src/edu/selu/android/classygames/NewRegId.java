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
 * Servlet implementation class NewRegId
 */
public class NewRegId extends HttpServlet
{


	private final static long serialVersionUID = 1L;


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

		if (id < 0 || name == null || name.isEmpty() || reg_id == null || reg_id.isEmpty())
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
				String sqlStatementString = "SELECT * FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

				// prevent SQL injection by inserting data this way
				sqlStatement.setLong(1, id);

				// run the SQL statement and acquire any return information
				final ResultSet sqlResult = sqlStatement.executeQuery();

				if (sqlResult.next())
				// the id already exists in the table therefore it's data needs to be updated
				{
					// prepare a SQL statement to be run on the database
					sqlStatementString = "UPDATE " + Utilities.DATABASE_TABLE_USERS + " SET " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?, " + Utilities.DATABASE_TABLE_USERS_COLUMN_NAME + " = ?, " + Utilities.DATABASE_TABLE_USERS_COLUMN_REG_ID + " = ? WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

					// prevent SQL injection by inserting user data this way
					sqlStatement.setLong(1, id);
					sqlStatement.setString(2, name);
					sqlStatement.setString(3, reg_id);
					sqlStatement.setLong(4, id);
				}
				else
				// id does not already exist in the table. let's insert it
				{
					// prepare a SQL statement to be run on the database
					sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_USERS + " " + Utilities.DATABASE_TABLE_USERS_FORMAT + " " + Utilities.DATABASE_TABLE_USERS_VALUES;
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

					// prevent SQL injection by inserting user data this way
					sqlStatement.setLong(1, id);
					sqlStatement.setString(2, name);
					sqlStatement.setString(3, reg_id);
				}

				// run the SQL statement
				sqlStatement.executeUpdate();

				printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
			}
			catch (final ClassNotFoundException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_LOAD));
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT + "\" message: \"" + e.getMessage() + "\" state: \"" + e.getSQLState() + "\" code: \"" + e.getErrorCode() + "\" reg_id:\"" + reg_id + "\""));
			}
			finally
			// it's best to release SQL resources in reverse order of their creation as seen here
			// https://dev.mysql.com/doc/refman/5.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


}
