package edu.selu.android.classygames;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;


public class NewGameActivity extends SherlockListActivity
{


	private ArrayList<String> friends;
	private ProgressDialog progressDialog;
	private Runnable runnable;


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
//			Bundle bundle = new Bundle();
//			bundle.putString("fields", "birthday");
//			mAsyncRunner.request("me/friends", bundle, new FriendListRequestListener());
		}
		catch (Exception e)
		{
			Utilities.easyToastAndLogError(NewGameActivity.this, "FRIENDS_LIST_FAIL");
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


	//Friends List Adapter Defined
	public class FriendsRequestListener extends BaseRequestListener
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
	                    Utilities.easyToastAndLogError(NewGameActivity.this, "Error occurred: " + _error);
	                }
	            });
	        }
	    }
		
		@Override
		public void onComplete(final String response, final Object state)
		{
//			Utilities.easyToastAndLog(NewGameActivity.this, "completell");
		}


	}


}
