package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.utilities.Utilities;


public class SharkActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shark_fragment);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


}