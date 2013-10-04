package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageButton;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.games.Coordinate;


/**
 * A View that occupies a single position on the BoardView. In Checkers or
 * Chess, 64 of things make up the entire BoardView.
 */
public class PositionView extends ImageButton
{


	public final static float PADDING_DEFAULT = 0;

	private final static int SCALE_TYPE_CENTER_CROP = 0;
	private final static int SCALE_TYPE_CENTER_INSIDE = 1;
	public final static int SCALE_TYPE_DEFAULT = SCALE_TYPE_CENTER_INSIDE;

	private Coordinate coordinate;


	public PositionView(final Context context, final int x, final int y, final float padding, final int scaleType,
		final Drawable brightBackground, final Drawable darkBackground)
	{
		super(context);
		coordinate = new Coordinate(x, y);
		setBackground(brightBackground, darkBackground);
		setScaleType(scaleType);

		final int paddingInt = (int) padding;
		setPadding(paddingInt, paddingInt, paddingInt, paddingInt);
	}


	public Coordinate getCoordinate()
	{
		return coordinate;
	}


	@SuppressWarnings("deprecation")
	private void setBackground(final Drawable brightBackground, final Drawable darkBackground)
	{
		final Drawable background;

		if (coordinate.areBothEitherEvenOrOdd())
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


	private void setScaleType(final int scaleType)
	{
		if (scaleType == SCALE_TYPE_CENTER_CROP)
		{
			setScaleType(ScaleType.CENTER_CROP);
		}
		else if (scaleType == SCALE_TYPE_CENTER_INSIDE)
		{
			setScaleType(ScaleType.CENTER_INSIDE);
		}
	}


}
