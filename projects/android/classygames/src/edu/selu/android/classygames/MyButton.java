
package edu.selu.android.classygames;

import android.content.Context;
import android.widget.ImageButton;

public class MyButton extends ImageButton{
	private int px, py;
	private boolean isEmpty;
	private boolean playerGreen;
	
	public MyButton(Context view, int x, int y, boolean isEmpty, boolean playerGreen)//add other params
	{
		super(view);
		px = x;
		py = y;
		this.isEmpty = isEmpty;
		this.playerGreen = playerGreen;
	}
	
	public int getPy() {
		return py;
	}

	public void setPy(int py) {
		this.py = py;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isPlayerGreen() {
		return playerGreen;
	}

	public void setPlayerGreen(boolean playerGreen) {
		this.playerGreen = playerGreen;
	}

	public int getPx() {
		return px;
	}

	public void setPx(int px) {
		this.px = px;
	}
  
}
