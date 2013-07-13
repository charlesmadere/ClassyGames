package com.charlesmadere.android.classygames.server;


import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;


/**
 * Allows data to be easily bundled up and sent to the Classy Games server.
 */
public class ApiData
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


	public void addKeyValuePair(final String key, final long value)
	{
		addKeyValuePair(key, Long.toString(value));
	}


	public void addKeyValuePair(final String key, final String value)
	{
		final BasicNameValuePair nameValuePair = new BasicNameValuePair(key, value);
		keyValuePairs.add(nameValuePair);
	}


}
