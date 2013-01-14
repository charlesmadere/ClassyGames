package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import edu.selu.android.classygames.utilities.Utilities;


public class CentralFragmentActivity extends SherlockFragmentActivity implements GamesListFragment.OnGameSelectedListener
{


	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.central_fragment_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		sessionStatusCallback = new Session.StatusCallback()
		{
			@Override
			public void call(Session session, SessionState state, Exception exception)
			{
				onSessionStateChange(session, state, exception);
			}
		};

		uiHelper = new UiLifecycleHelper(CentralFragmentActivity.this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);

		if (findViewById(R.id.central_fragment_activity_fragment_container) != null)
		{
			if (savedInstanceState == null || savedInstanceState.isEmpty())
			{
				final GamesListFragment gamesListFragment = new GamesListFragment();
				getSupportFragmentManager().beginTransaction().add(R.id.central_fragment_activity_fragment_container, gamesListFragment).commit();
			}
		}
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}


	@Override
	public void onGameSelected(final int position)
	{

	}


	@Override
	protected void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}


	private void onSessionStateChange(final Session session, final SessionState state, final Exception exception)
	{
		if (isResumed)
		// only make changes if this activity is visible
		{
			if (state.isClosed())
			// if the session state is closed then the user will have to
			// reauthenticate with Facebook
			{
				finish();
			}
		}
	}


}
