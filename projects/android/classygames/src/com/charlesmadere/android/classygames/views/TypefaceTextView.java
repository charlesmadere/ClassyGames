package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Typefaces;


public final class TypefaceTextView extends TextView
{


	private int typeface;


	public TypefaceTextView(final Context context)
	{
		super(context);
	}


	public TypefaceTextView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		parseAttributes(attrs);
		applyTypeface();
	}


	public TypefaceTextView(final Context context, final AttributeSet attrs, final int defStyle)
	{
		super(context, attrs, defStyle);
		parseAttributes(attrs);
		applyTypeface();
	}


	private void applyTypeface()
	{
		if (typeface == 1 || typeface == 2)
		{
			final Typeface font;

			if (typeface == 1)
			{
				font = Typefaces.getBlueHighway();
			}
			else
			{
				font = Typefaces.getSnellRoundhand();
			}

			setTypeface(font);
		}
	}


	private void parseAttributes(final AttributeSet attrs)
	{
		final Resources.Theme theme = getContext().getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, 0, 0);

		try
		{
			typeface = attributes.getInt(R.styleable.TypefaceTextView_typeface, 0);
		}
		finally
		{
			attributes.recycle();
		}
	}


}
