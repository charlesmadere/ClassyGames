package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class NewGameFragmentActivity extends SherlockFragmentActivity implements
	ConfirmGameFragment.Listeners,
	FriendsListFragment.Listeners
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - NewGameFragmentActivity";
	public final static String KEY_FRIEND = "KEY_FRIEND";
	public final static String KEY_WHICH_GAME = "KEY_WHICH_GAME";

	private ConfirmGameFragment confirmGameFragment;
	private EmptyConfirmGameFragment emptyConfirmGameFragment;
	private FriendsListFragment friendsListFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	// To follow along with what's going on in this crazy method, please check
	// the onCreate() that's in the GameFragmentActivity class. It's better
	// documented in there!
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_fragment_activity);
		Utilities.setActionBar(this, R.string.friends_list, true);

		final FragmentManager fManager = getSupportFragmentManager();

		if (savedInstanceState == null)
		{
			final FragmentTransaction fTransaction = fManager.beginTransaction();

			if (isDeviceLarge())
			{
				emptyConfirmGameFragment = new EmptyConfirmGameFragment();
				fTransaction.add(R.id.new_game_fragment_activity_fragment_confirm_game, emptyConfirmGameFragment);
				friendsListFragment = (FriendsListFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_friends_list_fragment);
			}
			else
			{
				friendsListFragment = new FriendsListFragment();
				fTransaction.add(R.id.new_game_fragment_activity_container, friendsListFragment);
			}

			fTransaction.commit();
		}
		else
		{
			if (isDeviceLarge())
			{
				friendsListFragment = (FriendsListFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_friends_list_fragment);
				final Fragment fragment = fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_confirm_game);

				if (fragment instanceof EmptyConfirmGameFragment)
				{
					emptyConfirmGameFragment = (EmptyConfirmGameFragment) fragment;
				}
				else
				{
					confirmGameFragment = (ConfirmGameFragment) fragment;
				}
			}
			else
			{
				final Fragment fragment = fManager.findFragmentById(R.id.new_game_fragment_activity_container);

				if (fragment instanceof FriendsListFragment)
				{
					friendsListFragment = (FriendsListFragment) fragment;
				}
				else
				{
					confirmGameFragment = (ConfirmGameFragment) fragment;
				}
			}
		}
	}


	@Override
	public void onBackPressed()
	{
		if (friendsListFragment != null && !friendsListFragment.onBackPressed())
		{
			super.onBackPressed();
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
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
		return findViewById(R.id.new_game_fragment_activity_container) == null;
	}




	@Override
	public boolean isDeviceSmall()
	{
		return !isDeviceLarge();
	}


	@Override
	public void onGameConfirm(final Person friend, final byte whichGame)
	{
		if (friend.isValid() && Game.isWhichGameValid(whichGame))
		{
			final Intent intent = new Intent()
				.putExtra(KEY_FRIEND, friend)
				.putExtra(KEY_WHICH_GAME, whichGame);

			setResult(RESULT_OK, intent);
		}
		else
		{
			Log.e(LOG_TAG, "Received malformed onGameConfirm data: Person name: \"" + friend.getName() +
				"\" Person id: " + friend.getId() + " whichGame: " + whichGame);

			Toast.makeText(this, R.string.couldnt_create_the_game_as_malformed_data_was_detected, Toast.LENGTH_LONG).show();
		}

		finish();
	}


	@Override
	public void onGameDeny()
	{
		onBackPressed();
	}


	@Override
	public void onFriendSelected(final Person friend)
	{
		if (isDeviceLarge() || (confirmGameFragment == null || !confirmGameFragment.isVisible()))
		{
			if (confirmGameFragment != null && confirmGameFragment.isVisible())
			{
				onBackPressed();
			}

			confirmGameFragment = ConfirmGameFragment.newInstance(friend);
			final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			fTransaction.addToBackStack(null);

			if (isDeviceLarge())
			{
				fTransaction.replace(R.id.new_game_fragment_activity_fragment_confirm_game, confirmGameFragment);
			}
			else
			{
				fTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_popped, R.anim.slide_out_popped);
				fTransaction.add(R.id.new_game_fragment_activity_container, confirmGameFragment);
			}

			fTransaction.commit();
		}
	}


	@Override
	public void onRefreshSelected()
	{
		if (isDeviceLarge() && confirmGameFragment != null && confirmGameFragment.isVisible())
		{
			onBackPressed();
		}
	}


}
