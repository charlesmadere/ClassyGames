package com.charlesmadere.android.classygames.settings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.SharkActivity;
import com.charlesmadere.android.classygames.utilities.Utilities;


public final class AboutActivity extends SherlockActivity
{


	public final static String GITHUB_URL = "https://github.com/ScootrNova/ClassyGames";


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		Utilities.setActionBar(this, R.string.about, true);

		final ImageView logo = (ImageView) findViewById(R.id.about_activity_imageview_logo);
		logo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(final View v)
			{
				startActivity(new Intent(AboutActivity.this, SharkActivity.class));
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		final MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.about_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				break;

			case R.id.about_activity_menu_github:
				gitHub();
				break;

			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}


	private void gitHub()
	{
		final Uri uri = Uri.parse(GITHUB_URL);
		final Intent github = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(github);
	}


}
