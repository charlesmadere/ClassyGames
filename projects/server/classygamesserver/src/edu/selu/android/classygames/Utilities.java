package edu.selu.android.classygames;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONValue;


public class Utilities
{


	public final static String APP_NAME = "Classy Games";
	public final static String BLANK = "";

	// list of algorithms found here
	// http://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#MessageDigest
	public final static String MESSAGE_DIGEST_ALGORITHM = "SHA-256";
	public final static int MESSAGE_DIGEST_LENGTH = 64;
	public final static int MESSAGE_DIGEST_RADIX = 16;

	public final static String UTF8 = "UTF-8";
	public final static String CHARSET = "charset=" + UTF8;
	public final static String MIMETYPE_HTML = "text/html";
	public final static String MIMETYPE_JSON = "application/json";
	public final static String CONTENT_TYPE_HTML = MIMETYPE_HTML + "; " + CHARSET;
	public final static String CONTENT_TYPE_JSON = MIMETYPE_JSON + "; " + CHARSET;

	public final static String DATABASE_TABLE_GAMES = "games";
	public final static String DATABASE_TABLE_GAMES_COLUMN_ID = "id";
	public final static String DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR = "user_creator";
	public final static String DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGER = "user_challenger";
	public final static String DATABASE_TABLE_GAMES_COLUMN_BOARD = "board";
	public final static String DATABASE_TABLE_GAMES_COLUMN_TURN = "turn";
	public final static String DATABASE_TABLE_GAMES_COLUMN_FINISHED = "finished";
	public final static int DATABASE_TABLE_GAMES_TURN_CREATOR = 0;
	public final static int DATABASE_TABLE_GAMES_TURN_CHALLENGED = 1;
	public final static int DATABASE_TABLE_GAMES_FINISHED_FALSE = 0;
	public final static int DATABASE_TABLE_GAMES_FINISHED_TRUE = 1;
	public final static String DATABASE_TABLE_GAMES_FORMAT = "(" + DATABASE_TABLE_GAMES_COLUMN_ID + ", " + DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR + ", " + DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGER + ", " + DATABASE_TABLE_GAMES_COLUMN_BOARD + ", " + DATABASE_TABLE_GAMES_COLUMN_TURN + ", " + DATABASE_TABLE_GAMES_COLUMN_FINISHED + ")";
	public final static String DATABASE_TABLE_USERS = "users";
	public final static String DATABASE_TABLE_USERS_COLUMN_ID = "id";
	public final static String DATABASE_TABLE_USERS_COLUMN_NAME = "name";
	public final static String DATABASE_TABLE_USERS_COLUMN_REG_ID = "reg_id";
	public final static String DATABASE_TABLE_USERS_FORMAT = "(" + DATABASE_TABLE_USERS_COLUMN_ID + ", " + DATABASE_TABLE_USERS_COLUMN_NAME + ", " + DATABASE_TABLE_USERS_COLUMN_REG_ID + ")";

	public final static String POST_DATA_BOARD = "board";
	public final static String POST_DATA_FINISHED = "finished";
	public final static String POST_DATA_ID = "id";
	public final static String POST_DATA_NAME = "name";
	public final static String POST_DATA_REG_ID = "reg_id";
	public final static String POST_DATA_TURN = "turn";
	public final static String POST_DATA_USER_CHALLENGED = "user_challenged";
	public final static String POST_DATA_USER_CREATOR = "user_creator";

	public final static String POST_ERROR_COULD_NOT_CREATE_GAME_ID = "Was unable to create a Game ID.";
	public final static String POST_ERROR_DATA_IS_EMPTY = "POST data is empty.";
	public final static String POST_ERROR_DATA_IS_MALFORMED = "POST data is malformed.";
	public final static String POST_ERROR_DATA_NOT_DETECTED = "No POST data detected.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_CONNECT = "Database connection was unable to be established.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_CREATE_CONNECTION_STRING = "Database connection String was unable to be created.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_FIND_GAME_WITH_SPECIFIED_ID = "Game could not be found with specified ID.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_LOAD = "Database DriverManager could not be loaded.";
	public final static String POST_ERROR_GENERIC = "POST data received but an error occurred.";
	public final static String POST_SUCCESS_DATABASE_QUERIED = "Database successfully queried for data.";
	public final static String POST_SUCCESS_GENERIC = "POST data received.";
	public final static String POST_SUCCESS_USER_ADDED_TO_DATABASE = "You've been successfully registered with " + APP_NAME + ".";
	public final static String POST_SUCCESS_USER_REMOVED_FROM_DATABASE = "You've been successfully unregistered from " + APP_NAME + ".";


	public static void closeSQL(Connection sqlConnection, PreparedStatement sqlStatement)
	{
		closeSQLStatement(sqlStatement);
		closeSQLConnection(sqlConnection);
	}


	public static void closeSQLConnection(Connection sqlConnection)
	{
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


	public static void closeSQLStatement(PreparedStatement sqlStatement)
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
	}


	public static Connection getSQLConnection() throws ClassNotFoundException, SQLException
	// I followed this guide to understand how to connect to the MySQL database that is created when
	// making a new Amazon Elastic Beanstalk application
	// http://docs.amazonwebservices.com/elasticbeanstalk/latest/dg/create_deploy_Java.rds.html
	{
		// ensure that the MySQL JDBC Driver has been loaded
		Class.forName("com.mysql.jdbc.Driver");

		// acquire database credentials
		final String hostname = System.getProperty("RDS_HOSTNAME");
		final String port = System.getProperty("RDS_PORT");
		final String dbName = System.getProperty("RDS_DB_NAME");
		final String username = System.getProperty("RDS_USERNAME");
		final String password = System.getProperty("RDS_PASSWORD");

		// return a new connection to the database
		return DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password);
	}


	private static String makePostData(final String message, final boolean hasError)
	{
		Map<String, String> result = new LinkedHashMap<String, String>();

		if (hasError)
		{
			result.put("error", message);
		}
		else
		{
			result.put("success", message);
		}

		Map<String, Map<String, String>> jsonData = new LinkedHashMap<String, Map<String, String>>();
		jsonData.put("result", result);

		return JSONValue.toJSONString(jsonData);
	}


	public static String makePostDataError(final String message)
	{
		return makePostData(message, true);
	}


	public static String makePostDataSuccess(final String message)
	{
		return makePostData(message, false);
	}


}
