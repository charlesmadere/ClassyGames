package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.ServerUtilities;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.nostra13.universalimageloader.core.ImageLoader;


public class GameOverActivity extends SherlockActivity
{


	public final static String BUNDLE_MESSAGE_TYPE = "BUNDLE_MESSAGE_TYPE";
	public final static String BUNDLE_PERSON_OPPONENT_ID = "BUNDLE_PERSON_OPPONENT_ID";
	public final static String BUNDLE_PERSON_OPPONENT_NAME = "BUNDLE_PERSON_OPPONENT_NAME";




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar(), true);
		final ImageLoader imageLoader = Utilities.getImageLoader(this);

		final Intent intent = getIntent();

		if (intent != null)
		{
			final byte messageType = intent.getByteExtra(BUNDLE_MESSAGE_TYPE, (byte) -1);
			final long personId = intent.getLongExtra(BUNDLE_PERSON_OPPONENT_ID, -1);
			final String personName = intent.getStringExtra(BUNDLE_PERSON_OPPONENT_NAME);

			if (Utilities.verifyValidStrings(personName) && Person.isIdAndNameValid(personId, personName))
			{
				final ImageView friendPicture = (ImageView) findViewById(R.id.game_over_activity_friend_picture);
				imageLoader.displayImage(FacebookUtilities.getFriendsPictureLarge(personId), friendPicture);

				final TextView friendName = (TextView) findViewById(R.id.game_over_activity_friend_name);
				friendName.setText(personName);
				friendName.setTypeface(TypefaceUtilities.getTypeface(getAssets(), TypefaceUtilities.SNELL_ROUNDHAND_BDSCR));

				final TextView winOrLose = (TextView) findViewById(R.id.game_over_activity_win_or_lose);

				switch (messageType)
				{
					case ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
						winOrLose.setText(R.string.you_lost_the_game_better_luck_next_time);
						break;

					case ServerUtilities.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
						winOrLose.setText(R.string.you_won_the_game_what_a_champ);
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
