package edu.selu.android.classygames;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;


public class CheckersGameActivity extends SherlockActivity implements OnClickListener
{
	TableLayout layout;
	ImageButton[][] buttons;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.checkers_game_activity);
		Utilities.styleActionBar(getResources(), getSupportActionBar());
		
			Display display = getWindowManager().getDefaultDisplay();
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
	public void onClick(View arg0)
	{
		ImageButton clickedButton = (ImageButton) findViewById(arg0.getId());
		clickedButton.setBackgroundColor(Color.CYAN);
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
	}

	*/

