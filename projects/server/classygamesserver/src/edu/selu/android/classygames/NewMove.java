package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
		response.setContentType(Utilities.MIMETYPE_JSON);
		PrintWriter printWriter = response.getWriter();
		printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_ERROR_DATA_NOT_DETECTED));
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	// JSON data coming into this code should look something like this
	// 
	// 
	{
		response.setContentType(Utilities.MIMETYPE_JSON);
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
			printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
		}
	}


}
