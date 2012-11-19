package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.selu.android.classygames.utilities.Utilities;


/**
 * Servlet implementation class RemoveRegId
 */
public class RemoveRegId extends HttpServlet
{


	private final static long serialVersionUID = 1L;


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

		final String user_id_parameter = request.getParameter(Utilities.POST_DATA_ID);

		if (user_id_parameter == null || user_id_parameter.isEmpty())
		// check for invalid inputs
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			final Long user_id = Long.valueOf(user_id_parameter);

			if (user_id.longValue() < 0)
			// check for invalid inputs
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
			}
			else
			{
				Connection sqlConnection = null;

				try
				{
					sqlConnection = Utilities.getSQLConnection();
					Utilities.removeUserRegId(sqlConnection, user_id.longValue());
					printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_USER_REMOVED_FROM_DATABASE));
				}
				catch (final SQLException e)
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
				}
				finally
				{
					Utilities.closeSQLConnection(sqlConnection);
				}
			}
		}
	}


}
