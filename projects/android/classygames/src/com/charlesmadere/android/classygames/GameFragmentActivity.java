package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Notification;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.settings.PreferenceActivity;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class GameFragmentActivity extends BaseActivity implements
	GamesListFragment.Listeners,
	GenericGameFragment.Listeners,
	MyStatsDialogFragment.Listeners
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - GameFragmentActivity";
	private final static String KEY_ACTION_BAR_TITLE = "KEY_ACTION_BAR_TITLE";
	public final static String KEY_NOTIFICATION = "KEY_NOTIFICATION";


	private EmptyGameFragment emptyGameFragment;
	private GamesListFragment gamesListFragment;
	private GenericGameFragment genericGameFragment;
	private MyStatsDialogFragment myStatsDialogFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	// I'm sorry that this method is so crazy. I really am. But there's a bunch
	// of different things that it does, so it's necessary. Check the comments
	// throughout the method to follow along.
	{
		super.onCreate(savedInstanceState, R.string.games_list, false);
		setContentView(R.layout.game_fragment_activity);
		setResult(RESULT_FIRST_USER);

		final FragmentManager fManager = getSupportFragmentManager();

		if (savedInstanceState == null)
		// Checks to see if the savedInstanceState object is null. If this
		// object is null then we're not rebuilding this FragmentActivity due
		// to an orientation change. This means that this FragmentActivity is
		// completely fresh and brand new.
		{
			final FragmentTransaction fTransaction = fManager.beginTransaction();

			if (isDeviceLarge())
			// Checks to see if this is a large device. If this is a large
			// device then we will load in the multi pane layout.
			{
				emptyGameFragment = new EmptyGameFragment();
				fTransaction.add(R.id.game_fragment_activity_fragment_game, emptyGameFragment);
				gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_games_list_fragment);
			}
			else
			// This is a small device. We will load in the single pane layout.
			{
				gamesListFragment = new GamesListFragment();
				fTransaction.add(R.id.game_fragment_activity_container, gamesListFragment);
			}

			fTransaction.commit();
		}
		else
		// The savedInstanceState object is not null. This means that the
		// Android device probably just went through an orientation change.
		// We're going to recover this FragmentActivity from the data stored
		// in the savedInstanceState object.
		{
			// Read in the previously stored title for the Action Bar. If the
			// title was not found, then the R.string.games_list String will be
			// loaded in and used instead.
			CharSequence actionBarTitle = savedInstanceState.getCharSequence(KEY_ACTION_BAR_TITLE);

			if (actionBarTitle == null || !Utilities.validString(actionBarTitle.toString()))
			{
				actionBarTitle = getString(R.string.games_list);
			}

			if (isDeviceLarge())
			// Checks to see if this is a large device. If this is a large
			// device then we will load in the multi pane layout.
			{
				gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_games_list_fragment);
				final Fragment fragment = fManager.findFragmentById(R.id.game_fragment_activity_fragment_game);

				if (fragment instanceof EmptyGameFragment)
				{
					emptyGameFragment = (EmptyGameFragment) fragment;
				}
				else
				{
					genericGameFragment = (GenericGameFragment) fragment;
					updateActionBar(actionBarTitle, true);
				}
			}
			else
			// This is a small device. We will load in the single pane layout.
			{
				final Fragment fragment = fManager.findFragmentById(R.id.game_fragment_activity_container);

				if (fragment instanceof GamesListFragment)
				{
					gamesListFragment = (GamesListFragment) fragment;
				}
				else
				{
					genericGameFragment = (GenericGameFragment) fragment;
					updateActionBar(actionBarTitle, true);
				}
			}
		}

		// Checks to see if the reason that this class is running is because
		// the user tapped a Classy Games notification in their notifications
		// bar.
		checkIfNotificationWasTapped();
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	// This method will be run when the NewGameFragmentActivity returns some
	// data back. That data pertains to who the current Android user wants to
	// create a game against.
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		// Check the result code as returned from NewGameFragmentActivity. If a
		// friend was selected, then this means that the current Android user
		// wants to start a new game against that selected user.
		{
			// Retrieve the data as returned from NewGameFragmentActivity. It
			// could be malformed, we'll check for that below.
			final Bundle extras = data.getExtras();

			if (extras != null && !extras.isEmpty())
			// Ensure that the returned data is not totally garbled.
			{
				final Person friend = (Person) extras.getSerializable(NewGameFragmentActivity.KEY_FRIEND);
				final byte whichGame = extras.getByte(NewGameFragmentActivity.KEY_WHICH_GAME);

				if (friend.isValid() && Game.isWhichGameValid(whichGame))
				// Ensure that we received proper data from
				// NewGameFragmentActivity.
				{
					final Game game = new Game(friend, whichGame);
					onGameSelected(game);
				}
				else
				{
					Log.e(LOG_TAG, "onActivityResult() received game data that was malformed.");
				}
			}
		}
	}


	@Override
	public void onBackPressed()
	{
		if (myStatsDialogFragment != null && myStatsDialogFragment.onBackPressed())
		{

		}
		else if (genericGameFragment != null && genericGameFragment.onBackPressed())
		{

		}
		else if (gamesListFragment != null && gamesListFragment.onBackPressed())
		{

		}
		else
		{
			super.onBackPressed();

			if (genericGameFragment == null || !genericGameFragment.isVisible())
			{
				updateActionBar(R.string.games_list, false);
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_fragment_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

//			case R.id.game_fragment_activity_menu_my_stats:
//				final FragmentManager fManager = getSupportFragmentManager();
//				final FragmentTransaction fTransaction = fManager.beginTransaction();
//				fTransaction.addToBackStack(null);
//				myStatsDialogFragment = new MyStatsDialogFragment();
//				myStatsDialogFragment.show(fTransaction, null);
//				break;

			case R.id.game_fragment_activity_menu_new_game:
				if (isDeviceLarge() && genericGameFragment != null && genericGameFragment.isVisible())
				{
					onBackPressed();
				}

				final Intent newGameIntent = new Intent(this, NewGameFragmentActivity.class);
				startActivityForResult(newGameIntent, RESULT_FIRST_USER);
				break;

			case R.id.game_fragment_activity_menu_settings:
				final Intent settingsIntent = new Intent(this, PreferenceActivity.class);
				startActivity(settingsIntent);
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		final CharSequence actionBarTitle = getActionBarTitle();
		outState.putCharSequence(KEY_ACTION_BAR_TITLE, actionBarTitle);

		super.onSaveInstanceState(outState);
	}




	/**
	 * Checks to see if the reason that the user is currently at this class
	 * (GameFragmentActivity) is because they tapped a notification in their
	 * notifications bar. If that turns out to be the reason, then this method
	 * will load in the game that the notification pertains to.
	 */
	private void checkIfNotificationWasTapped()
	{
		// Retrieve an Intent that was given to this class. It's possible that
		// no Intent was given to this class (in this case the resulting value
		// should be null). We need to do this because when someone taps a
		// Classy Games notification in their notifications bar, that action
		// triggers an Intent to be sent into this method. That Intent will
		// then include a bunch of information about the tapped notification.
		final Intent intent = getIntent();

		if (intent != null)
		{
			final Bundle arguments = intent.getExtras();

			if (arguments != null && !arguments.isEmpty())
			// Check to see if the Intent contains any arguments.
			{
				final Notification notification = (Notification) arguments.getSerializable(KEY_NOTIFICATION);

				if (notification != null)
				// Check the data gathered from the Intent. If a single piece
				// of this data is found to be invalid, then we will not act
				// upon the tapped notification.
				{
					final Game game = new Game(notification.getPerson(), notification.getWhichGame(), notification.getGameId());
					onGameSelected(game);
				}
			}
		}
	}


	/**
	 * Ensures that the gamesListFragment Fragment object is not null.
	 */
	private void getGamesListFragment()
	{
		if (gamesListFragment == null)
		{
			final FragmentManager fManager = getSupportFragmentManager();

			if (isDeviceLarge())
			{
				gamesListFragment = (GamesListFragment) fManager.findFragmentById(R.id.game_fragment_activity_fragment_games_list_fragment);
			}
			else
			{
				final Fragment fragment = fManager.findFragmentById(R.id.game_fragment_activity_container);

				if (fragment instanceof GamesListFragment)
				{
					gamesListFragment = (GamesListFragment) fragment;
				}
				else
				{
					Log.w(LOG_TAG, "getGamesListFragment() tried to grab the GamesListFragment but encountered weirdness!");
				}
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
		return findViewById(R.id.game_fragment_activity_container) == null;
	}




	@Override
	public void onGameSelected(final Game game)
	{
		if (isDeviceLarge() || (genericGameFragment == null || !genericGameFragment.isVisible()))
		// This statement will validate as true if this Android device is large
		// OR if EITHER of the following two statements are two: the
		// genericGameFragment variable is null, OR the genericGameFragment
		// variable is not visible.
		{
			if (genericGameFragment != null && genericGameFragment.isVisible())
			// If the genericGameFragment variable is both not null and is
			// visible. This means that a game is currently open and showing.
			// We want to remove that game before proceeding with the showing
			// of the newly selected game.
			{
				// Trigger a press of the back button. This will remove the
				// game that is currently showing.
				onBackPressed();
			}

			if (game.isGameCheckers())
			{
				genericGameFragment = CheckersGameFragment.newInstance(game.getId(), game.getWhichGame(), game.getPerson());
			}
			else if (game.isGameChess())
			{
				genericGameFragment = ChessGameFragment.newInstance(game.getId(), game.getWhichGame(), game.getPerson());
			}

			final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			fTransaction.addToBackStack(null);

			if (isDeviceLarge())
			{
				fTransaction.replace(R.id.game_fragment_activity_fragment_game, genericGameFragment);
			}
			else
			{
				fTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_popped, R.anim.slide_out_popped);
				fTransaction.add(R.id.game_fragment_activity_container, genericGameFragment);
			}

			fTransaction.commit();

			if (game.isGameCheckers())
			{
				updateActionBar(getString(R.string.checkers_with_x, game.getPerson().getName()), true);
			}
			else if (game.isGameChess())
			{
				updateActionBar(getString(R.string.chess_with_x, game.getPerson().getName()), true);
			}
			else
			{
				Log.e(LOG_TAG, "Player tried creating a game which was not one we recognize...");
			}
		}
	}


	@Override
	public void onRefreshSelected()
	{
		if (isDeviceLarge() && genericGameFragment != null && genericGameFragment.isVisible())
		{
			onBackPressed();
		}

		getGamesListFragment();
		gamesListFragment.refreshGamesList();
	}


	@Override
	public boolean isDeviceSmall()
	{
		return !isDeviceLarge();
	}


	@Override
	public void onGetGameCancelled()
	{
		onBackPressed();
	}


	@Override
	public void onGetGameDataError()
	{
		Toast.makeText(this, R.string.couldnt_create_the_game_as_malformed_data_was_detected, Toast.LENGTH_LONG).show();
		onBackPressed();
	}


	@Override
	public void onGetStatsDataError(final Exception e)
	{
		Log.e(LOG_TAG, "Exception in onGetStatsDataError!", e);
		Toast.makeText(this, R.string.a_server_error_occurred_when_trying_to_get_your_stats_data, Toast.LENGTH_LONG).show();
		onBackPressed();
	}


	@Override
	public void onServerApiTaskFinished()
	{
		final FragmentManager fManager = getSupportFragmentManager();
		fManager.popBackStack();
		final FragmentTransaction fTransaction = fManager.beginTransaction();

		if (isDeviceLarge())
		{
			if (emptyGameFragment == null)
			{
				emptyGameFragment = new EmptyGameFragment();
			}

			fTransaction.replace(R.id.game_fragment_activity_fragment_game, emptyGameFragment);
		}
		else
		{
			fTransaction.remove(genericGameFragment);
		}

		fTransaction.commit();
		fManager.executePendingTransactions();
		updateActionBar(R.string.games_list, false);

		onRefreshSelected();
	}


}
