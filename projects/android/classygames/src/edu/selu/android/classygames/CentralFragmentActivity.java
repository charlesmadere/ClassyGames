package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.utilities.Utilities;


public class CentralFragmentActivity extends SherlockFragmentActivity
	implements GamesListFragment.OnGameSelectedListener,
		GamesListFragment.OnNewGameSelectedListener,
		GenericGameFragment.OnGameSentListener
{


	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.central_fragment_activity);
		setResult(MainActivity.CENTRAL_FRAGMENT_ACTIVITY_RESULT_CODE);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);

		sessionStatusCallback = new Session.StatusCallback()
		{
			@Override
			public void call(final Session session, final SessionState state, final Exception exception)
			{
				onSessionStateChange(session, state, exception);
			}
		};

		uiHelper = new UiLifecycleHelper(CentralFragmentActivity.this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);

		if (findViewById(R.id.central_fragment_activity_fragment_container) != null)
		{
			if (savedInstanceState == null)
			{
				final GamesListFragment gamesListFragment = new GamesListFragment();

				final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
				fTransaction.add(R.id.central_fragment_activity_fragment_container, gamesListFragment);
				fTransaction.commit();
			}
		}
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
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
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
			if (!state.equals(SessionState.OPENED))
			// if the session state is not opened then the user will have to
			// reauthenticate with Facebook
			{
				finish();
			}
		}
	}




	@Override
	public void onGameSelected(final Game game)
	{
		Log.d(Utilities.LOG_TAG, "onGameSelected()! " + game.getId());
	}


	@Override
	public void onGameSent()
	{
		Log.d(Utilities.LOG_TAG, "onGameSent()!");
	}


	@Override
	public void onNewGameSelected()
	{
		final NewGameFragment newGameFragment = new NewGameFragment();
		final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

		if (findViewById(R.id.central_fragment_activity_fragment_container) == null)
		{
			fTransaction.replace(R.id.central_fragment_activity_fragment_list, newGameFragment);
		}
		else
		{
			fTransaction.replace(R.id.central_fragment_activity_fragment_container, newGameFragment);
		}

		fTransaction.addToBackStack(null);
		fTransaction.commit();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}


}
