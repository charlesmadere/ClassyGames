package edu.selu.android.classygames.games.checkers;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import edu.selu.android.classygames.R;
import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericTeam;


public class Team extends GenericTeam
{


	public final static int TEAM_GREEN = 0;
	public final static int TEAM_ORANGE = 1;

	// ignore these warnings for now!!
	private static BitmapDrawable ICON_NORMAL_GREEN;
	private static BitmapDrawable ICON_KING_GREEN;
	private static BitmapDrawable ICON_NORMAL_ORANGE;
	private static BitmapDrawable ICON_KING_ORANGE;


	public Team()
	{
		super();
	}


	public Team(final Resources resources, final int whichTeam)
	{
		super(whichTeam);

		switch (this.whichTeam)
		{
			case TEAM_GREEN:
//				ICON_NORMAL_GREEN = (BitmapDrawable) resources.getDrawable(R.drawable.checkers_piece_normal_green);
//				ICON_KING_GREEN = (BitmapDrawable) resources.getDrawable(R.drawable.checkers_piece_king_green);

				pieces.add(new Piece(new Coordinate(1, 0), 0));
				pieces.add(new Piece(new Coordinate(3, 0), 1));
				pieces.add(new Piece(new Coordinate(5, 0), 2));
				pieces.add(new Piece(new Coordinate(7, 0), 3));
				pieces.add(new Piece(new Coordinate(0, 1), 4));
				pieces.add(new Piece(new Coordinate(2, 1), 5));
				pieces.add(new Piece(new Coordinate(4, 1), 6));
				pieces.add(new Piece(new Coordinate(6, 1), 7));
				pieces.add(new Piece(new Coordinate(1, 2), 8));
				pieces.add(new Piece(new Coordinate(3, 2), 9));
				pieces.add(new Piece(new Coordinate(5, 2), 10));
				pieces.add(new Piece(new Coordinate(7, 2), 11));
				break;

			case TEAM_ORANGE:
//				ICON_NORMAL_ORANGE = (BitmapDrawable) resources.getDrawable(R.drawable.checkers_piece_normal_orange);
//				ICON_KING_ORANGE = (BitmapDrawable) resources.getDrawable(R.drawable.checkers_piece_king_orange);

				pieces.add(new Piece(new Coordinate(0, 7), 0));
				pieces.add(new Piece(new Coordinate(2, 7), 1));
				pieces.add(new Piece(new Coordinate(4, 7), 2));
				pieces.add(new Piece(new Coordinate(6, 7), 3));
				pieces.add(new Piece(new Coordinate(1, 6), 4));
				pieces.add(new Piece(new Coordinate(3, 6), 5));
				pieces.add(new Piece(new Coordinate(5, 6), 6));
				pieces.add(new Piece(new Coordinate(7, 6), 7));
				pieces.add(new Piece(new Coordinate(0, 5), 8));
				pieces.add(new Piece(new Coordinate(2, 5), 9));
				pieces.add(new Piece(new Coordinate(4, 5), 10));
				pieces.add(new Piece(new Coordinate(6, 5), 11));
				break;
		}
	}


//	@Override
//	public void run()
//	// TODO
//	{
//		for (GenericPiece piece : pieces)
//		{
//
//		}
//	}


}
