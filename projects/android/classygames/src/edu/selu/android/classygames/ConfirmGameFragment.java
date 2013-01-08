package edu.selu.android.classygames;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class ConfirmGameFragment extends SherlockFragment
{


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{

		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		final Bundle bundle = getIntent().getExtras();

		if (bundle == null || bundle.isEmpty())
		// bundle should NOT equal null
		{
			activityHasError();
		}
		else
		{
			final long challengedId = bundle.getLong(GenericGameFragment.INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = bundle.getString(GenericGameFragment.INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (challengedId < 0 || challengedName == null || challengedName.isEmpty())
			{
				activityHasError();
			}
			else
			{
				final Person personChallenged = new Person(challengedId, challengedName);

				ImageView personPicture = (ImageView) findViewById(R.id.confirm_game_activity_person_picture);
				personPicture.setImageResource(R.drawable.fb_placeholder);
				new AsyncPopulatePicture(personPicture).execute(personChallenged);

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
						Intent intent = new Intent(ConfirmGameFragment.this, GenericGameFragment.class);
						intent.putExtra(GenericGameFragment.INTENT_DATA_PERSON_CHALLENGED_ID, personChallenged.getId());
						intent.putExtra(GenericGameFragment.INTENT_DATA_PERSON_CHALLENGED_NAME, personChallenged.getName());

						// start the ConfirmGameActivity with a bit of extra data. We're passing it both
						// the id and the name of the facebook person that the user has decided to challenge
						startActivityForResult(intent, 0);
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
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode)
		{
			case CentralFragmentActivity.NEED_TO_REFRESH:
				setResult(CentralFragmentActivity.NEED_TO_REFRESH);
				finish();
				break;
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
		Utilities.easyToastAndLogError(ConfirmGameFragment.this, ConfirmGameFragment.this.getString(R.string.confirm_game_activity_data_error));
		finish();
	}
	
	
	private final class AsyncPopulatePicture extends AsyncTask<Person, Void, Drawable>
	{


		private ImageView imageView;


		AsyncPopulatePicture(final ImageView imageView)
		{
			super();
			this.imageView = imageView;
		}


		@Override
		protected Drawable doInBackground(final Person... person) 
		{
			return Utilities.loadImageFromWebOperations(Utilities.FACEBOOK_GRAPH_API_URL + person[0].getId() + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL);
		}


		@Override
		protected void onPostExecute(final Drawable result)
		{
			imageView.setImageDrawable(result);
		}


	}


}
