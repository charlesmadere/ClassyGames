package edu.selu.android.classygames;


import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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


	public final static int CENTRAL_FRAGMENT_ACTIVITY_RESULT_CODE = 64;


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

		if (resultCode == CENTRAL_FRAGMENT_ACTIVITY_RESULT_CODE)
		{
			finish();
		}
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

		final Person whoAmI = Utilities.getWhoAmI(MainActivity.this);

		if (whoAmI != null && whoAmI.isValid())
		{
			startCentralFragmentActivity();
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
				new AsyncGetFacebookIdentity(session).execute();
			}
		}
	}


	private void startCentralFragmentActivity()
	{
		final Intent intent = new Intent(MainActivity.this, CentralFragmentActivity.class);
		startActivityForResult(intent, CENTRAL_FRAGMENT_ACTIVITY_RESULT_CODE);
	}




	private final class AsyncGetFacebookIdentity extends AsyncTask<Void, Void, Person>
	{


		private ProgressDialog progressDialog;
		private Session session;


		AsyncGetFacebookIdentity(final Session session)
		{
			this.session = session;
		}


		@Override
		protected Person doInBackground(final Void... params)
		{
			final Person facebookIdentity = new Person();

			if (!isCancelled())
			{
				Request.newMeRequest(session, new Request.GraphUserCallback()
				{
					@Override
					public void onCompleted(final GraphUser user, final Response response)
					{
						if (!isCancelled())
						{
							facebookIdentity.setId(user.getId());
							facebookIdentity.setName(user.getName());
						}
					}
				}).executeAndWait();
			}

			return facebookIdentity;
		}


		@Override
		protected void onCancelled(final Person facebookIdentity)
		{
			session.closeAndClearTokenInformation();

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPostExecute(final Person facebookIdentity)
		{
			Utilities.setWhoAmI(MainActivity.this, facebookIdentity);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}

			startCentralFragmentActivity();
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setCancelable(true);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(getString(R.string.main_activity_get_facebook_identity_progressdialog_message));

			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(final DialogInterface dialog)
				{
					AsyncGetFacebookIdentity.this.cancel(false);
				}
			});

			progressDialog.setTitle(R.string.main_activity_get_facebook_identity_progressdialog_title);
			progressDialog.show();
		}


	}




}
