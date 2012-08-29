package edu.selu.android.classygames.games.checkers;


import edu.selu.android.classygames.games.generics.Coordinate;


public class Piece
{


	public final static int TYPE_NORMAL = 1;
	public final static int TYPE_KING = 2;


	Coordinate position;
	int type;


	public Piece()
	{
		position = new Coordinate();
		type = TYPE_NORMAL;
	}


	public Piece(final Coordinate position)
	{
		this.position = position;
	}


	Coordinate getPosition()
	{
		return position;
	}


	void setPosition(final Coordinate position)
	{
		this.position = position;
	}


	int getType()
	{
		return type;
	}


	boolean isTypeNormal()
	{
		return type == TYPE_NORMAL;
	}


	boolean isTypeKing()
	{
		return type == TYPE_KING;
	}


	void setKing()
	{
		type = TYPE_KING;
	}


}
