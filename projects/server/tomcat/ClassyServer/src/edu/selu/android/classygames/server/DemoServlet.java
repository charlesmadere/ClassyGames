package edu.selu.android.classygames.server;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Servlet implementation class DemoServlet
 */
@WebServlet("/DemoServlet")
public class DemoServlet extends HttpServlet
{


	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DemoServlet()
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
			printWriter.print(jsonData + "\n");
			printWriter.print(Utilities.JSON_DATA_ID + ": \"" + id + "\"\n");
			printWriter.print(Utilities.JSON_DATA_NAME + ": \"" + name + "\"\n");
			printWriter.print(Utilities.JSON_DATA_REG_ID + ": \"" + reg_id + "\"\n");
		}
	}


}
