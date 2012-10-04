package edu.selu.android.classygames;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;


public class CheckersGameActivity extends SherlockActivity
{


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkers_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		//Testing
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Toast.makeText(CheckersGameActivity.this,""+ position, Toast.LENGTH_SHORT).show();
			}
		});
	}


}
