package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageButton;
import com.charlesmadere.android.classygames.models.games.Coordinate;


/**
 * A View that occupies a single position on the BoardView. In Checkers or
 * Chess, 64 of things make up the entire BoardView.
 */
public final class PositionView extends ImageButton
{


	public final static float PADDING_DEFAULT = 0;

	private Coordinate coordinate;
	private Drawable background;
	private Drawable backgroundSelected;


	public PositionView(final Context context, final byte x, final byte y, final float padding,
		final Drawable brightBackground, final Drawable darkBackground, final Drawable brightBackgroundSelected,
		final Drawable darkBackgroundSelected)
	{
		super(context);
		coordinate = new Coordinate(x, y);
		setBackground(brightBackground, darkBackground, brightBackgroundSelected, darkBackgroundSelected);

		final int paddingInt = (int) padding;
		setPadding(paddingInt, paddingInt, paddingInt, paddingInt);
		setScaleType(ScaleType.CENTER_INSIDE);
	}




	public Coordinate getCoordinate()
	{
		return coordinate;
	}


	@SuppressWarnings("deprecation")
	private void setBackground(final Drawable brightBackground, final Drawable darkBackground,
		final Drawable brightBackgroundSelected, final Drawable darkBackgroundSelected)
	{
		if (coordinate.areBothEitherEvenOrOdd())
		{
			background = darkBackground;
			backgroundSelected = darkBackgroundSelected;
		}
		else
		{
			background = brightBackground;
			backgroundSelected = brightBackgroundSelected;
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


	@SuppressWarnings("deprecation")
	public void select()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			setBackground(backgroundSelected);
		}
		else
		{
			setBackgroundDrawable(backgroundSelected);
		}
	}


	@SuppressWarnings("deprecation")
	public void unselect()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		{
			setBackground(background);
		}
		else
		{
			setBackgroundDrawable(background);
		}
	}


}
