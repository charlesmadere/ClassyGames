package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.charlesmadere.android.classygames.R;
import com.charlesmadere.android.classygames.utilities.Typefaces;


public final class TypefaceButton extends Button
{


	private int typeface;


	public TypefaceButton(final Context context)
	{
		super(context);
	}


	public TypefaceButton(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);
		parseAttributes(attrs);
		applyTypeface();
	}


	public TypefaceButton(final Context context, final AttributeSet attrs, final int defStyle)
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
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.TypefaceButton, 0, 0);

		try
		{
			typeface = attributes.getInt(R.styleable.TypefaceButton_typefaceButton, 0);
		}
		finally
		{
			attributes.recycle();
		}
	}


}
