package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


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
		printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	// JSON data coming into this code should look something like this
	// {"id":"10443780","id_challenger":"13371337"}"
	// long, long
	{
		final String jsonData = request.getParameter(Utilities.JSON_DATA);

		long id = -1;
		long id_challenger = -1;

		try
		{
			final JSONObject json = (JSONObject) new JSONParser().parse(jsonData);
			id = Long.parseLong((String) json.get(Utilities.JSON_DATA_ID));
			id_challenger = Long.parseLong((String) json.get(Utilities.JSON_DATA_ID_CHALLENGER));
		}
		catch (final ParseException e)
		{

		}

		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		if (id >= 0 && id_challenger >= 0)
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

			}
		}
		else
		{
			printWriter.print(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_EMPTY_OR_MALFORMED));
		}
	}


}
