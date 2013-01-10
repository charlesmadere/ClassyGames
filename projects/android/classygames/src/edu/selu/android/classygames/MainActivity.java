package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class MainActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		final LoginButton loginButton = (LoginButton) findViewById(R.id.main_activity_button_login_with_facebook);
		loginButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				Session.openActiveSession(MainActivity.this, true, new Session.StatusCallback()
				{
					@Override
					public void call(final Session session, final SessionState state, final Exception exception)
					{
						if (session.isOpened())
						{
							// make request to the /me API
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback()
							{
								@Override
								public void onCompleted(final GraphUser user, final Response response)
								{
									if (user != null)
									{
										// store this user's data (their real name and facebook ID)
										final Person facebookIdentity = new Person(user.getId(), user.getName());
										Utilities.setWhoAmI(MainActivity.this, facebookIdentity);

										startActivity(new Intent(MainActivity.this, CentralFragmentActivity.class));
									}
								}
							});
						}
					}
				});
			}
		});
	}


	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(MainActivity.this, requestCode, resultCode, data);;
	}


}
