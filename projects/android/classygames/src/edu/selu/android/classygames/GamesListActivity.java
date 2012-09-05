package edu.selu.android.classygames;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class GamesListActivity extends SherlockActivity
{


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) 
		{
			case R.id.refresh_button:
			
				Context context = getApplicationContext();
				CharSequence text = "REFRESHENING";
				int duration = Toast.LENGTH_SHORT;
				
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
				return true;
				
			default:
				
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
	
		Button newGame = (Button) findViewById(R.id.new_game);
		newGame.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Context context = getApplicationContext();
				CharSequence text = "NEW GAME!";
				int duration = Toast.LENGTH_SHORT;
				
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
		
		Button refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Context context = getApplicationContext();
				CharSequence text = "REFRESHENING";
				int duration = Toast.LENGTH_SHORT;
				
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
	}

}
