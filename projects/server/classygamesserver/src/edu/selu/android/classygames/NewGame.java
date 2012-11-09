package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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


	private final static byte RUN_STATUS_NO_ERROR = 0;
	private final static byte RUN_STATUS_UNSUPPORTED_ENCODING = 1;
	private final static byte RUN_STATUS_NO_SUCH_ALGORITHM = 2;

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
				byte runStatus = RUN_STATUS_NO_ERROR;

				for (boolean continueToRun = true; continueToRun; )
				// 
				{
					// prepare a String to hold a digest in
					String digest = null;

					try
					{
						digest = createDigest
						(
							user_challenged.toString().getBytes(Utilities.UTF8),
							user_creator.toString().getBytes(Utilities.UTF8),
							board.toString().getBytes(Utilities.UTF8)
						);
					}
					catch (final NoSuchAlgorithmException e)
					{
						runStatus = RUN_STATUS_NO_SUCH_ALGORITHM;
					}
					catch (final UnsupportedEncodingException e)
					{
						runStatus = RUN_STATUS_UNSUPPORTED_ENCODING;
					}

					if (runStatus != RUN_STATUS_NO_ERROR || digest == null)
					{
						continueToRun = false;
					}
					else
					{
						// prepare a SQL statement to be run on the database
						String sqlStatementString = "SELECT " + Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED + " FROM " + Utilities.DATABASE_TABLE_USERS + " WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_ID + " = ?";
						sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

						// prevent SQL injection by inserting data this way
						sqlStatement.setString(1, digest);

						// run the SQL statement and acquire any return information
						ResultSet sqlResult = sqlStatement.executeQuery();

						if (sqlResult.next())
						// the digest we created to use as an ID already exists in the games table
						{
							if (sqlResult.getByte(Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED) == Utilities.DATABASE_TABLE_GAMES_FINISHED_TRUE)
							// Game with the digest we created already exists, AND has already been finished. Because of this, we
							// can safely replace that game's data with our new game's data
							{
								// close the SQL statement as we are going to reuse it now
								Utilities.closeSQLStatement(sqlStatement);

								// prepare a SQL statement to be run on the database
								sqlStatementString = "UPDATE " + Utilities.DATABASE_TABLE_GAMES + " SET " + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR + " = ?, " + Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGED + " = ?, " + Utilities.DATABASE_TABLE_GAMES_COLUMN_BOARD + " = ?, " + Utilities.DATABASE_TABLE_GAMES_COLUMN_TURN + " = ?, " + Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED + " = ? WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_ID + " = ?";
								sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

								// prevent SQL injection by inserting data this way
								sqlStatement.setLong(1, user_creator);
								sqlStatement.setLong(2, user_challenged);
								sqlStatement.setString(3, board);
								sqlStatement.setByte(4, Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED);
								sqlStatement.setByte(5, Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE);

								continueToRun = false;
							}
						}
						else
						// the digest that we created to use as an ID DOES NOT already exist in the games table. We we can now
						// just simply insert this new game's data into the table
						{
							// close the sql statement as we are going to reuse it now
							Utilities.closeSQLStatement(sqlStatement);

							// prepare a SQL statement to be run on the database
							sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_GAMES + " " + Utilities.DATABASE_TABLE_GAMES_FORMAT + " " + Utilities.DATABASE_TABLE_GAMES_VALUES;
							sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

							// prevent SQL injection by inserting data this way
							sqlStatement.setString(1, digest);
							sqlStatement.setLong(2, user_creator);
							sqlStatement.setLong(3, user_challenged);
							sqlStatement.setString(4, board);
							sqlStatement.setByte(5, Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED);
							sqlStatement.setInt(6, Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE);

							continueToRun = false;
						}
					}
				}

				switch (runStatus)
				// we may have hit an error in the above loop
				{
					case RUN_STATUS_NO_ERROR:
						printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_GENERIC));
						break;

					case RUN_STATUS_NO_SUCH_ALGORITHM:
						printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_COULD_NOT_CREATE_GAME_ID));
						break;

					case RUN_STATUS_UNSUPPORTED_ENCODING:
						printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_COULD_NOT_CREATE_GAME_ID));
						break;
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
			// add a hexadecimal character onto the end of the StringBuilder
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

		while (digestBuilder.length() > Utilities.MESSAGE_DIGEST_LENGTH)
		// ensure that our digest is only MESSAGE_DIGEST_LENGTH characters long. At the time of this
		// writing that value is 64. This will delete the very last character from the StringBuilder,
		// one at a time.
		{
			digestBuilder.deleteCharAt(digestBuilder.length() - 1);
		}

		return digestBuilder.toString();
	}


}
