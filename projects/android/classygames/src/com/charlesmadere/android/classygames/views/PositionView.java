package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageButton;
import com.charlesmadere.android.classygames.models.games.Coordinate;

import java.util.Random;


public class PositionView extends ImageButton
{


	private Coordinate coordinate;
	private static Random random;


	@SuppressWarnings("deprecation")
	public PositionView(final Context context, final int x, final int y, final Drawable brightBackground,
		final Drawable darkBackground)
	{
		super(context);
		coordinate = new Coordinate(x, y);
		final boolean isDark = coordinate.areBothEitherEvenOrOdd();
		final Drawable background;

		if (isDark)
		{
			background = darkBackground;
		}
		else
		{
			background = brightBackground;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			setBackground(background);
		}
		else
		{
			setBackgroundDrawable(background);
		}

		setRandomBackgroundColor();
	}


	public Coordinate getCoordinate()
	{
		return coordinate;
	}


	private void setRandomBackgroundColor()
	{
		if (random == null)
		{
			random = new Random(System.nanoTime());
		}

		int color;

		do
		{
			color = random.nextInt() % 10;
		}
		while (color < 0 || color > 11);

		switch (color)
		{
			case 0:
				color = Color.BLACK;
				break;

			case 1:
				color = Color.DKGRAY;
				break;

			case 2:
				color = Color.GRAY;
				break;

			case 3:
				color = Color.LTGRAY;
				break;

			case 4:
				color = Color.WHITE;
				break;

			case 5:
				color = Color.RED;
				break;

			case 6:
				color = Color.GREEN;
				 break;

			case 7:
				color = Color.BLUE;
				break;

			case 8:
				color = Color.GREEN;
				break;

			case 9:
				color = Color.YELLOW;
				break;

			case 10:
				color = Color.CYAN;
				break;

			case 11:
			default:
				color = Color.MAGENTA;
				break;
		}

		setBackgroundColor(color);
	}


}
