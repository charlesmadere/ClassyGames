package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;


public class CentralFragmentActivity extends SherlockFragmentActivity implements GamesListFragment.OnGameSelectedListener
{


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.central_fragment_activity);

		if (findViewById(R.id.central_fragment_activity_fragment_container) != null)
		{
			if (savedInstanceState == null)
			{
				GamesListFragment gamesListFragment = new GamesListFragment();
				getSupportFragmentManager().beginTransaction().add(R.id.central_fragment_activity_fragment_container, gamesListFragment).commit();
			}
		}
	}


	@Override
	public void onGameSelected(final int position)
	{

	}


}
