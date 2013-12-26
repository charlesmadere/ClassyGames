package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment
{


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(getConventView(), container, false);
	}


	/**
	 * @return
	 * Returns the R.layout.* int ID for the view you want to inflate.
	 */
	protected abstract int getConventView();


}
