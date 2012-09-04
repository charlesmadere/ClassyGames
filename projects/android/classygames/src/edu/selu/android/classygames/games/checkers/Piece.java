package edu.selu.android.classygames.games.checkers;


import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import edu.selu.android.classygames.R;
import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericPiece;


public class Piece extends GenericPiece
{


	public final static int TYPE_NORMAL = 0;
	public final static int TYPE_KING = 1;


	public static BitmapDrawable ICON_NORMAL_GREEN;
	public static BitmapDrawable ICON_KING_GREEN;
	public static BitmapDrawable ICON_NORMAL_ORANGE;
	public static BitmapDrawable ICON_KING_ORANGE;


	public Piece()
	{
		super(new Coordinate(), TYPE_NORMAL);
	}


	public Piece(final Resources resources, final Coordinate position)
	{
		super(position, TYPE_NORMAL);

		ICON_NORMAL_GREEN = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
		ICON_KING_GREEN = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
		ICON_NORMAL_ORANGE = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
		ICON_KING_ORANGE = (BitmapDrawable) resources.getDrawable(R.drawable.bg_actionbar);
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
