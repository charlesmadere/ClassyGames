package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class NewGame
 */
public class NewGame extends HttpServlet
{


	private final static long serialVersionUID = 1L;


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

		final Long user_challenged = new Long(request.getParameter(Utilities.POST_DATA_USER_CHALLENGED));
		final Long user_creator = new Long(request.getParameter(Utilities.POST_DATA_USER_CREATOR));
		final String board = request.getParameter(Utilities.POST_DATA_BOARD);

		if (user_challenged < 0 || user_creator < 0 || board == null || board.equals(Utilities.BLANK))
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_MALFORMED));
		}
		else
		{
			Connection sqlConnection = null;
			PreparedStatement sqlStatement = null;

			try
			{
				sqlConnection = Utilities.getSQLConnection();

				// prepare a SQL statement to be run on the database
				final String sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_GAMES + " " + Utilities.DATABASE_TABLE_GAMES_FORMAT + " VALUES (?, ?, ?, ?, ?)";
				sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

				try
				{
					final String digest = createDigest
					(
						user_challenged.toString().getBytes(Utilities.UTF8),
						user_creator.toString().getBytes(Utilities.UTF8),
						board.toString().getBytes(Utilities.UTF8)
					);

					// prevent SQL injection by inserting user data this way
					sqlStatement.setString(1, digest);
					sqlStatement.setLong(2, user_creator);
					sqlStatement.setLong(3, user_challenged);
					sqlStatement.setString(4, board);
					sqlStatement.setInt(5, Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE);

					printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
				}
				catch (final NoSuchAlgorithmException e)
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_COULD_NOT_CREATE_GAME_ID));
				}
				catch (final UnsupportedEncodingException e)
				{
					printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_COULD_NOT_CREATE_GAME_ID));
				}
			}
			catch (final ClassNotFoundException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_LOAD));
			}
			catch (final SQLException e)
			{
				printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_CONNECT));
			}
			finally
			{
				Utilities.closeSQL(sqlConnection, sqlStatement);
			}
		}
	}


	private String createDigest(final byte[] user_challenged_bytes, final byte[] user_creator_bytes, final byte[] board_bytes) throws NoSuchAlgorithmException, UnsupportedEncodingException
	// huge hash creation algorithm magic below
	{
		// create a digest to use as the Game ID. We are going to be using the Utilities.MESSAGE_DIGEST_ALGORITHM as our
		// hash generation algorithm. At the time of this writing it's SHA-256, but plenty more algorithms are available.
		final MessageDigest digest = MessageDigest.getInstance(Utilities.MESSAGE_DIGEST_ALGORITHM);

		// create a Random object. This will be used to generate some random values to use in the creation of a Game ID.
		// We're seeding it with the epoch in milliseconds because this will 100% certainly always be a different number
		// every single time that it's run, guaranteeing a strong seed.
		Random random = new Random(System.currentTimeMillis());

		// Build the digest. As can be seen, we're using a bunch of different variables here. The more data we use here
		// the better our digest will be.
		digest.update(user_challenged_bytes);
		digest.update(user_creator_bytes);
		digest.update(board_bytes);
		digest.update(new Integer(random.nextInt()).toString().getBytes(Utilities.UTF8));

		StringBuilder digestBuilder = new StringBuilder(new BigInteger(digest.digest()).abs().toString(Utilities.MESSAGE_DIGEST_RADIX));

		for (int nibble = 0; digestBuilder.length() < Utilities.MESSAGE_DIGEST_LENGTH; )
		// we want a digest that's Utilities.MESSAGE_DIGEST_LENGTH characters in length. At the time of this writing, we
		// are aiming for 64 characters long. Sometimes the digest algorithm will give us a bit less than that. So here
		// we're making up for that shortcoming by continuously adding random characters to the digest until we get 64
		{
			do
			// we don't want a negative number. keep generating random ints until we get one that's positive
			{
				// don't allow the random number we've generated to be above 15
				nibble = random.nextInt() % 16;
			}
			while (nibble < 0);

			switch (nibble)
			{
				case 0:
					digestBuilder.append('0');
					break;

				case 1:
					digestBuilder.append('1');
					break;

				case 2:
					digestBuilder.append('2');
					break;

				case 3:
					digestBuilder.append('3');
					break;

				case 4:
					digestBuilder.append('4');
					break;

				case 5:
					digestBuilder.append('5');
					break;

				case 6:
					digestBuilder.append('6');
					break;

				case 7:
					digestBuilder.append('7');
					break;

				case 8:
					digestBuilder.append('8');
					break;

				case 9:
					digestBuilder.append('9');
					break;

				case 10:
					digestBuilder.append('a');
					break;

				case 11:
					digestBuilder.append('b');
					break;

				case 12:
					digestBuilder.append('c');
					break;

				case 13:
					digestBuilder.append('d');
					break;

				case 14:
					digestBuilder.append('e');
					break;

				default:
					digestBuilder.append('f');
					break;
			}
		}

		return digestBuilder.toString();
	}


}
