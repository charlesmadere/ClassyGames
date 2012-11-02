package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;


/**
 * Servlet implementation class RemoveRegId
 */
public class RemoveRegId extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RemoveRegId()
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
		printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_ERROR_DATA_NOT_DETECTED));
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
				// connect to the MySQL database
				sqlConnection = Utilities.getSQLConnection();

				// parepare a SQL statement to be run on the MySQL database
				final String sqlStatementString = "DELETE FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_USERS_COLUMN_ID + " = ?";
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

				// prevent SQL injection by inserting user data this way
				sqlStatement.setLong(1, id);

				// run the SQL statement
				sqlStatement.executeUpdate();

				printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_USER_REMOVED_FROM_DATABASE));
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
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


}
