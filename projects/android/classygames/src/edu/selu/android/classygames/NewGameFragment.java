package edu.selu.android.classygames;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.facebook.widget.FriendPickerFragment;

import edu.selu.android.classygames.utilities.Utilities;


public class NewGameFragment extends SherlockListFragment
{


	private FriendPickerFragment friendPickerFragment;


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
		{
			friendPickerFragment = new FriendPickerFragment();
		}
		else
		{
			final FragmentManager fManager = getSherlockActivity().getSupportFragmentManager();
			friendPickerFragment = fManager.findFragmentById(R.id.new_game_fragment_friendpicker_fragment);
		}

		return inflater.inflate(R.layout.new_game_fragment, container, false);
	}


	@Override
	public void onStart()
	{
		super.onStart();

		try
		{
			friendPickerFragment.loadData(false);
		}
		catch (final Exception e)
		{
			onError(e);
		}
	}


	private void onError(final Exception e)
	{
		Log.e(Utilities.LOG_TAG, e.getLocalizedMessage());
	}


}
