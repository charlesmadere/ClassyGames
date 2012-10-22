package edu.selu.android.classygames.server;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Servlet implementation class AddNewRegId
 */
@WebServlet("/AddNewRegId")
public class AddNewRegId extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddNewRegId()
	{
		super();
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter printWriter = response.getWriter();
		printWriter.print(Utilities.POST_DATA_NOT_DETECTED);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// final String jsonData = "{\"id\":\"10443780\", \"name\":\"Charles Madere\", \"reg_id\":\"414931\"}";
		final String jsonData = request.getParameter(Utilities.JSON_DATA);

		long id = 0;
		String name = new String();
		String reg_id = new String();

		try
		{
			final JSONObject json = (JSONObject) new JSONParser().parse(jsonData);
			id = Long.parseLong((String) json.get(Utilities.JSON_DATA_ID));
			name = (String) json.get(Utilities.JSON_DATA_NAME);
			reg_id = (String) json.get(Utilities.JSON_DATA_REG_ID);
		}
		catch (final ParseException e)
		{

		}

		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();

		if (id < 1 || name.equals(Utilities.JSON_DATA_BLANK) || reg_id.equals(Utilities.JSON_DATA_BLANK))
		{
			printWriter.print(Utilities.POST_DATA_IS_EMPTY);
		}
		else
		{
			Connection connection = null;

			try
			{
				// connect to the MySQL database
				connection = DriverManager.getConnection(SecretConstants.AMAZON_RDS_ADDRESS, SecretConstants.AMAZON_RDS_USERNAME, SecretConstants.AMAZON_RDS_PASSWORD);

				// prepare a SQL statement to run on the MySQL database
				Statement sqlStatement = connection.createStatement();
				final String sqlStatementString = "INSERT INTO " + SecretConstants.AMAZON_RDS_TABLE_USERS_FORMAT + " " + SecretConstants.AMAZON_RDS_TABLE_USERS + " VALUES ();";

				// run the SQL statement
				final int result = sqlStatement.executeUpdate(sqlStatementString);
			}
			catch (final SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (connection != null)
				{
					try
					{
						connection.close();
					}
					catch (final SQLException e)
					{
						
					}
				}
			}
		}
	}


}
