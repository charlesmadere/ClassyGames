package com.charlesmadere.android.classygames.views;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.charlesmadere.android.classygames.R;


public final class LayoutFittingImageView extends ImageView
{


	private final static int FIT_DIMENSION_AUTO = 0;
	private final static int FIT_DIMENSION_LANDSCAPE = 1;
	private final static int FIT_DIMENSION_PORTRAIT = 2;


	private int fitDimension;


	public LayoutFittingImageView(final Context context, final AttributeSet attrs)
	{
		super(context, attrs);

		final Resources.Theme theme = context.getTheme();
		final TypedArray attributes = theme.obtainStyledAttributes(attrs, R.styleable.LayoutFittingImageView, 0, 0);

		try
		{
			fitDimension = attributes.getInteger(R.styleable.LayoutFittingImageView_fitDimension, FIT_DIMENSION_AUTO);
		}
		finally
		{
			attributes.recycle();
		}
	}


	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	{
		final Drawable drawable = getDrawable();

		if (drawable == null)
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		else
		{
			final int fitDimension;

			if (this.fitDimension == FIT_DIMENSION_LANDSCAPE || this.fitDimension == FIT_DIMENSION_PORTRAIT)
			{
				fitDimension = this.fitDimension;
			}
			else
			{
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
				{
					fitDimension = FIT_DIMENSION_LANDSCAPE;
				}
				else
				{
					fitDimension = FIT_DIMENSION_PORTRAIT;
				}
			}

			final float dWidth = (float) drawable.getIntrinsicWidth();
			final float dHeight = (float) drawable.getIntrinsicHeight();
			final int width;
			final int height;

			if (fitDimension == FIT_DIMENSION_LANDSCAPE)
			{
				height = MeasureSpec.getSize(heightMeasureSpec);
				width = (int) Math.ceil((float) height * dWidth / dHeight);
			}
			else
			{
				width = MeasureSpec.getSize(widthMeasureSpec);
				height = (int) Math.ceil((float) width * dHeight / dWidth);
			}

			setMeasuredDimension(width, height);
		}
	}


}
