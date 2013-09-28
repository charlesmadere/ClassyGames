package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.models.games.Coordinate;


public class BoardPosition extends ImageButton
{


	private Coordinate coordinate;


	public BoardPosition(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);

		final Resources.Theme theme = context.getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.BoardPosition, 0, 0);

		try
		{
			final int x = attributes.getInt(R.styleable.BoardPosition_coordinate_x, 0);
			final int y = attributes.getInt(R.styleable.BoardPosition_coordinate_y, 0);
			coordinate = new Coordinate(x, y);
		}
		finally
		{
			attributes.recycle();
		}
	}


	@Override
	@SuppressWarnings("SuspiciousNameCombination")
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		final int height;
		final int width;

		// Since we want this view to be absolutely square, we set both dimensions to exactly the
		// same size. If we're in landscape mode, then we know that the screen has more pixels wide
		// than it does tall, and therefore we'll restrict the view's overall dimensions to the
		// smaller number (height).

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			height = MeasureSpec.getSize(heightMeasureSpec);
			width = height;
		}
		else
		{
			width = MeasureSpec.getSize(widthMeasureSpec);
			height = width;
		}

		setMeasuredDimension(width, height);
	}


	public Coordinate getCoordinate()
	{
		return coordinate;
	}


}
