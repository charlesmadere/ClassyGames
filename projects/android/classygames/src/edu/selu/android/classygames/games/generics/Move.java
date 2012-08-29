package edu.selu.android.classygames.games.generics;


public class Move
{


	Coordinate positionNew;
	Coordinate positionOriginal;


	public Move()
	{
		positionNew = new Coordinate();
		positionOriginal = new Coordinate();
	}


	public Move(final Coordinate positionNew, final Coordinate positionOriginal)
	{
		this.positionNew = positionNew;
		this.positionOriginal = positionOriginal;
	}


	Coordinate getPositionNew()
	{
		return positionNew;
	}


	void setPositionNew(final Coordinate positionNew)
	{
		this.positionNew = positionNew;
	}


	Coordinate getPositionOriginal()
	{
		return positionOriginal;
	}


	void setPositionOriginal(final Coordinate positionOriginal)
	{
		this.positionOriginal = positionOriginal;
	}


}
