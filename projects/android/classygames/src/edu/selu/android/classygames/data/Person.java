package edu.selu.android.classygames.data;


import android.graphics.drawable.Drawable;


/**
 * Class representing a real person.
 */
public class Person
{


	/**
	 * The person's Facebook ID. This is a unique, always positive, number
	 * across the entire Facebook system.
	 */
	private long id;


	/**
	 * The person's Facebook name. It could be "Charles Madere". Obviously this
	 * is not a unique identifier for this person.
	 */
	private String name;


	/**
	 * The person's Facebook profile picture.
	 */
	private Drawable picture;




	/**
	 * Creates a Person object.
	 * 
	 * @param id
	 * The Facebook ID of the user.
	 * 
	 * @param name
	 * The name of the user. This should also be taken from Facebook.
	 */
	public Person(final long id, final String name)
	{
		this.id = id;
		this.name = name;
	}


	/**
	 * Creates a Person object.
	 * 
	 * @param id
	 * The Facebook ID of the user.
	 * 
	 * @param name
	 * The name of the user. This should also be taken from Facebook.
	 */
	public Person(final String id, final String name)
	{
		this.id = Long.parseLong(id);
		this.name = name;
	}


	/**
	 * Returns this Person's Facebook ID (a long).
	 * 
	 * @return
	 * Returns this Person's Facebook ID (a long).
	 */
	public long getId()
	{
		return id;
	}


	/**
	 * Returns this Person's Facebook name (a String). This is that person's
	 * <strong>whole name</strong>.
	 * 
	 * @return
	 * Returns this Person's Facebook name (a String).
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * Returns this Person's Facebook profile picture (a Drawable). Note that
	 * this has the possibility of being null.
	 * 
	 * @return
	 * Returns this Person's Facebook profile picture (a Drawable).
	 */
	public Drawable getPicture()
	{
		return picture;
	}


	/**
	 * Replaces this Person object's current picture with this newly given
	 * picture. Note that because this class has no constructor that supplies
	 * it an initial picture variable, if this method is never used then this
	 * object's picture variable <strong>will definitely be</strong> null.
	 * 
	 * @param picture
	 * The new picture to assign to this Person object.
	 */
	public void setDrawable(final Drawable picture)
	{
		this.picture = picture;
	}


}
