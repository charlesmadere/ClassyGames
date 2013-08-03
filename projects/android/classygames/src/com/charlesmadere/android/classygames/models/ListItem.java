package com.charlesmadere.android.classygames.models;


public final class ListItem<T>
{


	private boolean selected;
	private T object;


	public ListItem(final T object)
	{
		this.object = object;
	}


	public boolean isSelected()
	{
		return selected;
	}


	public T get()
	{
		return object;
	}


	public void select()
	{
		selected = true;
	}


	public void unselect()
	{
		selected = false;
	}


}
