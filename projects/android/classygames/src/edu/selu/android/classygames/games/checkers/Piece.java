package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static int TYPE_NORMAL = 0;
	public final static int TYPE_KING = 1;


	public Piece()
	{
		super();
	}


	public Piece(final Coordinate position, final int id)
	{
		super(position, id, TYPE_NORMAL);
	}


	@Override
	public void run()
	// TODO this method is by no means final
	{
		switch (type)
		{
			case TYPE_NORMAL:
				break;

			case TYPE_KING:
				break;
		}
	}


	boolean isTypeNormal()
	{
		return type == TYPE_NORMAL;
	}


	boolean isTypeKing()
	{
		return type == TYPE_KING;
	}


	void setToKing()
	{
		type = TYPE_KING;
	}


}
