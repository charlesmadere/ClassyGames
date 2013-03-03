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

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.FacebookUtilities;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.TypefaceUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class GameOverActivity extends SherlockActivity
{


	public final static String BUNDLE_DATA_MESSAGE_TYPE = "BUNDLE_DATA_MESSAGE_TYPE";
	public final static String BUNDLE_DATA_PERSON_OPPONENT_ID = "BUNDLE_DATA_PERSON_OPPONENT_ID";
	public final static String BUNDLE_DATA_PERSON_OPPONENT_NAME = "BUNDLE_DATA_PERSON_OPPONENT_NAME";




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);

		final Intent intent = getIntent();

		if (intent != null && intent.hasExtra(BUNDLE_DATA_MESSAGE_TYPE) && intent.hasExtra(BUNDLE_DATA_PERSON_OPPONENT_ID)
			&& intent.hasExtra(BUNDLE_DATA_PERSON_OPPONENT_NAME))
		{
			final byte messageTypeString = intent.getByteExtra(BUNDLE_DATA_MESSAGE_TYPE, (byte) -1);
			final long personId = intent.getLongExtra(BUNDLE_DATA_PERSON_OPPONENT_ID, -1);
			final String personName = intent.getStringExtra(BUNDLE_DATA_PERSON_OPPONENT_NAME);

			if (Utilities.verifyValidStrings(personName) && Person.isIdValid(personId))
			{
				final Byte messageType = Byte.valueOf(messageTypeString);

				final ImageView friendPicture = (ImageView) findViewById(R.id.game_over_activity_friend_picture);
				Utilities.getImageLoader(this).displayImage(FacebookUtilities.GRAPH_API_URL + personId + FacebookUtilities.GRAPH_API_URL_PICTURE_TYPE_LARGE_SSL, friendPicture);

				final TextView friendName = (TextView) findViewById(R.id.game_over_activity_friend_name);
				friendName.setText(personName);
				friendName.setTypeface(TypefaceUtilities.getTypeface(getAssets(), TypefaceUtilities.SNELL_ROUNDHAND_BDSCR));

				final TextView winOrLose = (TextView) findViewById(R.id.game_over_activity_win_or_lose);

				switch (messageType.byteValue())
				{
					case ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
						winOrLose.setText(R.string.game_over_activity_win_or_lose_lost);
						break;

					case ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
						winOrLose.setText(R.string.game_over_activity_win_or_lose_won);
						break;
				}

				final Button returnToGamesList = (Button) findViewById(R.id.game_over_activity_button_return);
				returnToGamesList.setTypeface(TypefaceUtilities.getTypeface(getAssets(), TypefaceUtilities.BLUE_HIGHWAY_D));
				returnToGamesList.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v)
					{
						finish();
					}
				});
			}
			else
			{
				finish();
			}
		}
		else
		{
			finish();
		}
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


}
