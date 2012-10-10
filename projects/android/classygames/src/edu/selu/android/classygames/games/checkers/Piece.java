package edu.selu.android.classygames.games.checkers;


import android.graphics.Canvas;
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
	public void draw(final Canvas canvas, final int paddingTop, final int paddingLeft)
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
