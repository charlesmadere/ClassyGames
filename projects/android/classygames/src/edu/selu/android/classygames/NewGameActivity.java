package edu.selu.android.classygames;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;


public class NewGameActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		// makes the back arrow visible
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Get friends list
		try
		{
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(Utilities.facebook);
			Bundle bundle = new Bundle();
			bundle.putString("fields", "birthday");
			mAsyncRunner.request("me/friends", bundle, new FriendListRequestListener());
		}
		catch(Exception e)
		{
			Log.e(Utilities.LOG_TAG, "FRIENDS_LIST_FAIL" + e.getMessage());
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case android.R.id.home:
				
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, GamesListActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	            
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	//Friends List Adapter Defined
	public class FriendListRequestListener extends BaseRequestListener
	{


		String _error;


		public void onComplete(final String response)
		{
	        _error = null;

	        try
	        {
	            JSONObject json = Util.parseJson(response);
	            final JSONArray friends = json.getJSONArray("data");

	            NewGameActivity.this.runOnUiThread(new Runnable()
	            {
	                public void run()
	                {
	                    //Supposed to do stuff with array here
	                }
	            });

	        }
	        catch (JSONException e)
	        {
	            _error = "JSON Error in response";
	        }
	        catch (FacebookError e)
	        {
	            _error = "Facebook Error: " + e.getMessage();
	        }

	        if (_error != null)
	        {
	            NewGameActivity.this.runOnUiThread(new Runnable()
	            {
	                public void run()
	                {
	                    Toast.makeText(getApplicationContext(), "Error occurred: " + _error, Toast.LENGTH_LONG).show();
	                }
	            });
	        }
	    }
		
		@Override
		public void onComplete(final String response, final Object state)
		{

		}


	}


}
