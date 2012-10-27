package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class NewGame
 */
public class NewGame extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewGame()
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
		printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	// JSON data coming into this code should look something like this
	// {"id":"10443780","id_challenger":"13371337"}"
	// long, long
	{
		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		final Long id = new Long(request.getParameter(Utilities.POST_DATA_ID));
		final Long id_challenger = new Long(request.getParameter(Utilities.POST_DATA_ID_CHALLENGER));

		if (id < 0 || id_challenger < 0)
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			final String MySQLConnectionString = Utilities.getMySQLConnectionString();

			if (MySQLConnectionString == null || MySQLConnectionString.equals(""))
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CREATE_CONNECTION_STRING));
			}
			else
			{
				printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_DATABASE_QUERIED));
			}
		}
	}


}
