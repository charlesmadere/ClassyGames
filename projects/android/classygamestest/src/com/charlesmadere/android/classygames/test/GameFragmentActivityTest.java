package com.charlesmadere.android.classygames.test;


import android.test.ActivityInstrumentationTestCase2;

import com.charlesmadere.android.classygames.GameFragmentActivity;


public class GameFragmentActivityTest extends ActivityInstrumentationTestCase2<GameFragmentActivity>
{


	private GameFragmentActivity activity;




	public GameFragmentActivityTest()
	{
		super(GameFragmentActivity.class);
	}


	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		setActivityInitialTouchMode(false);
		activity = getActivity();
	}


}
