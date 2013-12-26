package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.charlesmadere.android.classygames.models.Notification;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class GameOverActivity extends BaseActivity
{


	public final static String KEY_NOTIFICATION = "KEY_NOTIFICATION";




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, R.string.game_over, true);
		setContentView(R.layout.game_over_activity);
		final Bundle arguments = getIntent().getExtras();

		if (arguments == null || arguments.isEmpty())
		{
			finish();
		}
		else
		{
			final Notification notification = (Notification) arguments.getSerializable(KEY_NOTIFICATION);

			if (notification == null)
			{
				finish();
			}
			else
			{
				final ImageView friendsPicture = (ImageView) findViewById(R.id.game_over_activity_friend_picture);
				Utilities.getImageLoader().displayImage(FacebookUtilities.getFriendsPictureLarge(this, notification.getPerson().getId()), friendsPicture);

				final TextView friendsName = (TextView) findViewById(R.id.game_over_activity_friend_name);
				friendsName.setText(notification.getPerson().getName());

				final TextView winOrLose = (TextView) findViewById(R.id.game_over_activity_win_or_lose);

				switch (notification.getMessageType())
				{
					case Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_LOSE:
						winOrLose.setText(R.string.you_lost_the_game_better_luck_next_time);
						break;

					case Server.POST_DATA_MESSAGE_TYPE_GAME_OVER_WIN:
						winOrLose.setText(R.string.you_won_the_game_what_a_champ);
						break;
				}

				final Button returnToGamesList = (Button) findViewById(R.id.game_over_activity_button_return);
				returnToGamesList.setOnClickListener(new View.OnClickListener()
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
				onBackPressed();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


}
