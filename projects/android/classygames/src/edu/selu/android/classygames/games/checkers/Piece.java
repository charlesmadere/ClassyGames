package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static int TYPE_NORMAL = 1;
	public final static int TYPE_KING = 2;


	public Piece()
	{
		super(new Coordinate(), TYPE_NORMAL);
	}


	public Piece(final Coordinate position)
	{
		super(position, TYPE_NORMAL);
	}


	@Override
	public void draw()
	{

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
