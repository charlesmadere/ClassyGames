package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;


public class GamesListActivity extends SherlockActivity
{


	private ActionBar actionBar;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_list_activity);

		actionBar = getSupportActionBar();
		actionBar.setTitle(R.string.games_list_activity_title);
	}


}
