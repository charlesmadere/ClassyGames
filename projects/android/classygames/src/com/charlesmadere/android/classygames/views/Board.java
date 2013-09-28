package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.charlesmadere.android.classygames.R;


public class Board extends ViewGroup
{


	private final static int COLUMNS_DEFAULT = 2;
	private final static int ROWS_DEFAULT = 2;

	private int columns;
	private int rows;



	public Board(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);

		final Resources.Theme theme = context.getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.Board, 0, 0);

		try
		{
			columns = attributes.getInt(R.styleable.Board_columns, 2);
			rows = attributes.getInt(R.styleable.Board_rows, 2);
		}
		finally
		{
			attributes.recycle();
		}
	}


	@Override
	protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b)
	{

	}


}
