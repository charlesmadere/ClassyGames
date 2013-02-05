package edu.selu.android.classygames;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class NewGameFragmentActivity extends SherlockFragmentActivity implements
	ConfirmGameFragment.ConfirmGameFragmentOnDataErrorListener,
	ConfirmGameFragment.ConfirmGameFragmentOnGameConfirmListener,
	ConfirmGameFragment.ConfirmGameFragmentOnGameDenyListener,
	FriendsListFragment.FriendsListFragmentOnFriendSelectedListener,
	FriendsListFragment.FriendsListFragmentOnRefreshSelectedListener
{


	public final static int RESULT_CODE_DEFAULT = 0;
	public final static int RESULT_CODE_FRIEND_SELECTED = CentralFragmentActivity.NEW_GAME_FRAGMENT_ACTIVITY_FRIEND_SELECTED;


	ConfirmGameFragment confirmGameFragment;
	FriendsListFragment friendsListFragment;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_fragment_activity);
		setResult(RESULT_CODE_DEFAULT);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		if (savedInstanceState == null)
		{
			final FragmentManager fManager = getSupportFragmentManager();

			if (isDeviceLarge())
			{
				confirmGameFragment = (ConfirmGameFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_confirm_game_fragment);
				friendsListFragment = (FriendsListFragment) fManager.findFragmentById(R.id.new_game_fragment_activity_fragment_friends_list_fragment);
			}
			else
			{
				friendsListFragment = new FriendsListFragment();
				final FragmentTransaction fTransaction = fManager.beginTransaction();
				fTransaction.add(R.id.new_game_fragment_activity_container, friendsListFragment);
				fTransaction.commit();
			}
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				if (confirmGameFragment == null || !confirmGameFragment.isVisible() || !confirmGameFragment.isLoaded())
				{
					finish();
				}
				else
				{
					final FragmentManager fManager = getSupportFragmentManager();
					fManager.popBackStack();

					final FragmentTransaction fTransaction = fManager.beginTransaction();

					if (isDeviceLarge())
					{
						confirmGameFragment = new ConfirmGameFragment();
						fTransaction.replace(R.id.new_game_fragment_activity_fragment_confirm_game_fragment, confirmGameFragment);
					}
					else
					{
						fTransaction.replace(R.id.new_game_fragment_activity_container, friendsListFragment);
					}

					fTransaction.commit();
				}
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


	private void removeOrClearConfirmGameFragment()
	{
		if (confirmGameFragment != null && confirmGameFragment.isVisible())
		{
			final FragmentManager fManager = getSupportFragmentManager();
			fManager.popBackStack();

			if (isDeviceLarge())
			{
				confirmGameFragment = new ConfirmGameFragment();
			}
			else
			{
				final FragmentTransaction fTransaction = fManager.beginTransaction();
				fTransaction.remove(confirmGameFragment);
			}
		}
	}




	@Override
	public void confirmGameFragmentOnDataError()
	{
		removeOrClearConfirmGameFragment();
		Utilities.easyToastAndLogError(this, getString(R.string.confirm_game_fragment_data_error));
	}


	@Override
	public void confirmGameFragmentOnGameConfirm(final Person friend)
	{
		Log.d(Utilities.LOG_TAG, friend.toString());
	}


	@Override
	public void confirmGameFragmentOnGameDeny()
	{
		removeOrClearConfirmGameFragment();
	}


	@Override
	public void friendsListFragmentOnFriendSelected(final Person friend)
	{
		final Bundle arguments = new Bundle();
		arguments.putLong(ConfirmGameFragment.KEY_FRIEND_ID, friend.getId());
		arguments.putString(ConfirmGameFragment.KEY_FRIEND_NAME, friend.getName());

		confirmGameFragment = new ConfirmGameFragment();
		confirmGameFragment.setArguments(arguments);

		final FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
		fTransaction.addToBackStack(null);

		if (isDeviceLarge())
		{
			fTransaction.replace(R.id.new_game_fragment_activity_fragment_confirm_game_fragment, confirmGameFragment);
		}
		else
		{
			fTransaction.replace(R.id.new_game_fragment_activity_container, confirmGameFragment);
		}

		fTransaction.commit();
	}


	@Override
	public void friendsListFragmentOnRefreshSelected()
	{
		if (isDeviceLarge())
		{
			confirmGameFragment = new ConfirmGameFragment();

			final FragmentManager fManager = getSupportFragmentManager();
			fManager.popBackStack();

			final FragmentTransaction fTransaction = fManager.beginTransaction();
			fTransaction.replace(R.id.new_game_fragment_activity_fragment_confirm_game_fragment, confirmGameFragment);
			fTransaction.commit();
		}
	}


}