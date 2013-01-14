package edu.selu.android.classygames;


import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class MainActivity extends SherlockActivity
{


	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;




	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		sessionStatusCallback = new Session.StatusCallback()
		{
			@Override
			public void call(final Session session, final SessionState state, final Exception exception)
			{
				onSessionStateChange(session, state, exception);
			}
		};

		uiHelper = new UiLifecycleHelper(MainActivity.this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}


	@Override
	protected void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		isResumed = true;

		final Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
		{
			startActivity(new Intent(MainActivity.this, CentralFragmentActivity.class));
		}
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}


	private void onSessionStateChange(final Session session, final SessionState state, final Exception exception)
	{
		if (isResumed)
		// only make changes if this activity is visible
		{
			if (state.equals(SessionState.OPENED))
			// if the session state is open, show the authenticated activity
			{
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback()
				{
					@Override
					public void onCompleted(final GraphUser user, final Response response)
					{
						if (user != null)
						{
							final Person facebookIdentity = new Person(user.getId(), user.getName());
							Utilities.setWhoAmI(MainActivity.this, facebookIdentity);

							startActivity(new Intent(MainActivity.this, CentralFragmentActivity.class));
						}
					}
				});
			}
		}
	}


}
