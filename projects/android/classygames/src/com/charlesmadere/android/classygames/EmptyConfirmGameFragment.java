package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;


/**
 * This Fragment is used when there is no friend in the Friends List currently
 * selected.
 */
public final class EmptyConfirmGameFragment extends Fragment
{


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.empty_confirm_game_fragment, container, false);
	}


}
