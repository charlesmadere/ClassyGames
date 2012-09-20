package edu.selu.android.classygames;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

import edu.selu.android.classygames.games.Person;


public class NewGameActivity extends SherlockListActivity
{


	private ArrayList<Person> people;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
		
		//Get friends list
		try
		{
			Utilities.ensureFacebookIsNotNull();
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(Utilities.facebook);
			mAsyncRunner.request("/me/friends", new FriendsRequestListener());
		}
		catch (Exception e)
		{
			Utilities.easyToastAndLogError(NewGameActivity.this, "Failed retrieving your Facebook friends.");
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case android.R.id.home:
				finish();
	            return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private class FriendsRequestListener extends BaseRequestListener
	{


		@Override
		public void onComplete(final String response, final Object state)
		{
			try
			{
				final JSONArray friends = Util.parseJson(response).getJSONArray("data");

	            final int friendsLength = friends.length();
	            for (int i = 0; i < friendsLength; ++i)
	            {
	            	JSONObject friend = friends.getJSONObject(i);
	            	people.add(new Person(friend.getInt("id"), friend.getString("name")));
	            }
			}
			catch (FacebookError e)
			{
				e.printStackTrace();
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}


	}


}
