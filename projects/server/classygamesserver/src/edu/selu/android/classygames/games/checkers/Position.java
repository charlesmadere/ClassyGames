package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericPosition;


public class Position extends GenericPosition
{


	private final static byte PIECE_NORMAL = 1;
	private final static byte PIECE_KING = 2;


	public Position()
	{
		coordinate = new Coordinate();
		hasPiece = PIECE_NONE;
	}


	public Position(final Coordinate coordinate)
	{
		super(coordinate);
	}


	public Position(final Coordinate coordinate, final byte piece)
	{
		this(coordinate);

		switch (piece)
		{
			case PIECE_NORMAL:
				hasPiece = PIECE_NORMAL;
				break;

			case PIECE_KING:
				hasPiece = PIECE_KING;
				break;

			default:
				hasPiece = PIECE_NONE;
				break;
		}
	}


	boolean hasPieceNormal()
	{
		return hasPiece == PIECE_NORMAL;
	}


	boolean hasPieceKing()
	{
		return hasPiece == PIECE_KING;
	}


}
