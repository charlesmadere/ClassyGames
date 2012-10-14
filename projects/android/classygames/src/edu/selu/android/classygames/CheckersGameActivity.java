package edu.selu.android.classygames;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class CheckersGameActivity extends SherlockActivity implements OnClickListener
{


	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_ID = "GAME_PERSON_ID";
	public final static String INTENT_DATA_PERSON_NAME = "GAME_PERSON_NAME";


	private String id;
	private Long personId;
	private String personName;


	ImageButton[][] buttons;
	TableLayout layout;


	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.checkers_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		// retrieve data passed to this activity
		final Bundle bundle = getIntent().getExtras();

		if (bundle == null)
		{
			activityHasError();
		}
		else
		{
			id = bundle.getString(INTENT_DATA_GAME_ID);
			personId = bundle.getLong(INTENT_DATA_PERSON_ID);
			personName = bundle.getString(INTENT_DATA_PERSON_NAME);
		}

		Display display = getWindowManager().getDefaultDisplay();

		// TODO these values should probably be computed some other way, seeing how these are
		// deprecated methods. But I do definitely think that this is the right track
		// ~ charles
		int width = display.getWidth();
		int height = display.getHeight();

		TableRow[] rows = new TableRow[8];

		layout = new TableLayout(this);
		FrameLayout.LayoutParams tableLp = new FrameLayout.LayoutParams(width,width,1);
		TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams( width,width/8,1);
		TableRow.LayoutParams cellLp= new TableRow.LayoutParams( width/8,width/8,1);

		for (int i = 0; i < 8; i++)
		{
			rows[i] = new TableRow(this);
		}

		buttons = new ImageButton[8][8];

		//load buttons
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				buttons[i][j] = new ImageButton(this);
				buttons[i][j].setOnClickListener(this);
				buttons[i][j].setId(i*10+j);

				if ((i+j)%2 == 0)
				{
					buttons[i][j].setBackgroundColor(Color.RED);
					if (i >= 5)
					{
						buttons[i][j].setImageResource(R.drawable.chkorange);
					}
				}
				else
				{
					buttons[i][j].setBackgroundColor(Color.BLACK);
					if (i <= 2)
					{
						buttons[i][j].setImageResource(R.drawable.chkgreen);
					}
				}

				rows[i].addView(buttons[i][j],cellLp);
			}
		}

		for (int i = 0; i < 8; i++)
		{
			layout.addView(rows[i],rowLp);
		}

		setContentView(layout,tableLp);
	}


	@Override
	public void onClick(final View view)
	{
		ImageButton clickedButton = (ImageButton) findViewById(view.getId());
		clickedButton.setBackgroundColor(Color.CYAN);
	}


	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.checkers_game_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			case R.id.checkers_game_activity_actionbar_send_move:
				// TODO send this move to the server
				Utilities.easyToast(CheckersGameActivity.this, "sent");
				return true;

			case R.id.checkers_game_activity_actionbar_undo_move:
				// TODO undo the move that the user made on the board. this should not have any
				// kind of server connectivity
				Utilities.easyToast(CheckersGameActivity.this, "undone");
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private void activityHasError()
	{
		Utilities.easyToastAndLogError(CheckersGameActivity.this, CheckersGameActivity.this.getString(R.string.checkers_game_activity_data_error));
		finish();
	}


}


/*
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
*/


/*
 * this stuff is from the board branch

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
*/
