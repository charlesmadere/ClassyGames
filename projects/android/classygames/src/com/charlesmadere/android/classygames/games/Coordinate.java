package com.charlesmadere.android.classygames.games;


public class Coordinate
{


	/**
	 * This Coordinate object's <strong>X</strong> position.
	 */
	private byte x;


	/**
	 * This Coordinate object's <strong>Y</strong> position.
	 */
	private byte y;




	/**
	 * Creates a Coordinate object. Think of this object as an ordered pair. So
	 * it should look like this: (x, y). If x were to be 5 and y were to be 7,
	 * then the ordered pair would be this: (5, 7).
	 * 
	 * @param x
	 * The X value for this coordinate.
	 * 
	 * @param y
	 * The Y value for this coordinate.
	 */
	public Coordinate(final byte x, final byte y)
	{
		this.x = x;
		this.y = y;
	}


	/**
	 * Creates a Coordinate object. Think of this object as an ordered pair. So
	 * it should look like this: (x, y). If x were to be 5 and y were to be 7,
	 * then the ordered pair would be this: (5, 7).
	 * 
	 * @param x
	 * The X value for this coordinate.
	 * 
	 * @param y
	 * The Y value for this coordinate.
	 */
	public Coordinate(final int x, final int y)
	{
		this((byte) x, (byte) y);
	}


	/**
	 * Parses the passed in tag String for two numbers (those two numbers are
	 * the tag's coordinates) and creates a Coordinate object out of those
	 * Coordinates.
	 * 
	 * <p><strong>Example</strong><br />
	 * final String tag = "x2y9";<br />
	 * Coordinate coordinate = new Coordinate(tag);<br />
	 * coordinate.getX(): 2, coordinate.getY(): 9</p>
	 * 
	 * @param tag
	 * The tag to parse for coordinates. Note that this method <strong>does not
	 * check</strong> for a null or empty String. If this method encounters
	 * either of those scenarios then there will probably be a crash. The tag
	 * that this method parses for should be formatted like so: "x5y3", "x0y6",
	 * "x15y3", or "x21y32". Negative numbers are <strong>not</strong>
	 * supported.
	 */
	public Coordinate(final String tag)
	{
		boolean inDigits = false;

		// used to store positions in the tag in order to make substrings
		int beginIndex = 0;
		int endIndex = 0;

		// store the position we're currently at in the tag String
		int i = 0;

		// This will be used with whether or not the below loop continues to
		// run.
		boolean bothCoordinatesFound = false;

		do
		// Continue to loop until the bothCoordinatesFound variable is true.
		// This will only happen when... both coordinates have been found!
		{
			// save the current char in the String
			final char character = tag.charAt(i);

			// check to see if the char is a digit
			final boolean characterIsDigit = Character.isDigit(character);

			if (!inDigits && characterIsDigit)
			// if our position in the tag String is not already in digits and
			// the current char is a digit
			{
				// mark that we're now in digits
				inDigits = true;

				// store the beginning substring position
				beginIndex = i;
			}
			else if (inDigits && !characterIsDigit)
			// if our position in the tag String is in digits and the current
			// char is not a digit
			{
				// mark that we're no longer in digits
				inDigits = false;

				// store the end substring position
				endIndex = i;

				// create a substring from the tag String
				String sub = tag.substring(beginIndex, endIndex);

				// Parse that substring into a byte. This value is the tag
				// String's X value.
				x = Byte.parseByte(sub);

				// create another substring from the tag String
				sub = tag.substring(endIndex + 1, tag.length());

				// Parse that substring into a byte. this value is the tag
				// String's Y value.
				y = Byte.parseByte(sub);

				// Both coordinates have been found and stored. The loop can
				// exit now.
				bothCoordinatesFound = true;
			}

			// move to the next character in the tag String
			++i;
		}
		while (!bothCoordinatesFound);
	}


	/**
	 * Returns this Coordinate object's <strong>X</strong> position.
	 * 
	 * @return
	 * This Coordinate object's <strong>X</strong> position.
	 */
	public byte getX()
	{
		return x;
	}


	/**
	 * Returns this Coordinate object's <strong>Y</strong> position.
	 * 
	 * @return
	 * This Coordinate object's <strong>Y</strong> position.
	 */
	public byte getY()
	{
		return y;
	}


	/**
	 * Checks to see if this Coordinate's X and Y values are either both even
	 * (as in X % 2 == 0 and Y % 2 == 0) <strong>or</strong> they're both odd
	 * (as in Y % 2 == 1 and Y % 2 == 1). If X is 3 and Y is 2, then this will
	 * return false. If X is 3 and Y is 3, this will return true. If X is 4 and
	 * Y is 2, then this will return true.
	 * 
	 * @return
	 * Returns true if either both X and Y are even numbers or if both X and Y
	 * are odd.
	 */
	public boolean areBothEitherEvenOrOdd()
	{
		return (x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1);
	}




	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}


}
