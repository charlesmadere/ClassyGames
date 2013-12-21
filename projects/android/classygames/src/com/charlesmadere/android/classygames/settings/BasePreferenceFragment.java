package com.charlesmadere.android.classygames.settings;


import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;
import com.charlesmadere.android.classygames.R;


public abstract class BasePreferenceFragment extends PreferenceFragment
{


	private PreferenceFragmentListeners listeners;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		listeners.updateActionBarTitle(getActionBarTitle());
		addPreferencesFromResource(getPreferencesResources());
	}


	@Override
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final View view = getView();
		view.setBackgroundResource(R.drawable.bg_bright);

		final ListView listView = (ListView) view.findViewById(android.R.id.list);
		listView.setSelector(R.drawable.selectable_item);
	}


	@Override
	public void onAttach(final Activity activity)
	{
		super.onAttach(activity);
		listeners = (PreferenceFragmentListeners) activity;
	}


	protected abstract CharSequence getActionBarTitle();


	protected abstract int getPreferencesResources();


}
