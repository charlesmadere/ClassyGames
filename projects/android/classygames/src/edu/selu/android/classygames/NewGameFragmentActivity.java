package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.selu.android.classygames.utilities.Utilities;


public class NewGameFragmentActivity extends SherlockFragmentActivity
{


	public final static int RESULT_CODE_DEFAULT = 0;
	public final static int RESULT_CODE_FRIEND_SELECTED = CentralFragmentActivity.NEW_GAME_FRAGMENT_ACTIVITY_REQUEST_CODE;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game_fragment_activity);
		setResult(RESULT_CODE_DEFAULT);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);
	}


}
