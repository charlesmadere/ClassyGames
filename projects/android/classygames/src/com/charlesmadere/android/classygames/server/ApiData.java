package com.charlesmadere.android.classygames.server;


import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;


/**
 * A data type that should be used to bundle up data that is to be sent to the Classy Games
 * server.
 */
public final class ApiData
{


	/**
	 * The data that is going to be sent to the server.
	 */
	private LinkedList<BasicNameValuePair> keyValuePairs;


	public ApiData()
	{
		keyValuePairs = new LinkedList<BasicNameValuePair>();
	}


	public LinkedList<BasicNameValuePair> getKeyValuePairs()
	{
		return keyValuePairs;
	}


	public ApiData addKeyValuePair(final String key, final long value)
	{
		addKeyValuePair(key, String.valueOf(value));
		return this;
	}


	public ApiData addKeyValuePair(final String key, final String value)
	{
		final BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
		keyValuePairs.add(nameValuePair);
		return this;
	}


}
