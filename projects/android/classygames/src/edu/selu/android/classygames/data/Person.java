package edu.selu.android.classygames.data;


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


	public long getId()
	{
		return id;
	}


	public String getName()
	{
		return name;
	}


}
