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
 * Servlet implementation class NewMove
 */
public class NewMove extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewMove()
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

		final String game_id = request.getParameter(Utilities.POST_DATA_GAME_ID);
		final Long id_creator = new Long(request.getParameter(Utilities.POST_DATA_ID));
		final Long id_challenged = new Long(request.getParameter(Utilities.POST_DATA_ID_CHALLENGER));

		if (game_id == null || game_id.equals("") || id_creator < 0 || id_challenged < 0)
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_EMPTY));
		}
		else
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			try
			{
				sqlConnection = Utilities.getSQLConnection();
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
			}
			finally
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
				}
			}
		}
	}


}
