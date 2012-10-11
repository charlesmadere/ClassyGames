package edu.selu.android.classygames;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class ConfirmGameActivity extends SherlockActivity
{


	public final static String INTENT_DATA_PERSON_ID = "id";
	public final static String INTENT_DATA_PERSON_NAME = "name";


	private long id;
	private String name;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		final Bundle bundle = getIntent().getExtras();

		if (bundle == null)
		// bundle should NOT equal null
		{
			activityHasError();
		}
		else
		{
			final long id = bundle.getLong(INTENT_DATA_PERSON_ID);
			final String name = bundle.getString(INTENT_DATA_PERSON_NAME);

			if (id == 0 || name == null)
			{
				activityHasError();
			}
			else
			{
				this.id = id;
				this.name = name;
			}
		}

		Utilities.easyToast(ConfirmGameActivity.this, id + " " + name);
	}


	private void activityHasError()
	// something went wrong when the NewGameActivity went to pass this activity some data
	{
		finish();
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
