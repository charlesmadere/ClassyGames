package com.charlesmadere.android.classygames.test;


import android.test.ActivityInstrumentationTestCase2;

import com.charlesmadere.android.classygames.MainActivity;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{


	public MainActivityTest()
	{
		super(MainActivity.class);
	}


	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		setActivityInitialTouchMode(false);
	}


}
