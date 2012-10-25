package edu.selu.android.classygames.data;


public class Person
{


	private long id;
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


	public Person(final long id, final String name)
	{
		this.id = id;
		this.name = name;
	}


	public long getId()
	{
		return id;
	}


	public void setId(final long id)
	{
		this.id = id;
	}


	public String getName()
	{
		return name;
	}


	public void setName(final String name)
	{
		this.name = name;
	}


}
