package edu.selu.android.classygames;


import android.content.Intent;
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
			id = bundle.getLong(CheckersGameActivity.INTENT_DATA_PERSON_ID);
			name = bundle.getString(CheckersGameActivity.INTENT_DATA_PERSON_NAME);

			if (id <= 0  || name == null)
			{
				activityHasError();
			}
			else
			{
				ImageView personPicture = (ImageView) findViewById(R.id.confirm_game_activity_person_picture);
				UrlImageViewHelper.setUrlDrawable(personPicture, Utilities.FACEBOOK_GRAPH_API_URL + id + Utilities.FACEBOOK_GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL);

				TextView personName = (TextView) findViewById(R.id.confirm_game_activity_person_name);
				personName.setText(name);
				personName.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_SNELL_ROUNDHAND_BLKSCR));

				Button gameAccept = (Button) findViewById(R.id.confirm_game_activity_button_accept);
				gameAccept.setTypeface(Utilities.getTypeface(getAssets(), Utilities.TYPEFACE_BLUE_HIGHWAY_D));
				gameAccept.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						Intent intent = new Intent(ConfirmGameActivity.this, CheckersGameActivity.class);
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_ID, id);
						intent.putExtra(CheckersGameActivity.INTENT_DATA_PERSON_NAME, name);

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


}
