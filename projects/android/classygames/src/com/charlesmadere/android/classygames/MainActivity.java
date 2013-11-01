package com.charlesmadere.android.classygames;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.server.Server;
import com.charlesmadere.android.classygames.server.ServerApiRegister;
import com.charlesmadere.android.classygames.utilities.FacebookUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;
import com.facebook.*;
import com.facebook.Request.GraphUserCallback;
import com.facebook.model.GraphUser;


/**
 * This class is the app's entry point.
 */
public final class MainActivity extends SherlockActivity implements
	Session.StatusCallback
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - MainActivity";


	private LinearLayout facebook;
	private LinearLayout loading;
	private ProgressBar loadingSpinner;
	private TextSwitcher loadingText;


	private boolean hasFinished = false;
	private boolean isResumed = false;
	private ServerApiRegister serverApiTask;
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
		loadingSpinner = (ProgressBar) findViewById(R.id.main_activity_loading_spinner);
		loadingText = (TextSwitcher) findViewById(R.id.main_activity_loading_text);

		loadingText.setFactory(new ViewSwitcher.ViewFactory()
		{
			@Override
			public View makeView()
			{
				return getLayoutInflater().inflate(R.layout.main_activity_loading_text, null);
			}
		});

		uiHelper = new UiLifecycleHelper(this, this);
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


	@Override
	public void call(final Session session, final SessionState state, final Exception exception)
	{
		onSessionStateChange(session, state);
	}


	/**
	 * Cancels the AsyncRefreshGamesList AsyncTask if it is currently
	 * running.
	 */
	private void cancelRunningAnyAsyncTask()
	{
		if (isAnAsyncTaskRunning())
		{
			if (serverApiTask != null)
			{
				serverApiTask.cancel();
			}

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


		private Session session;
		private SherlockActivity activity;


		private AsyncGetFacebookIdentity(final Session session)
		{
			activity = MainActivity.this;
			this.session = session;
		}


		@Override
		protected Person doInBackground(final Void... params)
		{
			final Person facebookIdentity = new Person();

			if (!isCancelled())
			{
				try
				{
					Thread.sleep(Server.WAIT_FOR_SERVER_DELAY);
				}
				catch (final InterruptedException e)
				{
					Log.w(LOG_TAG, "AsyncGetFacebookIdentity thread sleep interrupted!", e);
				}

				Request.newMeRequest(session, new GraphUserCallback()
				{
					@Override
					public void onCompleted(final GraphUser user, final Response response)
					{
						facebookIdentity.setId(user.getId());
						facebookIdentity.setName(user.getName());
					}
				}).executeAndWait();
			}

			return facebookIdentity;
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			if (facebookIdentity.isValid())
			{
				Utilities.setWhoAmI(activity, facebookIdentity);

				serverApiTask = new ServerApiRegister(activity, false, new ServerApiRegister.RegisterListeners()
				{
					@Override
					public void onCancel()
					{}


					@Override
					public void onComplete(final String serverResponse)
					{
						loadingSpinner.setVisibility(View.INVISIBLE);
						final String name = facebookIdentity.getFirstName();
						loadingText.setText(getString(R.string.hello_x, name));

						final Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								asyncGetFacebookIdentity = null;
								startGameFragmentActivity();
							}
						}, Server.WAIT_FOR_SERVER_DELAY);
					}


					@Override
					public void onDismiss()
					{}


					@Override
					public void onRegistrationFail()
					{}


					@Override
					public void onRegistrationSuccess()
					{}
				});

				loadingText.setText(getString(R.string.registering_you_with_our_servers));

				final Handler handler = new Handler();
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						serverApiTask.execute(false);
					}
				}, Server.WAIT_FOR_SERVER_DELAY);
			}
			else
			{
				loadingText.setText(getString(R.string.we_had_a_problem_gathering_your_facebook_information));
			}
		}


		@Override
		protected void onPreExecute()
		{
			facebook.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			loadingText.setText(getString(R.string.authenticating_you_with_facebook));
		}


	}




}
