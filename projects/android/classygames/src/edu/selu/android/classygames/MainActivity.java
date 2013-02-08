package edu.selu.android.classygames;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import edu.selu.android.classygames.models.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class MainActivity extends SherlockActivity
{


	public final static int GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FINISH = 8;


	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback sessionStatusCallback;

	private boolean isResumed = false;
	private boolean hasFinished = false;




	/**
	 * Boolean that marks if the AsyncGetFacebookIdentity AsyncTask is
	 * currently running. This is used in conjunction with cancelling that
	 * AsyncTask.
	 */
	private boolean isAsyncGetFacebookIdentityRunning = false;


	/**
	 * Used to obtain the current user's Facebook identity.
	 */
	private AsyncGetFacebookIdentity asyncGetFacebookIdentity;




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

		uiHelper = new UiLifecycleHelper(this, sessionStatusCallback);
		uiHelper.onCreate(savedInstanceState);
	}


	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

		if (resultCode == GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FINISH)
		{
			hasFinished = true;
			finish();
		}
	}


	@Override
	public void onBackPressed()
	{
		if (isAsyncGetFacebookIdentityRunning)
		{
			asyncGetFacebookIdentity.cancel(true);
		}
		else
		{
			super.onBackPressed();
		}
	}


	@Override
	protected void onDestroy()
	{
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
			final Person whoAmI = Utilities.WhoAmIUtilities.getWhoAmI(this);
	
			if (whoAmI != null && whoAmI.isValid())
			{
				startCentralFragmentActivity();
			}
		}
	}


	@Override
	protected void onSaveInstanceState(final Bundle outState)
	{
		uiHelper.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}




	private void onSessionStateChange(final Session session, final SessionState state, final Exception exception)
	{
		if (isResumed)
		// only make changes if this activity is visible
		{
			if (state.equals(SessionState.OPENED))
			// if the session state is open, show the authenticated activity
			{
				asyncGetFacebookIdentity = new AsyncGetFacebookIdentity(this, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), session, (ViewGroup) findViewById(R.id.main_activity_listview));
				asyncGetFacebookIdentity.execute();
			}
		}
	}


	private void startCentralFragmentActivity()
	{
		final Intent intent = new Intent(MainActivity.this, GameFragmentActivity.class);
		startActivityForResult(intent, GAME_FRAGMENT_ACTIVITY_REQUEST_CODE_FINISH);
	}




	private final class AsyncGetFacebookIdentity extends AsyncTask<Void, Void, Person>
	{


		private Context context;
		private LayoutInflater inflater;
		private Session session;
		private ViewGroup viewGroup;


		AsyncGetFacebookIdentity(final Context context, final LayoutInflater inflater, final Session session, final ViewGroup viewGroup)
		{
			this.context = context;
			this.inflater = inflater;
			this.session = session;
			this.viewGroup = viewGroup;
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
			}

			return facebookIdentity;
		}


		private void cancelled()
		{
			session.closeAndClearTokenInformation();
			viewGroup.removeAllViews();
			isAsyncGetFacebookIdentityRunning = false;

			finish();
		}


		@Override
		protected void onCancelled()
		{
			cancelled();
		}


		@Override
		protected void onCancelled(final Person facebookIdentity)
		{
			cancelled();
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			if (facebookIdentity.isValid())
			{
				Utilities.WhoAmIUtilities.setWhoAmI(context, facebookIdentity);
				viewGroup.removeAllViews();
				isAsyncGetFacebookIdentityRunning = false;

				startCentralFragmentActivity();
			}
			else
			{
				cancelled();
			}
		}


		@Override
		protected void onPreExecute()
		{
			isAsyncGetFacebookIdentityRunning = true;
			viewGroup.removeAllViews();
			inflater.inflate(R.layout.main_activity_loading, viewGroup);
		}


	}




}
