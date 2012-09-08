package edu.selu.android.classygames.games;


public class Person
{


	private int id;
	private String name;


	public Person()
	{
		id = 0;
		name = "Classy Games";
	}


	public Person(final String name)
	{
		id = 0;
		this.name = name;
	}


	public Person(final int id, final String name)
	{
		this.id = id;
		this.name = name;
	}


	public int getId()
	{
		return id;
	}


	public String getName()
	{
		return name;
	}


}
