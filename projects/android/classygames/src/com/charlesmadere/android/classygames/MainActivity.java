package com.charlesmadere.android.classygames;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.facebook.*;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;


/**
 * This class is the app's entry point.
 */
public final class MainActivity extends SherlockActivity
{


	private LinearLayout facebook;
	private LinearLayout loading;


	private boolean hasFinished = false;
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;


	/**
	 * Used to obtain the current user's Facebook identity.
	 */
	private AsyncGetFacebookIdentity asyncGetFacebookIdentity;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);

		facebook = (LinearLayout) findViewById(R.id.main_activity_facebook);
		loading = (LinearLayout) findViewById(R.id.main_activity_loading);

		final Session.StatusCallback sessionStatusCallback = new Session.StatusCallback()
		{
			@Override
			public void call(final Session session, final SessionState state, final Exception exception)
			{
				onSessionStateChange(session, state);
			}
		};

		uiHelper = new UiLifecycleHelper(this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_FIRST_USER)
		{
			hasFinished = true;
			finish();
		}
	}


	@Override
	public void onBackPressed()
	{
		if (isAnAsyncTaskRunning())
		{
			cancelRunningAnyAsyncTask();
		}

		super.onBackPressed();
	}


	@Override
	protected void onDestroy()
	{
		cancelRunningAnyAsyncTask();

		isResumed = false;
		uiHelper.onDestroy();
		super.onDestroy();
	}


	@Override
	protected void onPause()
	{
		isResumed = false;
		uiHelper.onPause();
		super.onPause();
	}


	@Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
		isResumed = true;

		if (!hasFinished)
		{
			final Person whoAmI = Utilities.getWhoAmI(this);

			if (whoAmI != null && whoAmI.isValid())
			{
				startGameFragmentActivity();
			}
		}
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		uiHelper.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}




	/**
	 * Cancels the AsyncRefreshGamesList AsyncTask if it is currently
	 * running.
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			asyncGetFacebookIdentity.cancel(true);
		}
	}


	/**
	 * @return
	 * Returns true if an AsyncTask is running.
	 */
	private boolean isAnAsyncTaskRunning()
	{
		return asyncGetFacebookIdentity != null;
	}


	private void onSessionStateChange(final Session session, final SessionState state)
	{
		if (isResumed)
		// only make changes if this activity is visible
		{
			if (state.equals(SessionState.OPENED))
			// if the session state is open, show the authenticated activity
			{
				// store the user's Facebook Access Token for retrieval later
				FacebookUtilities.setAccessToken(this, session.getAccessToken());

				asyncGetFacebookIdentity = new AsyncGetFacebookIdentity(session);
				asyncGetFacebookIdentity.execute();
			}
		}
	}


	private void startGameFragmentActivity()
	{
		final Intent intent = new Intent(this, GameFragmentActivity.class);
		startActivityForResult(intent, RESULT_FIRST_USER);
	}




	private final class AsyncGetFacebookIdentity extends AsyncTask<Void, Void, Person>
	{


		private Context context;
		private Session session;


		private AsyncGetFacebookIdentity(final Session session)
		{
			this.session = session;
			context = MainActivity.this;
		}


		@Override
		protected Person doInBackground(final Void... params)
		{
			final Person facebookIdentity = new Person();

			if (!isCancelled())
			{
				Request.newMeRequest(session, new GraphUserCallback()
				{
					@Override
					public void onCompleted(final GraphUser user, final Response response)
					{
						facebookIdentity.setId(user.getId());
						facebookIdentity.setName(user.getName());
					}
				}).executeAndWait();

				Server.gcmPerformRegister(context);
			}

			return facebookIdentity;
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			if (facebookIdentity.isValid())
			{
				Utilities.setWhoAmI(context, facebookIdentity);
				asyncGetFacebookIdentity = null;

				facebook.setVisibility(View.GONE);
				loading.setVisibility(View.INVISIBLE);

				startGameFragmentActivity();
			}
			else
			{
				finish();
			}
		}


		@Override
		protected void onPreExecute()
		{
			facebook.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
		}


	}




}
