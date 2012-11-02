package edu.selu.android.classygames;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONValue;

import com.mysql.jdbc.Connection;


public class Utilities
{


	public final static String APP_NAME = "Classy Games";
	public final static String BLANK = "";

	public final static String CHARSET_UTF8 = "charset=utf-8";
	public final static String MIMETYPE_HTML = "text/html";
	public final static String MIMETYPE_JSON = "application/json";
	public final static String CONTENT_TYPE_HTML = MIMETYPE_HTML + "; " + CHARSET_UTF8;
	public final static String CONTENT_TYPE_JSON = MIMETYPE_JSON + "; " + CHARSET_UTF8;

	public final static String DATABASE_TABLE_GAMES = "games";
	public final static String DATABASE_TABLE_GAMES_COLUMN_ID = "id";
	public final static String DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR = "user_creator";
	public final static String DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGER = "user_challenger";
	public final static String DATABASE_TABLE_GAMES_COLUMN_BOARD = "board";
	public final static String DATABASE_TABLE_GAMES_FORMAT = "(`" + DATABASE_TABLE_GAMES_COLUMN_ID + "`, `" + DATABASE_TABLE_GAMES_COLUMN_USER_CREATOR + "`, `" + DATABASE_TABLE_GAMES_COLUMN_USER_CHALLENGER + "`, `" + DATABASE_TABLE_GAMES_COLUMN_BOARD + "`)";

	public final static String DATABASE_TABLE_USERS = "users";
	public final static String DATABASE_TABLE_USERS_COLUMN_ID = "id";
	public final static String DATABASE_TABLE_USERS_COLUMN_NAME = "name";
	public final static String DATABASE_TABLE_USERS_COLUMN_REG_ID = "reg_id";
	public final static String DATABASE_TABLE_USERS_FORMAT = "(`" + DATABASE_TABLE_USERS_COLUMN_ID + "`, `" + DATABASE_TABLE_USERS_COLUMN_NAME + "`, `" + DATABASE_TABLE_USERS_COLUMN_REG_ID + "`)";

	public final static String POST_DATA_BOARD = "board";
	public final static String POST_DATA_GAME_ID = "game_id";
	public final static String POST_DATA_ID = "id";
	public final static String POST_DATA_ID_CHALLENGER = "id_challenger";
	public final static String POST_DATA_NAME = "name";
	public final static String POST_DATA_REG_ID = "reg_id";

	public final static String POST_ERROR_DATA_IS_EMPTY = "POST data is empty.";
	public final static String POST_ERROR_DATA_IS_MALFORMED = "POST data is malformed.";
	public final static String POST_ERROR_DATA_NOT_DETECTED = "No POST data detected.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_CONNECT = "Database connection was unable to be established.";
	public final static String POST_ERROR_DATABASE_COULD_NOT_CREATE_CONNECTION_STRING = "Database connection String was unable to be created.";
	public final static String POST_ERROR_GENERIC = "POST data received but an error occurred.";
	public final static String POST_SUCCESS_DATABASE_QUERIED = "Database successfully queried for data.";
	public final static String POST_SUCCESS_GENERIC = "POST data received.";
	public final static String POST_SUCCESS_USER_ADDED_TO_DATABASE = "You've been successfully registered with " + APP_NAME + ".";
	public final static String POST_SUCCESS_USER_REMOVED_FROM_DATABASE = "You've been successfully unregistered from " + APP_NAME + ".";
	public static String con;


	public static Connection getSQLConnection() throws SQLException
	{
		Properties sqlProperties = new Properties();
		sqlProperties.put("username", System.getProperty("RDS_USERNAME"));
		sqlProperties.put("password", System.getProperty("RDS_PASSWORD"));

		return DriverManager.getConnection(getSQLConnectionString(), System.getProperty("username"), System.getProperty("password"));
	}


	private static String getSQLConnectionString()
	{
		final String dbHostname = System.getProperty("RDS_HOSTNAME");
		final String dbPort = System.getProperty("RDS_PORT");
		final String dbName = System.getProperty("RDS_DB_NAME");

		if (dbName == null || dbHostname == null || dbPort == null || dbName.equals("") || dbHostname.equals("") || dbPort.equals(""))
		{
			con = null;
			return null;
		}
		else
		{
			con = "jdbc:mysql://" + dbHostname + ":" + dbPort + "/" + dbName;
			// http://docs.amazonwebservices.com/elasticbeanstalk/latest/dg/create_deploy_Java.rds.html
			return con;
		}
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
