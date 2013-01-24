package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import edu.selu.android.classygames.data.Game;
import edu.selu.android.classygames.utilities.Utilities;


public class CentralFragmentActivity extends SherlockFragmentActivity
	implements GamesListFragment.GamesListFragmentOnGameSelectedListener,
		GamesListFragment.GamesListFragmentOnNewGameSelectedListener,
		NewGameFragment.NewGameFragmentOnDestroyViewListener
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
				fTransaction.add(R.id.central_fragment_activity_container, gamesListFragment);
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
		return findViewById(R.id.central_fragment_activity_container) == null;
	}


	/**
	 * Transitions either a part of the device's screen or a part of the
	 * device's screen to the given fragment. A large device will have only
	 * part of the screen occupied by the given fragment while a smaller device
	 * (a phone most likely) will have the entire screen occupied.
	 * 
	 * @param fragment
	 * 
	 * 
	 * @param largeLayout
	 * In the case that this is a large device, what portion of the screen do
	 * you want the given fragment to occupy? Use
	 * central_fragment_activity_fragment_list for the smaller, left side of
	 * screen or central_fragment_activity_fragment_game for the bigger, right
	 * side of the screen.
	 */
	private void transitionToFragment(final Fragment fragment, final int largeLayout)
	{
		final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();

		if (isDeviceLarge())
		{
			fTransaction.add(largeLayout, fragment);
		}
		else
		{
			fTransaction.add(R.id.central_fragment_activity_container, fragment);
		}

		fTransaction.addToBackStack(null);
		fTransaction.commit();
	}




	@Override
	public void gameListFragmentOnGameSelected(final Game game)
	{
		Utilities.easyToastAndLog(this, game.getId() + " vs " + game.getPerson().getName());

		// if a future release of Classy Games has chess as well as
		// checkers, then we will need to do some logic here to check the
		// game type and then instantiate that game's fragment
		genericGameFragment = new CheckersGameFragment(game);

		// Build up a set of arguments to send to the fragment as it's
		// instantiated. These arguments tell the GenericGameFragment the
		// Game's ID, the player ID of the challenger, and the player name of
		// the challenger.
		final Bundle arguments = new Bundle();
		arguments.putString(GenericGameFragment.BUNDLE_DATA_GAME_ID, game.getId());
		arguments.putLong(GenericGameFragment.BUNDLE_DATA_PERSON_CHALLENGED_ID, game.getPerson().getId());
		arguments.putString(GenericGameFragment.BUNDLE_DATA_PERSON_CHALLENGED_NAME, game.getPerson().getName());
		genericGameFragment.setArguments(arguments);

		transitionToFragment(genericGameFragment, R.id.central_fragment_activity_fragment_game);
	}


	@Override
	public void gamesListFragmentOnNewGameSelected()
	{
		if (newGameFragment == null)
		{
			newGameFragment = new NewGameFragment();
		}

		transitionToFragment(newGameFragment, R.id.central_fragment_activity_fragment_list);
	}


	@Override
	public void newGameFragmentOnDestroyViewListener()
	// This listener does some weird work around stuff. Check out my
	// explanation of what exactly this does in the NewGameFragment class.
	{
		if (gamesListFragment.isVisible())
		{
			getSupportActionBar().setTitle(R.string.games_list_fragment_title);
		}
	}


}
