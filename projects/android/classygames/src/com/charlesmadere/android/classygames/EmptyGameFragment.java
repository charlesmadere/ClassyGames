package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;


/**
 * This Fragment is used when there is no game currently loaded.
 */
public class EmptyGameFragment extends SherlockFragment
{


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.empty_game_fragment, container, false);
	}


}
