package edu.selu.android.classygames.data;


public class Person
{


	private long id;
	private String name;


	/**
	 * Creates a Person object.
	 * 
	 * @param id
	 * The Facebook ID of the user.
	 * 
	 * @param name
	 * The name of the user. This should probably be taken from Facebook.
	 */
	public Person(final long id, final String name)
	{
		this.id = id;
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
