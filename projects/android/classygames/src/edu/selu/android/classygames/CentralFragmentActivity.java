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
	implements GamesListFragment.GamesListFragmentOnGameSelectedListener,
		GamesListFragment.GamesListFragmentOnNewGameSelectedListener
{


	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;

	private GamesListFragment gamesListFragment;
	private GenericGameFragment genericGameFragment;
	private NewGameFragment newGameFragment;




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

		if (savedInstanceState == null)
		{
			gamesListFragment = new GamesListFragment();
			genericGameFragment = new EmptyGameFragment();

			final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

			if (isDeviceLarge())
			{
				fTransaction.add(R.id.central_fragment_activity_fragment_list, gamesListFragment);
				fTransaction.add(R.id.central_fragment_activity_fragment_game, genericGameFragment);
			}
			else
			{
				fTransaction.add(R.id.central_fragment_activity_fragment_container, gamesListFragment);
			}

			fTransaction.commit();
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


	/**
	 * Checks to see what size screen this device has. This method will return
	 * true if the device has a large screen, and as such said device should be
	 * using the dual pane, fragment based, layout stuff.
	 * 
	 * @return
	 * Returns true if this device has a large screen.
	 */
	private boolean isDeviceLarge()
	{
		return findViewById(R.id.central_fragment_activity_fragment_container) == null;
	}




	@Override
	public void gameListFragmentOnGameSelected(final Game game)
	{
		Log.d(Utilities.LOG_TAG, "onGameSelected()! " + game.getId());
	}


	@Override
	public void gamesListFragmentOnNewGameSelected()
	{
		final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

		if (newGameFragment == null)
		{
			newGameFragment = new NewGameFragment();
		}

		if (isDeviceLarge())
		{
			fTransaction.add(R.id.central_fragment_activity_fragment_list, newGameFragment);
		}
		else
		{
			fTransaction.add(R.id.central_fragment_activity_fragment_container, newGameFragment);
		}

		fTransaction.addToBackStack(null);
		fTransaction.commit();
	}


}
