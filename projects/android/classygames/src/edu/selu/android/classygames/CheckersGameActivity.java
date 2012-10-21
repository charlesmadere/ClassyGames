package edu.selu.android.classygames;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class CheckersGameActivity extends SherlockActivity implements OnClickListener
{
	TableLayout layout;
    MyButton[][] buttons;
    
    MyButton prevButton;
    int greenPlayer, orangePlayer;
    

	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_ID = "GAME_PERSON_ID";
	public final static String INTENT_DATA_PERSON_NAME = "GAME_PERSON_NAME";


	private String id;
	private Long personId;
	private String personName;
	

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

		

		prevButton = null;
    	greenPlayer = R.drawable.chkgreen;
    	orangePlayer = R.drawable.chkorange;
    	
        super.onCreate(savedInstanceState);
        //height width 
        Display display = getWindowManager().getDefaultDisplay();
        @SuppressWarnings("deprecation")
		int width = display.getWidth();
       // int height = display.getHeight();
        
        TableRow[] rows = new TableRow[8];
       
        layout = new TableLayout(this);
        FrameLayout.LayoutParams tableLp = new FrameLayout.LayoutParams(width,width,1);
        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams( width,width/8,1);
        TableRow.LayoutParams cellLp= new TableRow.LayoutParams( width/8,width/8,1);
        
        for (int i = 0; i < 8; i++)
        {
        	 rows[i] = new TableRow(this);
        }
        buttons = new MyButton[8][8];
        
        //load buttons
        for (int i = 0; i < 8; i++)
    	{
    		for (int j = 0; j < 8; j++)
    		{
    			buttons[i][j] = new MyButton(this,i,j,true,false);
    			buttons[i][j].setOnClickListener(this);
    			buttons[i][j].setId(i*10+j);
    			
    			if ((i+j)%2 == 0)
    			{
    				buttons[i][j].setBackgroundColor(Color.BLACK);
    				if (i >= 5)
					{
    	    			buttons[i][j].setPlayerGreen(true);//this is Green LOWER IS GREEN
    	    			buttons[i][j].setEmpty(false);
						buttons[i][j].setImageResource(greenPlayer);
					}
    				if (i <= 2)
					{
    	    			buttons[i][j].setPlayerGreen(false);//this is Not Green TOP IS ORANGE
    	    			buttons[i][j].setEmpty(false);
						buttons[i][j].setImageResource(orangePlayer);
					}
    			}
    			
    			else
    			{
    				buttons[i][j].setBackgroundColor(Color.RED);
    				
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
	
	public void onClick(View arg0)
	{
		
		MyButton clickedButton = (MyButton) findViewById(arg0.getId());
		//clickedButton.setBackgroundColor(Color.LTGRAY);
		
		if (prevButton != null)
		{
			if (clickedButton.isEmpty())
			{
				if (canMove(clickedButton))
				{
					//change image and data
					prevButton.setImageResource(0);
					prevButton.setEmpty(true);
					
					//change new button
					int changeImage = orangePlayer;
					if (prevButton.isPlayerGreen())
						changeImage = greenPlayer;
					clickedButton.setImageResource(changeImage);
					clickedButton.setEmpty(false);
					clickedButton.setPlayerGreen(prevButton.isPlayerGreen());
					
					prevButton = null;
				}
			}
			
			else
			{
				prevButton = null;
			}
		}
		
		else
		{
			if (!clickedButton.isEmpty())
			{
				prevButton = clickedButton;
			}
		}
	}
	
	boolean canMove(MyButton button)
	{
		if (abs(button.getPx()-prevButton.getPx()) == 1 && abs(button.getPy()-prevButton.getPy()) == 1)
			return true;
		else
			return false;
	}

	private int abs(int i)
	{	
		return (i < 0)?-1*i:i;
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
