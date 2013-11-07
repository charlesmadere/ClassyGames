package com.charlesmadere.android.classygames.settings;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.BaseActivity;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.SharkActivity;


public final class AboutActivity extends BaseActivity
{


	public final static String GITHUB_URL = "https://github.com/ScootrNova/ClassyGames";


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, R.string.about, true);
		setContentView(R.layout.about_activity);

		final ImageView logo = (ImageView) findViewById(R.id.about_activity_imageview_logo);
		logo.setOnClickListener(new View.OnClickListener()
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
