package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageButton;
import com.charlesmadere.android.classygames.models.games.Coordinate;


public class PositionView extends ImageButton
{


	private Coordinate coordinate;


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
	}


	public Coordinate getCoordinate()
	{
		return coordinate;
	}


}
