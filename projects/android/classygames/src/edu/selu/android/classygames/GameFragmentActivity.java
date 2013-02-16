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


public class GameFragmentActivity extends SherlockFragmentActivity implements
	GamesListFragment.GamesListFragmentOnGameSelectedListener,
	GamesListFragment.GamesListFragmentOnRefreshSelectedListener,
	GenericGameFragment.GenericGameFragmentIsDeviceLargeListener,
	GenericGameFragment.GenericGameFragmentOnAsyncGetGameOnCancelledListener,
	GenericGameFragment.GenericGameFragmentOnDataErrorListener
{


	public final static int RESULT_CODE_FINISH = MainActivity.GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FINISH;
	public final static int NEW_GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FRIEND_SELECTED = 16;

	private final static String KEY_ACTION_BAR_TITLE = "KEY_ACTION_BAR_TITLE";




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
		setContentView(R.layout.game_fragment_activity);
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

		final FragmentManager fManager = getSupportFragmentManager();

		if (savedInstanceState == null)
		{
			final FragmentTransaction fTransaction = fManager.beginTransaction();

			if (isDeviceLarge())
			{
				emptyGameFragment = new EmptyGameFragment();
				fTransaction.add(R.id.game_fragment_activity_fragment_game, emptyGameFragment);

				gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_games_list_fragment);
			}
			else
			{
				gamesListFragment = new GamesListFragment();
				fTransaction.add(R.id.game_fragment_activity_container, gamesListFragment);
			}

			fTransaction.commit();
		}
		else
		{
			if (isDeviceLarge())
			{
				try
				{
					emptyGameFragment = (EmptyGameFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_game);
				}
				catch (final ClassCastException e)
				{
					genericGameFragment = (GenericGameFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_game);

					final ActionBar actionBar = getSupportActionBar();
					actionBar.setDisplayHomeAsUpEnabled(true);
					actionBar.setTitle(savedInstanceState.getCharSequence(KEY_ACTION_BAR_TITLE));
				}
			}
			else
			{
				try
				{
					gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.game_fragment_activity_container);
				}
				catch (final ClassCastException e)
				{
					genericGameFragment = (GenericGameFragment) fManager.findFragmentById(R.id.game_fragment_activity_container);

					final ActionBar actionBar = getSupportActionBar();
					actionBar.setDisplayHomeAsUpEnabled(true);
					actionBar.setTitle(savedInstanceState.getCharSequence(KEY_ACTION_BAR_TITLE));
				}
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
	public void onBackPressed()
	{
		if (gamesListFragment != null && gamesListFragment.getIsAsyncRefreshGamesListRunning())
		{
			gamesListFragment.cancelAsyncRefreshGamesList();
		}
		else
		{
			final ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setTitle(R.string.games_list_fragment_title);

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
		isResumed = false;
		uiHelper.onDestroy();
		super.onDestroy();
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
		isResumed = false;
		uiHelper.onPause();
		super.onPause();
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
		final CharSequence actionBarTitle = getSupportActionBar().getTitle();
		outState.putCharSequence(KEY_ACTION_BAR_TITLE, actionBarTitle);

		uiHelper.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
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
		return findViewById(R.id.game_fragment_activity_container) == null;
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
		if (isDeviceLarge() || (genericGameFragment == null || !genericGameFragment.isVisible()))
		{
			if (genericGameFragment != null && genericGameFragment.isVisible())
			{
				onBackPressed();
			}

			final Bundle arguments = new Bundle();
			arguments.putString(GenericGameFragment.KEY_GAME_ID, game.getId());
			arguments.putLong(GenericGameFragment.KEY_PERSON_ID, game.getPerson().getId());
			arguments.putString(GenericGameFragment.KEY_PERSON_NAME, game.getPerson().getName());

			// if a future release of Classy Games has chess as well as checkers,
			// then we will need to do some logic here to check the game type and
			// then instantiate that game's fragment
			genericGameFragment = new CheckersGameFragment();
			genericGameFragment.setArguments(arguments);

			final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			fTransaction.addToBackStack(null);

			if (isDeviceLarge())
			{
				fTransaction.replace(R.id.game_fragment_activity_fragment_game, genericGameFragment);
			}
			else
			{
				fTransaction.add(R.id.game_fragment_activity_container, genericGameFragment);
			}

			fTransaction.commit();

			final ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(getString(R.string.checkers_game_fragment_title) + " " + game.getPerson().getName());
		}
	}


	@Override
	public void gamesListFragmentOnRefreshSelected()
	{
		if (isDeviceLarge() && genericGameFragment != null && genericGameFragment.isVisible())
		{
			onBackPressed();
		}

		gamesListFragment.refreshGamesList();
	}


	@Override
	public boolean genericGameFragmentIsDeviceSmall()
	{
		return !isDeviceLarge();
	}


	@Override
	public void genericGameFragmentOnAsyncGetGameOnCancelled()
	{
		onBackPressed();
	}


	@Override
	public void genericGameFragmentOnDataError()
	{
		Utilities.easyToastAndLogError(this, "Couldn't create a game as malformed data was detected!");
		onBackPressed();
	}


	


}
