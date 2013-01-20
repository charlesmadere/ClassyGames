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
	 * is not a unique identifier for this person. This class's long id
	 * variable, however, <strong>is</strong> a unique identifier.
	 */
	private String name;


	/**
	 * The person's Facebook profile picture. The value of this variable will
	 * definitely be null unless it has been set using this class's
	 * setDrawable(final Drawable picture) method.
	 */
	private Drawable picture;




	/**
	 * Creates a Person object. If possible, <strong>please avoid</strong>
	 * using this constructor, and instead use this class's other, argument
	 * taking, constructors.
	 */
	public Person()
	{

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
	 * Returns this Person's Facebook profile picture (a Drawable). Note that,
	 * unless this object has had the setDrawable(final Drawable picture)
	 * method run on it, this method's return value <strong>will
	 * definitely</strong> be null.
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


	/**
	 * Replaces this Person object's current Facebook ID with this newly given
	 * id. An ID should be a number that is always greater than 0.
	 * 
	 * @param id
	 * New Facebook ID of the user.
	 */
	public void setId(final long id)
	{
		this.id = id;
	}


	/**
	 * Replaces this Person object's current Facebook ID with this newly given
	 * id. An ID should be a number that is always greater than 0. This method
	 * will convert the given String into a long. As this method doesn't check
	 * for the possibility that a null String was given to it, this method will
	 * cause a crash if that happens.
	 * 
	 * @param id
	 * New Facebook ID of the user.
	 */
	public void setId(final String id)
	{
		this.id = Long.parseLong(id);
	}


	/**
	 * Replaces this Person object's current Facebook name with this newly
	 * given name. 
	 * 
	 * @param name
	 * New Facebook name of the user.
	 */
	public void setName(final String name)
	{
		this.name = name;
	}


	/**
	 * Checks to see if this Person object has a picture associated with it.
	 * 
	 * @return
	 * Returns true if this Person object has a picture associated with it.
	 */
	public boolean hasPicture()
	{
		return picture != null;
	}


	/**
	 * Checks to see that this Person object is valid. Valid means three
	 * things:
	 * <ol>
	 * <li>This Person's Facebook ID is greater than or equal to 1.</li>
	 * <li>This Person's name is not null.</li>
	 * <li>This Person's name is not empty.</li>
	 * </ol>
	 * 
	 * @return
	 * Returns true if all of the above conditions are true.
	 */
	public boolean isValid()
	{
		return id >= 1 && name != null && !name.isEmpty();
	}




	/**
	 * When Facebook IDs are acquired throughout the app's runtime they should
	 * be checked for validity. Use this method to check for that validity.
	 * 
	 * @param id
	 * The Facebook ID to check for validity.
	 * 
	 * @return
	 * Returns true if the passed in id variable is greater than or equal to 1.
	 */
	public static boolean isIdValid(final long id)
	{
		return id >= 1;
	}


	/**
	 * When Facebook IDs are acquired throughout the app's runtime they should
	 * be checked for validity. Use this method to check for that validity.
	 * This method allows you to check a whole bunch of IDs at once. If any
	 * single ID that is passed in is invalid, this method will cease to check
	 * the rest and will immediately return false.
	 * 
	 * @param ids
	 * The Facebook IDs to check for validity.
	 * 
	 * @return
	 * Returns true if <strong>all</strong> of the passed in Facebook IDs are
	 * valid.
	 */
	public static boolean isIdValid(final long... ids)
	{
		for (int i = 0; i < ids.length; ++i)
		{
			if (!isIdValid(ids[i]))
			{
				return false;
			}
		}

		return true;
	}


}
