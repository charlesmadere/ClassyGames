package edu.selu.android.classygames;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.selu.android.classygames.utilities.GCMUtilities;
import edu.selu.android.classygames.utilities.GameUtilities;
import edu.selu.android.classygames.utilities.Utilities;


/**
 * Servlet implementation class NewMove
 */
public class NewMove extends HttpServlet
{


	private final static long serialVersionUID = 1L;


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

		final String game_id = request.getParameter(Utilities.POST_DATA_GAME_ID);
		final String user_id_parameter = request.getParameter(Utilities.POST_DATA_ID);
		final String user_opponent_parameter = request.getParameter(Utilities.POST_DATA_USER_CHALLENGED);
		final String user_opponent_name = request.getParameter(Utilities.POST_DATA_NAME);
		final String board = request.getParameter(Utilities.POST_DATA_BOARD);

		if (game_id == null || game_id.isEmpty() || user_id_parameter == null || user_id_parameter.isEmpty()
			|| user_opponent_parameter == null || user_opponent_parameter.isEmpty()
			|| user_opponent_name == null || user_opponent_name.isEmpty() || board == null || board.isEmpty())
		// check for invalid inputs
		{
			printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATA_IS_EMPTY));
		}
		else
		{
			final Long user_id = Long.valueOf(user_id_parameter);
			final Long user_opponent = Long.valueOf(user_opponent_parameter);

			if (user_id.longValue() < 0 || user_opponent.longValue() < 0)
			// check for invalid inputs
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

					if (Utilities.ensureUserExistsInDatabase(sqlConnection, user_opponent, user_opponent_name))
					{
						// prepare a SQL statement to be run on the database
						String sqlStatementString = "SELECT * FROM " + Utilities.DATABASE_TABLE_GAMES + " WHERE " + Utilities.DATABASE_TABLE_GAMES_COLUMN_ID + " = ?";
						sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

						// prevent SQL injection by inserting data this way
						sqlStatement.setString(1, game_id);

						// run the SQL statement and acquire any return information
						final ResultSet sqlResult = sqlStatement.executeQuery();

						if (sqlResult.next())
						{
							if (sqlResult.getByte(Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED) == Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE)
							// make sure that the game has not been finished
							{
								final long user_creator = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR);
								final long user_challenged = sqlResult.getLong(Utilities.DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGED);
								final byte turn = sqlResult.getByte(Utilities.DATABASE_TABLE_GAMES_COLUMN_TURN);

								if ((user_id.longValue() == user_creator && turn == Utilities.DATABASE_TABLE_GAMES_TURN_CREATOR)
									|| (user_id.longValue() == user_challenged && turn == Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED))
								{
									final Byte board_validation_result = Byte.valueOf(GameUtilities.checkBoardValidityAndStatus(sqlResult.getString(Utilities.DATABASE_TABLE_GAMES_COLUMN_BOARD), board, Utilities.BOARD_NEW_MOVE));

									if (board_validation_result.byteValue() == Utilities.BOARD_NEW_MOVE || board_validation_result.byteValue() == Utilities.BOARD_WIN)
									{
										// close the PreparedStatement as it is no longer needed
										Utilities.closeSQLStatement(sqlStatement);

										// prepare a SQL statement to be run on the database
										sqlStatementString = "INSERT INTO " + Utilities.DATABASE_TABLE_GAMES + " (" + Utilities.DATABASE_TABLE_GAMES_COLUMN_BOARD + ", " + Utilities.DATABASE_TABLE_GAMES_COLUMN_TURN + ", " + Utilities.DATABASE_TABLE_GAMES_COLUMN_FINISHED + ") VALUES (?, ?, ?)";
										sqlStatement = sqlConnection.prepareStatement(sqlStatementString);

										// prevent SQL injection by inserting data this way
										sqlStatement.setString(1, board);

										switch (turn)
										{
											case Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED:
												sqlStatement.setByte(2, Utilities.DATABASE_TABLE_GAMES_TURN_CREATOR);
												break;

											case Utilities.DATABASE_TABLE_GAMES_TURN_CREATOR:
												sqlStatement.setByte(2, Utilities.DATABASE_TABLE_GAMES_TURN_CHALLENGED);
												break;
										}

										switch (board_validation_result.byteValue())
										{
											case Utilities.BOARD_NEW_MOVE:
												sqlStatement.setByte(3, Utilities.DATABASE_TABLE_GAMES_FINISHED_FALSE);
												break;

											case Utilities.BOARD_WIN:
												sqlStatement.setByte(3, Utilities.DATABASE_TABLE_GAMES_FINISHED_TRUE);
												break;
										}

										// run the SQL statement
										sqlStatement.executeUpdate();

										// TODO
										// change to send message instead to user_opponent.longValue()
										GCMUtilities.sendMessage(sqlConnection, game_id, user_id.longValue(), user_opponent_name, board_validation_result);
										printWriter.write(Utilities.makePostDataSuccess(Utilities.POST_SUCCESS_MOVE_ADDED_TO_DATABASE));
									}
								}
								else
								{
									printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_ITS_NOT_YOUR_TURN));
								}
							}
							else
							// we are trying to add a new move to a game that is already finished. this should never happen
							{
								printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_GAME_IS_ALREADY_OVER));
							}
						}
						else
						{
							printWriter.write(Utilities.makePostDataError(Utilities.POST_ERROR_DATABASE_COULD_NOT_FIND_GAME_WITH_SPECIFIED_ID));
						}
					}
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
	}


}
