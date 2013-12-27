package com.charlesmadere.android.classygames.models;


/**
 * This class is meant to be used as the contents of an Android ListView.
 *
 * @param <T>
 * The actual object type that you want to be stored in the Android list.
 */
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


	/**
	 * @return
	 * Returns the object that you gave this class in its constructor.
	 */
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
