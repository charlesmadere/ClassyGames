/*
 * Copyright 2013 Simple Finance Corporation. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.charlesmadere.android.classygames.utilities;


import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;


/**
 * Style a Spannable with a custom Typeface. This code was largely taken from
 * a blog post found here:
 * http://www.tristanwaddington.com/2013/03/styling-the-android-action-bar-with-a-custom-font/
 *
 * This class is primarily used to give the action bar a custom typeface.
 *
 * @author
 * Tristan Waddington
 */
public final class StyledString extends MetricAffectingSpan
{


	private Typeface typeface;


	/**
	 * Applies the given Typeface to a Spannable String.
	 */
	public StyledString(final Typeface typeface)
	{
		this.typeface = typeface;
	}


	@Override
	public void updateMeasureState(final TextPaint tp)
	{
		tp.setTypeface(typeface);
	}


	@Override
	public void updateDrawState(final TextPaint tp)
	{
		tp.setTypeface(typeface);
	}


}
