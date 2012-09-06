package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class GamesListActivity extends SherlockActivity
{


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
				Utilities.easyToastAndLog(GamesListActivity.this, "NEW GAME!");
			}
		});

		Button refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Utilities.easyToastAndLog(GamesListActivity.this, "REFRESHENING");
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.games_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId()) 
		{		
			case R.id.actionbar_new_game:
				Utilities.easyToastAndLog(GamesListActivity.this, "NUEVO JUEGO!!");
				startActivity(new Intent(GamesListActivity.this, NewGameActivity.class));
				return true;

			case R.id.actionbar_refresh:
				Utilities.easyToastAndLog(GamesListActivity.this, "REFRESHENING");
				return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
