package edu.selu.android.classygames.models;


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
	 * @return
	 * Returns this Person's Facebook ID (a long).
	 */
	public long getId()
	{
		return id;
	}


	/**
	 * Converts this Person's Facebook ID (a long) into a String and then
	 * returns that String.
	 * 
	 * @return
	 * Returns this Person's Facebook ID as a String.
	 */
	public String getIdAsString()
	{
		return Long.valueOf(id).toString();
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
	 * Returns true if all of the above conditions are true. Returns false if
	 * any single one of the above conditions are false.
	 */
	public boolean isValid()
	{
		return isIdValid(id) && isNameValid(name);
	}




	/**
	 * When Facebook IDs are acquired throughout the app's runtime they should
	 * be checked for validity. Use this method to check for that validity.
	 * Valid means one thing:
	 * <ol>
	 * <li>This ID is greater than or equal to 1.</li>
	 * </ol>
	 * 
	 * @param id
	 * The Facebook ID to check for validity.
	 * 
	 * @return
	 * Returns true if the above condition is true. Returns false if the above
	 * condition is false.
	 */
	public static boolean isIdValid(final long id)
	{
		return id >= 1;
	}


	/**
	 * When Facebook IDs are acquired throughout the app's runtime they should
	 * be checked for validity. Use this method to check for that validity.
	 * This method allows you to check a whole bunch of IDs at once. If any
	 * single ID that is passed in is invalid, then this method will cease to
	 * check the rest and will immediately return false.
	 * 
	 * @param ids
	 * The Facebook IDs to check for validity.
	 * 
	 * @return
	 * Returns true if <strong>all</strong> of the passed in Facebook IDs are
	 * valid. Returns false if <strong>any single</strong> ID is invalid.
	 */
	public static boolean areIdsValid(final long... ids)
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


	/**
	 * When Facebook names are acquired throughout the app's runtime they
	 * should be checked to make sure they're not messed up in any way. Use
	 * this method to check to make sure that they're not messed up.
	 * 
	 * @param name
	 * The Facebook name to check for validity.
	 * 
	 * @return
	 * Returns true if the passed in Facebook name is valid.
	 */
	public static boolean isNameValid(final String name)
	{
		return name != null && !name.isEmpty();
	}


	/**
	 * When Facebook names are acquired throughout the app's runtime they
	 * should be checked to make sure they're not messed up in any way. This
	 * method allows you to check a whole bunch of names at once. If any single
	 * name that is passed in is invalid, then this method will cease to check
	 * the rest and will immediately return false.
	 * 
	 * @param names
	 * The Facebook names to check for validity.
	 * 
	 * @return
	 * Returns true if <strong>all</strong> of the passed in Facebook names are
	 * valid. Returns false if <strong>any single</strong> name is invalid.
	 */
	public static boolean areNamesValid(final String... names)
	{
		for (int i = 0; i < names.length; ++i)
		{
			if (!isNameValid(names[i]))
			{
				return false;
			}
		}

		return true;
	}


}
