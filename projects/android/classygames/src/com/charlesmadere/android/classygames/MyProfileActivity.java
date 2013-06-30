package com.charlesmadere.android.classygames;


import android.os.Bundle;
import com.actionbarsherlock.app.SherlockActivity;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class MyProfileActivity extends SherlockActivity
{


	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_activity);
		Utilities.setActionBar(this, R.string.my_profile, true);
	}


}
