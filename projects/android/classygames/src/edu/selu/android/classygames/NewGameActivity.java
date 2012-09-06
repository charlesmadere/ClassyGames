package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;


public class NewGameActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
	}


}
