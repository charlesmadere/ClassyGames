package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import edu.selu.android.classygames.models.Game;
import edu.selu.android.classygames.utilities.Utilities;


public class CentralFragmentActivity extends SherlockFragmentActivity implements
	GamesListFragment.GamesListFragmentOnGameSelectedListener,
	GamesListFragment.GamesListFragmentOnResumeListener,
	GenericGameFragment.GenericGameFragmentIsDeviceLargeListener,
	GenericGameFragment.GenericGameFragmentOnAsyncGetGameOnCancelledListener,
	GenericGameFragment.GenericGameFragmentOnDataErrorListener,
	GenericGameFragment.GenericGameFragmentOnResumeListener
{


	public final static int RESULT_CODE_FINISH = MainActivity.CENTRAL_FRAGMENT_ACTIVITY_REQUEST_CODE_FINISH;
	public final static int NEW_GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FRIEND_SELECTED = 16;




	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;

	private EmptyGameFragment emptyGameFragment;
	private GamesListFragment gamesListFragment;
	private GenericGameFragment genericGameFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.central_fragment_activity);
		setResult(RESULT_CODE_FINISH);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), false);

		sessionStatusCallback = new Session.StatusCallback()
		{
			@Override
			public void call(final Session session, final SessionState state, final Exception exception)
			{
				onSessionStateChange(session, state, exception);
			}
		};

		uiHelper = new UiLifecycleHelper(this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);

		if (savedInstanceState == null)
		{
			emptyGameFragment = new EmptyGameFragment();

			final FragmentManager fManager = getSupportFragmentManager();
			final FragmentTransaction fTransaction = fManager.beginTransaction();

			if (isDeviceLarge())
			{
				gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.central_fragment_activity_fragment_games_list_fragment);
				fTransaction.add(R.id.central_fragment_activity_fragment_game, emptyGameFragment);
			}
			else
			{
				gamesListFragment = new GamesListFragment();
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
	public void onBackPressed()
	{
		if (gamesListFragment != null && gamesListFragment.getIsAsyncRefreshGamesListRunning())
		{
			gamesListFragment.cancelAsyncRefreshGamesList();
		}
		else if (genericGameFragment != null && genericGameFragment.isVisible())
		{
			if (genericGameFragment.isAnAsyncTaskRunning())
			{
				genericGameFragment.cancelRunningAnyAsyncTask();
			}
			else
			{
				super.onBackPressed();
			}

			if (isDeviceLarge())
			{
				gamesListFragmentOnResume();
			}
		}
		else
		{
			super.onBackPressed();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		final MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.central_fragment_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			case R.id.central_fragment_activity_menu_about:
				startActivity(new Intent(this, AboutActivity.class));
				break;

			case R.id.central_fragment_activity_menu_new_game:
				if (isDeviceLarge() && genericGameFragment != null && genericGameFragment.isVisible())
				{
					onBackPressed();
				}

				startActivityForResult(new Intent(this, NewGameFragmentActivity.class), NEW_GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FRIEND_SELECTED);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
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
	 * Attemps to remove an instance of GenericGameFragment from the screen.
	 * 
	 * @return
	 * Returns true if an instance of GenericGameFragment was displaying on the
	 * screen and was then removed.
	 */
	private boolean destroyGenericGameFragment()
	{
		if (genericGameFragment == null || !genericGameFragment.isVisible())
		{
			return true;
		}
		else
		{
			final FragmentManager fManager = getSupportFragmentManager();
			fManager.popBackStack();

			final FragmentTransaction fTransaction = fManager.beginTransaction();

			if (isDeviceLarge())
			{
				emptyGameFragment = new EmptyGameFragment();
				fTransaction.replace(R.id.central_fragment_activity_fragment_game, emptyGameFragment);
			}
			else
			{
				fTransaction.replace(R.id.central_fragment_activity_container, gamesListFragment);
			}

			fTransaction.commit();

			final ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setTitle(R.string.games_list_fragment_title);

			return false;
		}
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
	public void gameListFragmentOnGameSelected(final Game game)
	{
		destroyGenericGameFragment();

		// if a future release of Classy Games has chess as well as checkers,
		// then we will need to do some logic here to check the game type and
		// then instantiate that game's fragment
		genericGameFragment = new CheckersGameFragment();

		final Bundle arguments = new Bundle();
		arguments.putString(GenericGameFragment.KEY_GAME_ID, game.getId());
		arguments.putLong(GenericGameFragment.KEY_PERSON_ID, game.getPerson().getId());
		arguments.putString(GenericGameFragment.KEY_PERSON_NAME, game.getPerson().getName());
		genericGameFragment.setArguments(arguments);

		final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
		fTransaction.addToBackStack(null);

		if (isDeviceLarge())
		{
			fTransaction.replace(R.id.central_fragment_activity_fragment_game, genericGameFragment);
		}
		else
		{
			fTransaction.replace(R.id.central_fragment_activity_container, genericGameFragment);
		}

		fTransaction.commit();
	}


	@Override
	public void gamesListFragmentOnResume()
	{
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setTitle(R.string.games_list_fragment_title);
	}


	@Override
	public boolean genericGameFragmentIsDeviceSmall()
	{
		return !isDeviceLarge();
	}


	@Override
	public void genericGameFragmentOnAsyncGetGameOnCancelled()
	{
		destroyGenericGameFragment();
	}


	@Override
	public void genericGameFragmentOnDataError()
	{
		destroyGenericGameFragment();
		Utilities.easyToastAndLogError(this, "Couldn't create a game as malformed data was detected!");
	}


	@Override
	public void genericGameFragmentOnResume()
	{
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(genericGameFragment.getActionBarTitle());
	}


}
