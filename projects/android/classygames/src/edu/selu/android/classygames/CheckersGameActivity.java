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
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.selu.android.classygames.data.Person;
import edu.selu.android.classygames.utilities.ServerUtilities;
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
	public final static String INTENT_DATA_PERSON_CHALLENGED_ID = "GAME_PERSON_CHALLENGED_ID";
	public final static String INTENT_DATA_PERSON_CHALLENGED_NAME = "GAME_PERSON_CHALLENGED_NAME";


	private String gameId;
	private Person personChallenged;


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
			
				if( gameId != null && !gameId.isEmpty() )
				{
					new AsyncGetGame().execute();
				}
				else
				{
				}
			
			}
		}

		prevButton = null;
		greenPlayer = R.drawable.piece_checkers_green_normal;
		orangePlayer = R.drawable.piece_checkers_orange_normal;

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
				buttons[i][j] = new MyButton(this, i, j, true, false);
				buttons[i][j].setOnClickListener(this);
				buttons[i][j].setId(i * 10 + j);

				if ((i + j) % 2 == 0)
				{
//					buttons[i][j].setBackgroundColor(Color.WHITE);
					setBackground(R.drawable.bg_board_bright, buttons[i][j]);

					if (i >= 5)
					{
						buttons[i][j].setEmpty(false);
						buttons[i][j].setPlayerGreen(true);
						buttons[i][j].setImageResource(greenPlayer);
					}

					if (i <= 2)
					{
						buttons[i][j].setEmpty(false);
						buttons[i][j].setPlayerGreen(false);
						buttons[i][j].setImageResource(orangePlayer);
					}

					buttons[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
				}
				else
				{
//					buttons[i][j].setBackgroundColor(Color.BLACK);
					setBackground(R.drawable.bg_board_dark, buttons[i][j]);
				}

				rows[i].addView(buttons[i][j], cellLp);
			}
		}

		for (int i = 0; i < 8; i++)
		{
			layout.addView(rows[i],rowLp);
		}

		// finds the linearlayout in the checkers_game_activity.xml file and adds our
		// checkers board to it. Because of properties set in that file, the checkers
		// board will be automatically centered
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.checkers_game_activity_linearlayout);
		linearLayout.addView(layout);

		// I don't believe this line is necessary with our new layout style
//		setContentView(layout, tableLp);
	}


/***************************/


	private final class AsyncGetGame extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... v)
		{
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_ID, gameId));

			try
			{
				return ServerUtilities.postToServer( ServerUtilities.SERVER_GET_GAME_ADDRESS, nameValuePairs );
			}
			catch (final IOException e)
			{
				Log.e(Utilities.LOG_TAG, "Error in HTTP POST to " + ServerUtilities.SERVER_GET_GAME_ADDRESS, e);
			}
			
			return null;
		}


		@Override
		protected void onPostExecute(final String board)
		{
			parseBoard(board);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			System.out.println( "Progress Dialog creation starting");
			progressDialog = new ProgressDialog(CheckersGameActivity.this);
			progressDialog.setMessage(CheckersGameActivity.this.getString(R.string.checkers_game_activity_getgame_progressdialog_message));
			progressDialog.setTitle(R.string.checkers_game_activity_getgame_progressdialog_title);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}


		private void parseBoard(final String jsonString)
		{
			if (jsonString != null && !jsonString.isEmpty())
			{
				// TODO
				// write an algorithm that parses this json string and sets the board
				// accordingly
			}
		}


	}


	private final class AsyncSendMove extends AsyncTask<Void, Void, String>
	{


		private ProgressDialog progressDialog;


		@Override
		protected String doInBackground(final Void... v)
		{
			try
			{
				JSONArray teams = createJSONTeams();
				JSONObject board = new JSONObject();
				board.put("teams", teams);

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				try
				{
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CHALLENGED, Long.valueOf(personChallenged.getId()).toString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_NAME, personChallenged.getName()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_USER_CREATOR, Long.valueOf(Utilities.getWhoAmI(CheckersGameActivity.this).getId()).toString()));
					nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_BOARD, board.toString()));

					if (gameId == null || gameId.isEmpty())
					{
						return ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_GAME_ADDRESS, nameValuePairs);
					}
					else
					{
						nameValuePairs.add(new BasicNameValuePair(ServerUtilities.POST_DATA_GAME_ID, gameId));
						return ServerUtilities.postToServer(ServerUtilities.SERVER_NEW_MOVE_ADDRESS, nameValuePairs);
					}
				}
				catch (final IOException e)
				{
					Log.e(Utilities.LOG_TAG, "Error in HTTP POST to server.", e);
				}
			}
			catch (final JSONException e)
			{
				Log.e(Utilities.LOG_TAG, "Error in creating JSON data to send to server.", e);
			}

			return null;
		}


		@Override
		protected void onPostExecute(final String serverResponse)
		{
			parseServerResponse(serverResponse);

			if (progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
		}


		@Override
		protected void onPreExecute()
		{
			progressDialog = new ProgressDialog(CheckersGameActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage(CheckersGameActivity.this.getString(R.string.checkers_game_activity_sendmove_progressdialog_message));
			progressDialog.setTitle(R.string.checkers_game_activity_sendmove_progressdialog_title);
			progressDialog.show();
		}


		private JSONArray createJSONTeams()
		{
			JSONArray teamGreen = createJSONTeam(true);
			JSONArray teamOrange = createJSONTeam(false);

			JSONArray teams = new JSONArray();
			teams.put(teamGreen);
			teams.put(teamOrange);

			return teams;
		}


		private JSONArray createJSONTeam(final boolean isPlayerGreen)
		{
			JSONArray team = new JSONArray();

			for (int x = 0; x < 8; ++x)
			{
				for (int y = 0; y < 8; ++y)
				{
					if (!buttons[x][y].isEmpty() && buttons[x][y].isPlayerGreen() == isPlayerGreen)
					// this position has a piece in it that is of the given team color
					{
						try
						{
							JSONArray coordinate = new JSONArray();
							coordinate.put(x);
							coordinate.put(y);

							JSONObject piece = new JSONObject();
							piece.put("coordinate", coordinate);

							if( isKing(buttons[x][y]) == true )
							{
								piece.put("type", 2);
							}
							else
							{
								piece.put("type", 1);
							}

							team.put(piece);
						}
						catch (final JSONException e)
						{

						}
					}
				}
			}

			return team;
		}


		private void parseServerResponse(final String jsonString)
		{
			if (jsonString != null && !jsonString.isEmpty())
			{
				// TODO
				// write an algorithm that parses this json string makes a toast
				// response to the user informing them of the server's response
			}
		}


	}


	/********************************/


	@Override
	public void onClick(View arg0)
	{
		MyButton clickedButton = (MyButton) findViewById(arg0.getId());
		//clickedButton.setBackgroundColor(Color.LTGRAY);

		// TODO
		// I like this setBackground function here as it changes the background image
		// of a position that I tap on... but it never changes itself back!
		// try it for yourself!
		if(!clickedButton.isEmpty())
		 setBackground(R.drawable.bg_board_bright_selected, clickedButton);
		
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
			clickedButton.setImageResource(R.drawable.piece_checkers_green_king);
		}
		else
			if (!clickedButton.isPlayerGreen())
		{
			clickedButton.setImageResource(R.drawable.piece_checkers_orange_king);
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
		setBackground(R.drawable.bg_board_bright, prevButton);
		
		prevButton = null;
	}

	private void Move(MyButton clickedButton) 
	{
		//change image and data
		prevButton.setImageResource(0);
		prevButton.setEmpty(true);
		setBackground(R.drawable.bg_board_bright, prevButton);
		
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
		if (isKing(button))
		{
			if (abs(button.getPx()-prevButton.getPx()) == 1 && abs(button.getPy()-prevButton.getPy()) == 1)
				return true;
			else
				return false;
		}
		
		else if (prevButton.isPlayerGreen())
		{
			if (button.getPx()-prevButton.getPx() == -1 && abs(button.getPy()-prevButton.getPy()) == 1)
				return true;
			else
				return false;
		}
		
		else 
		{
			if (button.getPx()-prevButton.getPx() == 1 && abs(button.getPy()-prevButton.getPy()) == 1)
				return true;
			else
				return false;
		}
	}
	
	private boolean canJump(MyButton cbutton)
	{
		if(prevButton.isPlayerGreen())
		  {
			  if (cbutton.getPx()-prevButton.getPx() == -2 && abs(cbutton.getPy()-prevButton.getPy()) == 2)
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
		  
		  else
		  {
			  if (cbutton.getPx()-prevButton.getPx() == 2 && abs(cbutton.getPy()-prevButton.getPy()) == 2)
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
				new AsyncSendMove().execute();
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


	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setBackground(final int resource, final View view)
	{
		final Drawable drawable = getResources().getDrawable(resource);

		if (android.os.Build.VERSION.SDK_INT >= 16)
		// if the version of Android running this code is API Level 16 and higher (JellyBean 4.1 and up)
		// https://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels
		{
			view.setBackground(drawable);
		}
		else
		{
			view.setBackgroundDrawable(drawable);
		}
	}


}

