package edu.selu.android.classygames;


import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.games.checkers.Board;
import edu.selu.android.classygames.utilities.ServerUtilities;
import edu.selu.android.classygames.utilities.Utilities;


public class CheckersGameFragment extends GenericGameFragment
{


	private MyButton prevButton = null;
	private MyButton[][] buttons;

	private int greenNormal;
	private int greenKing;
	private int orangeNormal;
	private int orangeKing;


/*
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		// retrieve data passed to this activity
		final Bundle bundle = 

		if (bundle == null || bundle.isEmpty())
		{
			activityHasError();
		}
		else
		{
			gameId = bundle.getString(INTENT_DATA_GAME_ID);
			final long challengedId = bundle.getLong(INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = bundle.getString(INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (challengedId < 0 || challengedName == null || challengedName.isEmpty())
			{
				activityHasError();
			}
			else
			{
				personChallenged = new Person(challengedId, challengedName);
				getSupportActionBar().setTitle(CheckersGameActivity.this.getString(R.string.checkers_game_activity_title) + " " + personChallenged.getName());

				initBoard();

				if (gameId == null || gameId.isEmpty())
				{
					initPieces();
				}
				else
				{
					new AsyncGetGame().execute();
				}
			}
		}

		return inflater.inflate(R.layout.checkers_game_fragment, container, false);
	}
*/


/*
	@SuppressLint("NewApi")
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkers_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());

		// retrieve data passed to this activity
		final Bundle bundle = getIntent().getExtras();

		if (bundle == null || bundle.isEmpty())
		{
			activityHasError();
		}
		else
		{
			gameId = bundle.getString(INTENT_DATA_GAME_ID);
			final long challengedId = bundle.getLong(INTENT_DATA_PERSON_CHALLENGED_ID);
			final String challengedName = bundle.getString(INTENT_DATA_PERSON_CHALLENGED_NAME);

			if (challengedId < 0 || challengedName == null || challengedName.isEmpty())
			{
				activityHasError();
			}
			else
			{
				personChallenged = new Person(challengedId, challengedName);
				getSupportActionBar().setTitle(CheckersGameActivity.this.getString(R.string.checkers_game_activity_title) + " " + personChallenged.getName());

				initBoard();

				if (gameId == null || gameId.isEmpty())
				{
					initPieces();
				}
				else
				{
					new AsyncGetGame().execute();
				}
			}
		}
	}
*/


/*
	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.checkers_game_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
*/


/*
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(final MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;

			case R.id.game_fragment_actionbar_send_move:
				new AsyncSendMove().execute();
				return true;

			case R.id.game_fragment_actionbar_undo_move:
				undo();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
*/


	@Override
	public void onPrepareOptionsMenu(final Menu menu)
	{
		if (boardLocked)
		{
			menu.findItem(R.id.game_fragment_actionbar_send_move).setEnabled(true);
			menu.findItem(R.id.game_fragment_actionbar_undo_move).setEnabled(true);
		}
		else
		{
			menu.findItem(R.id.game_fragment_actionbar_send_move).setEnabled(false);
			menu.findItem(R.id.game_fragment_actionbar_undo_move).setEnabled(false);
		}
	}


/*
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void initBoard()
	{
		prevButton = null;
		greenNormal = R.drawable.piece_checkers_green_normal;
		greenKing = R.drawable.piece_checkers_green_king;
		orangeNormal = R.drawable.piece_checkers_orange_normal;
		orangeKing = R.drawable.piece_checkers_orange_king;

		Display display = getWindowManager().getDefaultDisplay();

		TableLayout layout;
		TableLayout.LayoutParams rowLp;
		TableRow.LayoutParams cellLp;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		// if the running version of Android is API Level 13 and higher (Honeycomb 3.2 and up)
		// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
		{
			Point size = new Point();
			display.getSize(size);
			final int screen_width = size.x;

			layout = new TableLayout(this);
			rowLp = new TableLayout.LayoutParams(screen_width, screen_width / 8, 1);
			cellLp = new TableRow.LayoutParams(screen_width / 8, screen_width / 8, 1);
		}
		else
		{
			final int screen_width = display.getWidth();

			layout = new TableLayout(this);
			rowLp = new TableLayout.LayoutParams(screen_width, screen_width / 8, 1);
			cellLp = new TableRow.LayoutParams(screen_width / 8, screen_width / 8, 1);
		}

		TableRow[] rows = new TableRow[8];

		for (int i = 0; i < 8; i++)
		{
			rows[i] = new TableRow(this);
		}

		buttons = new MyButton[8][8];

		for (int y = Board.LENGTH_VERTICAL; y < 8; ++y)
		{
			for (int x = Board.LENGTH_HORIZONTAL; x < 8; ++x)
			{
				buttons[x][y] = new MyButton(this, x, y, true, false, false);
				buttons[x][y].setId(x * 10 + y);
				buttons[x][y].setOnClickListener(this);
				buttons[x][y].setScaleType(ImageView.ScaleType.CENTER_CROP);

				if ((x + y) % 2 == 1)
				{
					setBackground(R.drawable.bg_board_bright, buttons[x][y]);
				}
				else
				{
					setBackground(R.drawable.bg_board_dark, buttons[x][y]);
				}

				rows[y].addView(buttons[x][y], cellLp);
			}
		}

		for (int i = 7; i >= 0; --i)
		{
			layout.addView(rows[i], rowLp);
		}

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.game_fragment_linearlayout);
		linearLayout.addView(layout);
	}


	@Override
	protected void initPieces()
	{
		for (int x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
		{
			for (int y = 0; y < Board.LENGTH_VERTICAL; ++y)
			{
				if (((y + x) % 2 == 1) && (y <= 2 || y >= 5))
				{
					buttons[x][y].setEmpty(false);
					buttons[x][y].setCrown(false);

					if (y <= 2)
					{
						buttons[x][y].setPlayerGreen(true);
						buttons[x][y].setImageResource(greenNormal);
					}
					else
					{
						buttons[x][y].setPlayerGreen(false);
						buttons[x][y].setImageResource(orangeNormal);
					}
				}
			}
		}
	}
*/


	private void clearPieces()
	{
		for (int x = 0; x < Board.LENGTH_HORIZONTAL; ++x)
		{
			for (int y = 0; y < Board.LENGTH_VERTICAL; ++y)
			{
				buttons[x][y].setEmpty(true);
				buttons[x][y].setCrown(false);
				buttons[x][y].setImageResource(0);
			}
		}
	}


/*
	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0)
	{
		MyButton clickedButton = (MyButton) findViewById(arg0.getId());

		//if(!clickedButton.isEmpty() && prevButton == null)

		if (!boardLocked)
		{
			if(!clickedButton.isPlayerGreen()&& !clickedButton.isEmpty())
			{
				prevButton = null;
			}
			else if (prevButton != null)
			{
				if (clickedButton.isEmpty())
				{
					if (canMove(clickedButton))
					{
						Move(clickedButton);
						
						if (canBeKing(clickedButton))
						{
							makeKing(clickedButton);
						}
						
						boardLocked = true;
						this.invalidateOptionsMenu();
					}
					else if (canJump(clickedButton))
					{
						Move(clickedButton);
						if (canBeKing(clickedButton))
						{
							makeKing(clickedButton);
						}
						
						boardLocked = true;
						this.invalidateOptionsMenu();
					}
					else 
					{
						setBackground(R.drawable.bg_board_bright, prevButton);
						prevButton = null;
					}
				}
				else
				{
					setBackground(R.drawable.bg_board_bright, prevButton);
					prevButton = null;
				}
			}
			else
			{
				if (!clickedButton.isEmpty())
				{
					prevButton = clickedButton;
					setBackground(R.drawable.bg_board_bright_selected, clickedButton);
				}
			}
		}
	}
*/

	
	private boolean canBeKing (MyButton pbutton)
	{
		if (pbutton.isPlayerGreen() && pbutton.getPy() == 7)
		{
			return true;
		}
		else if (!pbutton.isPlayerGreen() && pbutton.getPy() == 7)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	private void makeKing(MyButton pButton)
	{
		if(pButton.isPlayerGreen())
		{
			pButton.setImageResource(R.drawable.piece_checkers_green_king);
		}
		else if (!pButton.isPlayerGreen())
		{
			pButton.setImageResource(R.drawable.piece_checkers_orange_king);
		}
		pButton.setCrown(true);
	}
	

	private boolean isKing(MyButton pButton) 
	{		
		if(pButton.isCrown())
			return true;
		else
			return false;
	}

	

	private void Move(MyButton clickedButton) 
	{
		//change image and data
		prevButton.setImageResource(0);
		prevButton.setEmpty(true);

		setBackground(R.drawable.bg_board_bright, prevButton);
		
		if(prevButton.isPlayerGreen() && isKing(prevButton))
		{
			clickedButton.setImageResource(greenKing);
			clickedButton.setCrown(true);
			clickedButton.setPlayerGreen(true);
		}
		
		else if (!prevButton.isPlayerGreen() && isKing(prevButton))
		{
			clickedButton.setImageResource(orangeKing);
			clickedButton.setCrown(true);
			clickedButton.setPlayerGreen(false);
		}
		
		else if (prevButton.isPlayerGreen())
		{
			clickedButton.setImageResource(greenNormal);
			clickedButton.setPlayerGreen(true);
		}
		
		else{
			clickedButton.setImageResource(orangeNormal);
			clickedButton.setPlayerGreen(false);
		}
		
		clickedButton.setEmpty(false);
		prevButton.setCrown(false);
		
		prevButton = null;
	}

	private boolean canMove(MyButton button)
	{	
		if (prevButton.isCrown()){
			if(abs(button.getPx()-prevButton.getPx()) == 1 && abs(button.getPy()- prevButton.getPy()) == 1)
			{
			
				return true;
			}
			else 
				return false;
		}
		else {
			if (!prevButton.isPlayerGreen()){
				if(abs(button.getPx()-prevButton.getPx()) == 1 && (button.getPy()- prevButton.getPy()) == 1)
					return true;
				else 
					return false;
			}
			else {
				if (abs(button.getPx()-prevButton.getPx()) == 1 && (button.getPy()-prevButton.getPy()) == 1)
				{
				
					return true;
				}
				else
					return false;
			}
		}
	}


/*
	private boolean canJump(MyButton cbutton)
	{
		if (!prevButton.isCrown())
		{
			int change_In_X = (cbutton.getPx() - prevButton.getPx())/2;
			int change_In_Y = (cbutton.getPy() - prevButton.getPy())/2;
			
			MyButton middleButton = (MyButton)findViewById((prevButton.getPx() + change_In_X) *10 + (prevButton.getPy() + change_In_Y));
			
			if(prevButton.isPlayerGreen())
			  {
				  if (abs(cbutton.getPx()-prevButton.getPx()) == 2 && (cbutton.getPy()-prevButton.getPy()) == 2)
				  {
						if (middleButton.isPlayerGreen() != prevButton.isPlayerGreen() && !middleButton.isEmpty())
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
			  else
			  {
				  if (abs(cbutton.getPx()-prevButton.getPx()) == 2 && (cbutton.getPy()-prevButton.getPy()) == -2)
				  {
						if (middleButton.isPlayerGreen() != prevButton.isPlayerGreen() && !middleButton.isEmpty())
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
		}
		
		else 
		{	
			int change_In_X = (cbutton.getPx() - prevButton.getPx())/2;
			int change_In_Y = (cbutton.getPy() - prevButton.getPy())/2;
			
			MyButton middleButton = (MyButton)findViewById((prevButton.getPx() + change_In_X) *10 + (prevButton.getPy() + change_In_Y));
			
			if(prevButton.isPlayerGreen())
			  {
				  if (abs(cbutton.getPx()-prevButton.getPx()) == 2 && abs(cbutton.getPy()-prevButton.getPy()) == 2)
				  {
						if (middleButton.isPlayerGreen() != prevButton.isPlayerGreen() && !middleButton.isEmpty())
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
			  else
			  {
				  if (abs(cbutton.getPx()-prevButton.getPx()) == 2 && abs(cbutton.getPy()-prevButton.getPy()) == 2)
				  {
						if (middleButton.isPlayerGreen() != prevButton.isPlayerGreen() && !middleButton.isEmpty())
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
		}
	}
*/


/*
	@SuppressLint("NewApi")
	protected void undo()
	{
		clearPieces();
		if (boardJSON == null || boardJSON.isEmpty())
		// simply re instantiate the default board
		{
			initPieces();
		}
		else
		// re parse the board string that we downloaded
		{
			buildBoard();
		}

		boardLocked = false;
		CheckersGameFragment.this.invalidateOptionsMenu();
	}
*/


/*
	private void activityHasError()
	{
		Utilities.easyToastAndLogError(CheckersGameFragment.this, R.string.game_fragment_data_error);
		finish();
	}
*/


	@Override
	protected void initBoard()
	{

	}


	@Override
	protected void initPieces()
	{

	}


	@Override
	protected void initViews()
	{
		getSherlockActivity().getSupportActionBar().setTitle(CheckersGameFragment.this.getString(R.string.checkers_game_fragment_title) + " " + personChallenged.getName());

		getView().findViewById(R.id.checkers_game_fragment_x0y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y0).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y0).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y1).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y1).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y2).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y2).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y3).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y3).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y4).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y4).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y5).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y5).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y6).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y6).setOnClickListener(onBoardClick);

		getView().findViewById(R.id.checkers_game_fragment_x0y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x1y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x2y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x3y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x4y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x5y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x6y7).setOnClickListener(onBoardClick);
		getView().findViewById(R.id.checkers_game_fragment_x7y7).setOnClickListener(onBoardClick);
	}


	@Override
	protected void onBoardClick(final View v)
	{
		Log.d(LOG_TAG, "Click! " + v.getId());
	}


}

