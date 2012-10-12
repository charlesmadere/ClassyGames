package edu.selu.android.classygames;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;


public class ConfirmGameActivity extends SherlockActivity
{


	public final static String INTENT_DATA_PERSON_ID = "id";
	public final static String INTENT_DATA_PERSON_NAME = "name";


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
				ImageView personPicture = (ImageView) findViewById(R.id.confirm_game_activity_person_picture);
				UrlImageViewHelper.setUrlDrawable(personPicture, Utilities.FACEBOOK_GRAPH_API_URL + id + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL);

				TextView personName = (TextView) findViewById(R.id.confirm_game_activity_person_name);
				personName.setText(name);
				personName.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));

				Button gameAccept = (Button) findViewById(R.id.confirm_game_activity_button_accept);
				gameAccept.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Utilities.easyToastAndLog(ConfirmGameActivity.this, "YOU'RE WINNER");
					}
				});

				Button gameDeny = (Button) findViewById(R.id.confirm_game_activity_button_deny);
				gameDeny.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Utilities.easyToastAndLog(ConfirmGameActivity.this, "YOU'RE LOSER");
					}
				});
			}
		}
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
