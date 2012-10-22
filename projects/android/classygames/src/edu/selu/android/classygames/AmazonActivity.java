package edu.selu.android.classygames;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.games.Person;


public class AmazonActivity extends SherlockActivity
{


	private Person person;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amazon_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		final Bundle bundle = getIntent().getExtras();

		if (bundle == null)
		{
			activityHasError();
		}
		else
		{
			final long id = bundle.getLong(CheckersGameActivity.INTENT_DATA_PERSON_ID);
			final String name = bundle.getString(CheckersGameActivity.INTENT_DATA_PERSON_NAME);

			if (id <= 0  || name == null)
			{
				activityHasError();
			}
			else
			{
				person = new Person(id, name);
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.amazon_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			case R.id.amazon_activity_actionbar_send_data:
				new AddPersonData().execute(person);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private void activityHasError()
	{
		Utilities.easyToastAndLogError(AmazonActivity.this, "herror herror herror");
		finish();
	}


	private final class AddPersonData extends AsyncTask<Person, Void, Void>
	{


		private ProgressDialog progressDialog;


		@Override
		protected Void doInBackground(final Person... people)
		{
			

			return null;
		}


		@Override
		protected void onPostExecute(final Void result)
		{
			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(AmazonActivity.this);
			progressDialog.setMessage("messagegeegegee");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setTitle(R.string.new_game_activity_progressdialog_title);

			progressDialog.show();
		}


	}


}
