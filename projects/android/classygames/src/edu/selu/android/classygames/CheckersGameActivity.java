package edu.selu.android.classygames;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.views.CheckersBoardSquareView;


public class CheckersGameActivity extends SherlockActivity
{


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkers_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private class CheckersGameAdapter extends BaseAdapter
	{


		private Context context;


		@Override
		public int getCount()
		{
			return 0;
		}


		@Override
		public Object getItem(final int item)
		{
			return null;
		}


		@Override
		public long getItemId(final int item)
		{
			return 0;
		}


		@Override
		public View getView(final int position, View convertView, final ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = new View(context);
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.checkers_game_activity_gridview_item, parent, false);
			}

			CheckersBoardSquareView checkersBoardSquareView = (CheckersBoardSquareView) convertView.findViewById(R.id.checkers_game_activity_gridview_item_square);
			checkersBoardSquareView.setImageResource(R.drawable.bg_subtlegrey);

			return convertView;
		}


	}


}
