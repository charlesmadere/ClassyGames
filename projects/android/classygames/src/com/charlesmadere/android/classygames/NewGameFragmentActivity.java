package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class NewGameFragmentActivity extends SherlockFragmentActivity implements
	ConfirmGameFragment.ConfirmGameFragmentIsDeviceSmallListener,
	ConfirmGameFragment.ConfirmGameFragmentOnDataErrorListener,
	ConfirmGameFragment.ConfirmGameFragmentOnGameConfirmListener,
	ConfirmGameFragment.ConfirmGameFragmentOnGameDenyListener,
	FriendsListFragment.FriendsListFragmentOnFriendSelectedListener,
	FriendsListFragment.FriendsListFragmentOnRefreshSelectedListener
{


	public final static int RESULT_CODE_DEFAULT = 0;
	public final static int RESULT_CODE_FRIEND_SELECTED = GameFragmentActivity.NEW_GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FRIEND_SELECTED;

	public final static String KEY_FRIEND_ID = "KEY_FRIEND_ID";
	public final static String KEY_FRIEND_NAME = "KEY_FRIEND_NAME";


	ConfirmGameFragment confirmGameFragment;
	EmptyConfirmGameFragment emptyConfirmGameFragment;
	FriendsListFragment friendsListFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_fragment_activity);
		setResult(RESULT_CODE_DEFAULT);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

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
		if (friendsListFragment != null && friendsListFragment.isAsyncRefreshFriendsListRunning())
		{
			friendsListFragment.cancelAsyncRefreshFriendsList();
		}
		else
		{
			super.onBackPressed();
		}
	}


	@Override
	protected void onDestroy()
	{
		if (friendsListFragment != null)
		{
			friendsListFragment.cancelAsyncRefreshFriendsList();
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
	public boolean confirmGameFragmentIsDeviceSmall()
	{
		return !isDeviceLarge();
	}


	@Override
	public void confirmGameFragmentOnDataError()
	{
		Utilities.easyToastAndLogError(this, getString(R.string.confirm_game_fragment_data_error));
		onBackPressed();
	}


	@Override
	public void confirmGameFragmentOnGameConfirm(final Person friend)
	{
		final Bundle extras = new Bundle();
		extras.putLong(KEY_FRIEND_ID, friend.getId());
		extras.putString(KEY_FRIEND_NAME, friend.getName());

		final Intent intent = new Intent();
		intent.putExtras(extras);

		setResult(RESULT_CODE_FRIEND_SELECTED, intent);
		finish();
	}


	@Override
	public void confirmGameFragmentOnGameDeny()
	{
		onBackPressed();
	}


	@Override
	public void friendsListFragmentOnFriendSelected(final Person friend)
	{
		if (isDeviceLarge() || (confirmGameFragment == null || !confirmGameFragment.isVisible()))
		{
			if (confirmGameFragment != null && confirmGameFragment.isVisible())
			{
				onBackPressed();
			}

			final Bundle arguments = new Bundle();
			arguments.putLong(ConfirmGameFragment.KEY_FRIEND_ID, friend.getId());
			arguments.putString(ConfirmGameFragment.KEY_FRIEND_NAME, friend.getName());

			confirmGameFragment = new ConfirmGameFragment();
			confirmGameFragment.setArguments(arguments);

			final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
			fTransaction.addToBackStack(null);

			if (isDeviceLarge())
			{
				fTransaction.replace(R.id.new_game_fragment_activity_fragment_confirm_game, confirmGameFragment);
			}
			else
			{
				fTransaction.add(R.id.new_game_fragment_activity_container, confirmGameFragment);
			}

			fTransaction.commit();
		}
	}


	@Override
	public void friendsListFragmentOnRefreshSelected()
	{
		if (isDeviceLarge() && confirmGameFragment != null && confirmGameFragment.isVisible())
		{
			onBackPressed();
		}

		friendsListFragment.refreshFriendsList();
	}


}
