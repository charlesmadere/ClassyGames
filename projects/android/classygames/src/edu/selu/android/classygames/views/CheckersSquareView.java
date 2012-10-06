package edu.selu.android.classygames.views;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import edu.selu.android.classygames.games.checkers.Piece;


public class CheckersSquareView extends ImageView
{


	private Piece piece;


	public CheckersSquareView(final Context context)
	{
		super(context);
		piece = new Piece();

		initialize();
	}


	public CheckersSquareView(final Context context, final Piece piece)
	{
		super(context);
		this.piece = piece;

		initialize();
	}


	public CheckersSquareView(final Context context, final AttributeSet attributes)
	{
		super(context, attributes);
		piece = new Piece();

		initialize();
	}


	public CheckersSquareView(final Context context, final AttributeSet attributes, final Piece piece)
	{
		super(context, attributes);
		this.piece = piece;

		initialize();
	}


	private void initialize()
	{
		setClickable(true);
	}


	@Override
	protected void onDraw(final Canvas canvas)
	{
		super.onDraw(canvas);
		piece.draw(canvas, getPaddingLeft(), getPaddingTop());
	}


	Piece getPiece()
	{
		return piece;
	}


	void setPiece(final Piece piece)
	{
		this.piece = piece;
	}


}
