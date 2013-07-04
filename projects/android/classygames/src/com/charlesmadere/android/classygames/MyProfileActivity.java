package com.charlesmadere.android.classygames;


import android.os.Bundle;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.charlesmadere.android.classygames.models.Person;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;
import com.charlesmadere.android.classygames.utilities.Utilities;


public class MyProfileActivity extends SherlockActivity
{


	private Person whoAmI;




	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_activity);
		Utilities.setActionBar(this, R.string.my_profile, true);

		whoAmI = Utilities.getWhoAmI(this);

		final TextView myName = (TextView) findViewById(R.id.my_profile_activity_textview_my_name);
		myName.setText(whoAmI.getName());
		TypefaceUtilities.applyTypefaceSnellRoundhand(getAssets(), myName);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				onBackPressed();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
