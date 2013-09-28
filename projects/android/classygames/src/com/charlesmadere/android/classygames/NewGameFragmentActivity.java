package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Game;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class NewGameFragmentActivity extends SherlockFragmentActivity implements
	ConfirmGameFragment.Listeners,
	FriendsListFragment.Listeners
{


	public final static int RESULT_FRIEND_SELECTED = 2;

	public final static String KEY_FRIEND = "KEY_FRIEND";
	public final static String KEY_WHICH_GAME = "KEY_WHICH_GAME";

	private ConfirmGameFragment confirmGameFragment;
	private EmptyConfirmGameFragment emptyConfirmGameFragment;
	private FriendsListFragment friendsListFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_fragment_activity);
		setResult(RESULT_OK);
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

				try
				{
					emptyConfirmGameFragment = (EmptyConfirmGameFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_confirm_game);
				}
				catch (final ClassCastException e)
				{
					confirmGameFragment = (ConfirmGameFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_confirm_game);
				}

				friendsListFragment = (FriendsListFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_friends_list_fragment);
			}
			else
			{
				try
				{
					friendsListFragment = (FriendsListFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_container);
				}
				catch (final ClassCastException e)
				{
					confirmGameFragment = (ConfirmGameFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_container);
				}
			}
		}
	}


	@Override
	public void onBackPressed()
	{
		if (friendsListFragment != null && friendsListFragment.isAnAsyncTaskRunning())
		{
			friendsListFragment.cancelRunningAnyAsyncTask();
		}
		else
		{
			if (friendsListFragment != null && !friendsListFragment.wasCancelled())
			{
				friendsListFragment.refreshListDrawState();
			}

			super.onBackPressed();
		}
	}


	@Override
	protected void onDestroy()
	{
		if (friendsListFragment != null)
		{
			friendsListFragment.cancelRunningAnyAsyncTask();
		}

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
	public void onDataError()
	{
		Utilities.easyToastAndLogError(this, getString(R.string.error_when_trying_to_store_the_data_for_the_friend_that_you_selected));
		onBackPressed();
	}


	@Override
	public void onGameConfirm(final Person friend, final byte whichGame)
	{
		final Bundle extras = new Bundle();
		extras.putSerializable(KEY_FRIEND, friend);
		extras.putByte(KEY_WHICH_GAME, whichGame);

		if (Game.isWhichGameValid(whichGame))
		{
			final Intent intent = new Intent();
			intent.putExtras(extras);
			setResult(RESULT_FRIEND_SELECTED, intent);
		}
		else
		{
			Utilities.easyToastAndLogError(this, R.string.couldnt_create_the_game_as_malformed_data_was_detected);
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

		friendsListFragment.refreshFriendsList();
	}


}
