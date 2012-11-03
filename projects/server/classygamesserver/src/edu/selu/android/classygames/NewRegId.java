package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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


	private static final long serialVersionUID = 1L;


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

		if (id < 0 || name == null || reg_id == null || name.equals(Utilities.BLANK) || reg_id.equals(Utilities.BLANK))
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			int part = -4;
			Connection sqlConnection = null;
			part = -3;
			PreparedStatement sqlStatement = null;
			part = -2;

			try
			{
				part = -1;
				sqlConnection = Utilities.getSQLConnection();
				part = 0;

				// prepare a SQL statement to be run on the MySQL database
				final String sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_USERS_FORMAT + " " + Utilities.DATABASE_TABLE_USERS + " VALUES (?, ?, ?);";
				part = 2;
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);
				part = 3;

				// prevent SQL injection by inserting user data this way
				sqlStatement.setLong(1, id);
				part = 4;
				sqlStatement.setString(2, name);
				part = 5;
				sqlStatement.setString(3, reg_id);
				part = 6;

				// run the SQL statement
				sqlStatement.executeUpdate();
				part = 7;

				printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_DATABASE_QUERIED));
			}
			catch (final ClassNotFoundException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_LOAD));
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT + " part: \"" + part + "\" message: \"" + e.getMessage() + "\" state: \"" + e.getSQLState() + "\" code: \"" + e.getErrorCode() + "\""));
			}
			finally
			// it's best to release SQL resources in reverse order of their creation
			// https://dev.mysql.com/doc/refman/5.0/en/connector-j-usagenotes-statements.html#connector-j-examples-execute-select
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


}
