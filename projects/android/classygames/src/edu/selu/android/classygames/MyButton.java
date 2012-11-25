package edu.selu.android.classygames;


import android.content.Context;
import android.widget.ImageButton;


public class MyButton extends ImageButton
{


	private boolean isEmpty;
	private boolean playerGreen;
	private boolean crown;
	private int px, py;


	public MyButton(final Context view, final int x, final int y, final boolean isEmpty, final boolean playerGreen,final boolean crown)//add other params
	{
		super(view);
		px = x;
		py = y;
		this.isEmpty = isEmpty;
		this.playerGreen = playerGreen;
		this.setCrown(crown);
	}


	public int getPy()
	{
		return py;
	}


	public void setPy(final int py)
	{
		this.py = py;
	}


	public boolean isEmpty()
	{
		return isEmpty;
	}


	public void setEmpty(final boolean isEmpty)
	{
		this.isEmpty = isEmpty;
	}


	public boolean isPlayerGreen()
	{
		return playerGreen;
	}


	public void setPlayerGreen(final boolean playerGreen)
	{
		this.playerGreen = playerGreen;
	}


	public int getPx()
	{
		return px;
	}


	public void setPx(final int px)
	{
		this.px = px;
	}


	public boolean isCrown() 
	{
		return crown;
	}


	public void setCrown(boolean crown)
	{
		this.crown = crown;
	}


}
