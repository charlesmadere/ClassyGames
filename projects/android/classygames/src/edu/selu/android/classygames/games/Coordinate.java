package edu.selu.android.classygames.games;


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

		// used to store positions in the tag String in order to make
		// substrings
		int beginIndex = 0, endIndex = 0;

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




	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}


}
