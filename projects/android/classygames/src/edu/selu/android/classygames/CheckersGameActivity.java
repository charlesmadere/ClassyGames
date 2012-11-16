package edu.selu.android.classygames;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
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

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.Utilities;


public class CheckersGameActivity extends SherlockActivity implements OnClickListener
{


	TableLayout layout;
	MyButton[][] buttons;
	
	FrameLayout.LayoutParams tableLp;
	TableLayout.LayoutParams rowLp;
	TableRow.LayoutParams cellLp;
	TableRow[] rows;
	 
	MyButton prevButton;
	int greenPlayer, orangePlayer;
	//AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.DialogWindowTitle_Sherlock);

	public final static String INTENT_DATA_GAME_ID = "GAME_ID";
	public final static String INTENT_DATA_PERSON_CREATOR_ID = "GAME_PERSON_CREATOR_ID";
	public final static String INTENT_DATA_PERSON_CREATOR_NAME = "GAME_PERSON_CREATOR_NAME";
	public final static String INTENT_DATA_PERSON_CHALLENGED_ID = "GAME_PERSON_CHALLENGED_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_NAME = "GAME_PERSON_CHALLENGED_NAME";


	private String gameId;
	private Person personCreator;
	private Person personChallenged;


	@SuppressLint("NewApi")
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
			gameId = bundle.getString(INTENT_DATA_GAME_ID);
			personCreator = new Person(bundle.getLong(INTENT_DATA_PERSON_CREATOR_ID) ,bundle.getString(INTENT_DATA_PERSON_CREATOR_NAME));
			personChallenged = new Person(bundle.getLong(INTENT_DATA_PERSON_CHALLENGED_ID), bundle.getString(INTENT_DATA_PERSON_CHALLENGED_NAME));

			if (personCreator.getId() < 0 || personChallenged.getId() < 0 || personChallenged.getName().isEmpty())
			{
				activityHasError();
			}
			else
			{
				getSupportActionBar().setTitle(CheckersGameActivity.this.getString(R.string.checkers_game_activity_title) + " " + personChallenged.getName());
			}
		}

		prevButton = null;
    	greenPlayer = R.drawable.chkgreen;
    	orangePlayer = R.drawable.chkorange;

        Display display = getWindowManager().getDefaultDisplay();
        
        
        if (android.os.Build.VERSION.SDK_INT >= 13)
        {
            //do stuff pertaining to this version here
        	Point size = new Point();
        	display.getSize(size);
        	int screen_width = size.x;
       		
        	layout = new TableLayout(this);
        	tableLp = new FrameLayout.LayoutParams(screen_width,screen_width,1);
        	rowLp = new TableLayout.LayoutParams( screen_width,screen_width/8,1);
         	cellLp= new TableRow.LayoutParams( screen_width/8,screen_width/8,1);
        
        	
        }
        else
        {
            //other versions
              @SuppressWarnings("deprecation")
              int width = display.getWidth();
         		
              layout = new TableLayout(this);
              tableLp = new FrameLayout.LayoutParams(width,width,1);
              rowLp = new TableLayout.LayoutParams( width,width/8,1);
              cellLp= new TableRow.LayoutParams( width/8,width/8,1);
        
        }
       
        
        TableRow[] rows = new TableRow[8];
       
       
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
    				buttons[i][j].setBackgroundColor(Color.WHITE);
    				if (i >= 5)
					{
    	    			buttons[i][j].setPlayerGreen(true); 
    	    			buttons[i][j].setEmpty(false);
						buttons[i][j].setImageResource(greenPlayer);
					}
    				
    				if (i <= 2)
					{
    	    			buttons[i][j].setPlayerGreen(false);
    	    			buttons[i][j].setEmpty(false);
						buttons[i][j].setImageResource(orangePlayer);
					}
    			}
    			
    			else
    			{
    				buttons[i][j].setBackgroundColor(Color.BLACK);
    				
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
					Move(clickedButton);
					if (isKing(clickedButton))
					{
						makeKing(clickedButton);
					}
				}
				else if (canJump(clickedButton))
				{
					Jump(clickedButton);
					if (isKing(clickedButton))
					{
						makeKing(clickedButton);
					}
				}
				else 
				{
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
			prevButton = clickedButton;
		}
	}
	
	
	private void makeKing(MyButton clickedButton)
	{
		if(clickedButton.isPlayerGreen())
		{
			clickedButton.setImageResource(R.drawable.kinged_green);
		}
		else
			if (!clickedButton.isPlayerGreen())
		{
			clickedButton.setImageResource(R.drawable.kinged_orange);
		}
	}

	private boolean isKing(MyButton clickedButton) 
	{
		
			if(clickedButton.getPx() == 7 || clickedButton.getPx() == 0)
			{
				
				return true;
			}
			
			else
			{
				return false;
			}
		
	}

	private void Jump(MyButton clickedButton) 
	{
		int changeImage = orangePlayer;
		if (prevButton.isPlayerGreen())
			changeImage = greenPlayer;
		clickedButton.setImageResource(changeImage);
		clickedButton.setEmpty(false);
		clickedButton.setPlayerGreen(prevButton.isPlayerGreen());
		
		prevButton.setEmpty(true);
		prevButton.setImageResource(0);
		
		prevButton = null;
	}

	private void Move(MyButton clickedButton) 
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

	private boolean canMove(MyButton button)
	{
		if (abs(button.getPx()-prevButton.getPx()) == 1 && abs(button.getPy()-prevButton.getPy()) == 1)
			return true;
		else
			return false;
	}
	
	private boolean canJump(MyButton cbutton)
	{
		if (abs(cbutton.getPx()-prevButton.getPx()) == 2 && abs(cbutton.getPy()-prevButton.getPy()) == 2)
		{
			int change_In_X = (cbutton.getPx() - prevButton.getPx())/2;
			int change_In_Y = (cbutton.getPy() - prevButton.getPy())/2;
			
			MyButton middleButton = (MyButton)findViewById((prevButton.getPx() + change_In_X) *10 + (prevButton.getPy() + change_In_Y));
			
			if (middleButton.isPlayerGreen() != prevButton.isPlayerGreen())
			{
				middleButton.setEmpty(true);
				middleButton.setImageResource(0);
				
				return true;
			}
			
			else 
			{
				return false;
			}
		}
		
		else 
		{
			return false;
		}
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
				Utilities.easyToast(CheckersGameActivity.this, "sent move with gameId \"" + gameId + "\"");
				return true;

			case R.id.checkers_game_activity_actionbar_undo_move:
				// TODO undo the move that the user made on the board
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

