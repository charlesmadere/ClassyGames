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
		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	// JSON data coming into this code should look something like this
	// {"id":"10443780","name":"Charles Madere","reg_id":"414931"}"
	// long, String, String
	{
		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		final Long id = new Long(request.getParameter(Utilities.POST_DATA_ID));
		final String name = request.getParameter(Utilities.POST_DATA_NAME);
		final String reg_id = request.getParameter(Utilities.POST_DATA_REG_ID);

		if (id < 0 || name == null || reg_id == null || name.equals(Utilities.POST_DATA_BLANK) || reg_id.equals(Utilities.POST_DATA_BLANK))
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			final String MySQLConnectionString = Utilities.getMySQLConnectionString();

			if (MySQLConnectionString == null)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CREATE_CONNECTION_STRING));
			}
			else
			{
				try
				{
					// connect to the MySQL database
					sqlConnection = DriverManager.getConnection(Utilities.getMySQLConnectionString());

					// prepare a SQL statement to be run on the MySQL database
					final String sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_USERS_FORMAT + " " + Utilities.DATABASE_TABLE_USERS + " VALUES (?, '?', '?');";
					sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

					// prevent SQL injection by inserting user data this way
					sqlStatement.setLong(1, id);
					sqlStatement.setString(2, name);
					sqlStatement.setString(3, reg_id);

					// run the SQL statement
					sqlStatement.executeUpdate();

					printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_USER_ADDED_TO_DATABASE));
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


}
