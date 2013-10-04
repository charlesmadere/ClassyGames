package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Utilities;


/**
 * A custom ViewGroup that creates an entire game board. Performs sizing checks
 * so that no individual PositionView on the board is unsquare or non-uniform
 * in shape. Handles rotation changes and will dynamically resize itself to
 * continue to maintain good dimensions despite the orientation flip.
 */
public class BoardView extends ViewGroup
{


	private final static String LOG_TAG = Utilities.LOG_TAG + " - BoardView";
	private final static int COLUMNS_DEFAULT = 2;
	private final static int ROWS_DEFAULT = 2;


	/**
	 * The background that will be given to views whose X and Y coordinates add
	 * up to give an even number sum. What this means is that coordinates like
	 * (0, 0), (3, 5), and (7, 1) will be dark. This is because 0 + 0 = 0 (an
	 * even number), 3 + 5 = 8 (an even number), and 7 + 1 = 8 (an even
	 * number).
	 */
	private Drawable darkBackground;


	/**
	 * The background that will be given to views whose X and Y coordinates add
	 * up to give an odd number sum. So that means coordinates like (1, 0),
	 * (5, 2), and (3, 2) will be bright. This is because 1 + 0 = 1 (an odd
	 * number), 5 + 2 = 7 (an odd number), and 3 + 2 = 5 (an odd number).
	 */
	private Drawable brightBackground;


	/**
	 * The total number of columns that this board has.
	 */
	private int columns;


	/**
	 * The total number of rows that this board has.
	 */
	private int rows;


	/**
	 * The inner-layout padding to be applied to the board's PositionViews.
	 */
	private float padding;


	/**
	 * The image scaling setting to be applied to the board's PositionViews.
	 */
	private int scaleType;


	/**
	 * A two-dimensional array containing all of this board's positions. It's
	 * meant to be accessed like [x][y]. In a board with 4 columns and 4 rows,
	 * coordinate (0, 0) is at the bottom left, coordinate (3, 3) is at the
	 * top right, coordinate (3, 0) is at the bottom right, and coordinate
	 * (0, 3) is at the top left.
	 */
	private PositionView positionViews[][];


	public BoardView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		parseAttributes(attrs);
		createPositions();
	}


	/**
	 * Interested in learning what this method is about? For starters, you
	 * should read the documentation on this method. I promise it's not too
	 * hairy!
	 *
	 * https://developer.android.com/reference/android/view/ViewGroup.html#onLayout(boolean, int, int, int, int)
	 *
	 * But quick-and-rough, this method does the actual placing of its view
	 * children onto the screen.
	 */
	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{
		final int width = positionViews[0][0].getMeasuredWidth();
		final int height = positionViews[0][0].getMeasuredHeight();

		for (int x = 0; x < columns; ++x)
		{
			final int left = width * x;
			final int right = left + width;

			for (int y = 0; y < rows; ++y)
			{
				final int top = height * (rows - (y + 1));
				final int bottom = top + height;

				final PositionView positionView = positionViews[x][y];
				positionView.layout(left, top, right, bottom);
			}
		}
	}


	/**
	 * Again, check them docs:
	 * https://developer.android.com/reference/android/view/View.html#onMeasure(int, int)
	 *
	 * Finds out the size that each child view in this board should be.
	 */
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

		// We use Math.ceil() here to prevent rounding issues. If the width and
		// height measured out to be 50.5px, then that means that some sides of
		// the board wouldn't sit touching the walls of its container. So it'd
		// look funky. However, this also means that we may have a tiny bit of
		// overflow with some positions going a tiny bit off screen.
		// Unfortunately, there's not much that we can do about that one.
		final int widthSize = (int) Math.ceil((double) width / (double) columns);
		final int heightSize = (int) Math.ceil((double) height / (double) rows);

		final int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
		final int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);

		for (int x = 0; x < columns; ++x)
		{
			for (int y = 0; y < rows; ++y)
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


	/**
	 * Applies the given OnClickListener to every one of the PositionViews that
	 * belong to this BoardView.
	 */
	public void setPositionsOnClickListener(final OnClickListener onClickListener)
	{
		for (int x = 0; x < columns; ++x)
		{
			for (int y = 0; y < rows; ++y)
			{
				final PositionView positionView = positionViews[x][y];
				positionView.setOnClickListener(onClickListener);
			}
		}
	}


	/**
	 * Initializes the View objects for all of this board's children.
	 */
	private void createPositions()
	{
		positionViews = new PositionView[columns][rows];
		final Context context = getContext();

		for (int x = 0; x < columns; ++x)
		{
			for (int y = 0; y < rows; ++y)
			{
				final PositionView positionView = new PositionView(context, x, y, padding, scaleType, brightBackground, darkBackground);
				positionViews[x][y] = positionView;
				addView(positionView);
			}
		}
	}


	/**
	 * @return
	 * Returns true if the device's current orientation is landscape.
	 */
	private boolean isOrientationLandscape()
	{
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}


	/**
	 * Reads the AttributeSet object given to us in the constructor and parses
	 * the data out of it.
	 */
	private void parseAttributes(final AttributeSet attrs)
	{
		final Resources.Theme theme = getContext().getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.BoardView, 0, 0);

		try
		{
			brightBackground = attributes.getDrawable(R.styleable.BoardView_bright_background);
			darkBackground = attributes.getDrawable(R.styleable.BoardView_dark_background);
			columns = attributes.getInt(R.styleable.BoardView_columns, COLUMNS_DEFAULT);
			rows = attributes.getInt(R.styleable.BoardView_rows, ROWS_DEFAULT);
			padding = attributes.getDimension(R.styleable.BoardView_position_padding, PositionView.PADDING_DEFAULT);
			scaleType = attributes.getInt(R.styleable.BoardView_position_scaleType, PositionView.SCALE_TYPE_DEFAULT);
		}
		catch (final Exception e)
		{
			Log.e(LOG_TAG, "Exception when reading attributes!", e);
			brightBackground = null;
			darkBackground = null;
			columns = COLUMNS_DEFAULT;
			rows = ROWS_DEFAULT;
			padding = PositionView.PADDING_DEFAULT;
			scaleType = PositionView.SCALE_TYPE_DEFAULT;
		}
		finally
		{
			attributes.recycle();
		}
	}


}
