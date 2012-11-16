package edu.selu.android.classygames;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class ConfirmGameActivity extends SherlockActivity
{


	private Person personCreator;
	private Person personChallenged;


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
			final long creatorId = bundle.getLong(CheckersGameActivity.INTENT_DATA_PERSON_CREATOR_ID);
			final String creatorName = bundle.getString(CheckersGameActivity.INTENT_DATA_PERSON_CREATOR_NAME);
			final long challengedId = bundle.getLong(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = bundle.getString(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (creatorId < 0  || creatorName == null || creatorName.equals("") || challengedId < 0 || challengedName == null || challengedName.equals(""))
			{
				activityHasError();
			}
			else
			{
				personCreator = new Person(creatorId, creatorName);
				personChallenged = new Person(challengedId, challengedName);

				ImageView personPicture = (ImageView) findViewById(R.id.confirm_game_activity_person_picture);
				personPicture.setImageResource(R.drawable.fb_placeholder);
				new AsyncPopulatePictures(personPicture).execute(personChallenged);
				
				//UrlImageViewHelper.setUrlDrawable(personPicture, Utilities.FACEBOOK_GRAPH_API_URL + personChallenged.getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL);

				TextView personName = (TextView) findViewById(R.id.confirm_game_activity_person_name);
				personName.setText(personChallenged.getName());
				personName.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_SNELL_ROUNDHAND_BLKSCR));

				Button gameAccept = (Button) findViewById(R.id.confirm_game_activity_button_accept);
				gameAccept.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
				gameAccept.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Intent intent = new Intent(ConfirmGameActivity.this, CheckersGameActivity.class);
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CREATOR_ID, personCreator.getId());
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CREATOR_NAME, personCreator.getName());
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_ID, personChallenged.getId());
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_CHALLENGED_NAME, personChallenged.getName());

						// start the ConfirmGameActivity with a bit of extra data. We're passing it both
						// the id and the name of the facebook person that the user has decided to challenge
						startActivity(intent);
					}
				});

				Button gameDeny = (Button) findViewById(R.id.confirm_game_activity_button_deny);
				gameDeny.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
				gameDeny.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						finish();
					}
				});
			}
		}
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


	private void activityHasError()
	{
		Utilities.easyToastAndLogError(ConfirmGameActivity.this, ConfirmGameActivity.this.getString(R.string.confirm_game_activity_data_error));
		finish();
	}
	
	
	private final class AsyncPopulatePictures extends AsyncTask<Person, Long, Drawable>
	{
		private Drawable drawable;
		private ImageView imageView;
		

		AsyncPopulatePictures(final ImageView imageView)
		{
			super();
			this.imageView = imageView;
		}


		@Override
		protected Drawable doInBackground(final Person... person) 
		{
			try
			{
				drawable = Utilities.loadImageFromWebOperations(Utilities.FACEBOOK_GRAPH_API_URL + person[0].getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL);
			}
			catch (final Exception e)
			{
				Log.e("Classy Games", "Image Load Failed: " + e);
			}

			return drawable;
		}


		@Override
		protected void onPostExecute(final Drawable result)
		{
			imageView.setImageDrawable(result);
		}


	}


}
