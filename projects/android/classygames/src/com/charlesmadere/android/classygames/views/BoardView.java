package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.charlesmadere.android.classygames.R;


public class BoardView extends ViewGroup
{


	private final static int COLUMNS_DEFAULT = 2;
	private final static int ROWS_DEFAULT = 2;

	private Drawable darkBackground;
	private Drawable brightBackground;
	private int columns;
	private int rows;
	private int totalPositions;
	private PositionView positionViews[][];



	public BoardView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		parseAttributes(attrs);
		createPositions();
	}


	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{
		final int dimension;

		if (isOrientationLandscape())
		{
			dimension = (b - t) / rows;
		}
		else
		{
			dimension = (r - l) / columns;
		}

		for (int x = 0; x < rows; ++x)
		{
			final int left = l + (l * x);
			final int right = left + dimension;

			for (int y = 0; y < columns; ++y)
			{
				final int top = t + (t * y);
				final int bottom = top + dimension;

				final PositionView positionView = positionViews[x][y];
				positionView.layout(left, top, right, bottom);
			}
		}
	}


	@Override
	@SuppressWarnings("SuspiciousNameCombination")
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		final int height;
		final int width;

		if (isOrientationLandscape())
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

		final int widthSpec = MeasureSpec.makeMeasureSpec(width / totalPositions, MeasureSpec.EXACTLY);
		final int heightSpec = MeasureSpec.makeMeasureSpec(height / totalPositions, MeasureSpec.EXACTLY);

		for (int x = 0; x < rows; ++x)
		{
			for (int y = 0; y < columns; ++y)
			{
				final PositionView positionView = positionViews[x][y];
				positionView.measure(widthSpec, heightSpec);
			}
		}
	}


	@Override
	public boolean shouldDelayChildPressedState()
	{
		return false;
	}


	public void setPositionsOnClickListener(final OnClickListener onClickListener)
	{
		for (int x = 0; x < rows; ++x)
		{
			for (int y = 0; y < columns; ++y)
			{
				final PositionView positionView = positionViews[x][y];
				positionView.setOnClickListener(onClickListener);
			}
		}
	}


	private void createPositions()
	{
		positionViews = new PositionView[rows][columns];
		final Context context = getContext();

		for (int x = 0; x < rows; ++x)
		{
			for (int y = 0; y < columns; ++y)
			{
				final PositionView positionView = new PositionView(context, x, y, brightBackground, darkBackground);
				positionViews[x][y] = positionView;
			}
		}
	}


	private boolean isOrientationLandscape()
	{
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}


	private void parseAttributes(final AttributeSet attrs)
	{
		final Resources.Theme theme = getContext().getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.BoardView, 0, 0);

		try
		{
			darkBackground = attributes.getDrawable(R.styleable.BoardView_dark_background);
			brightBackground = attributes.getDrawable(R.styleable.BoardView_bright_background);
			columns = attributes.getInt(R.styleable.BoardView_columns, COLUMNS_DEFAULT);
			rows = attributes.getInt(R.styleable.BoardView_rows, ROWS_DEFAULT);
		}
		catch (final Exception e)
		{
			darkBackground = null;
			brightBackground = null;
			columns = COLUMNS_DEFAULT;
			rows = ROWS_DEFAULT;
		}
		finally
		{
			attributes.recycle();
		}

		totalPositions = columns * rows;
	}


}
