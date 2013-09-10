package com.charlesmadere.android.classygames;


import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.charlesmadere.android.classygames.utilities.TypefaceUtilities;


public final class ChessGlossaryDialogFragment extends SherlockDialogFragment
{


	public final static String KEY_PLAYER_COLOR = "KEY_PLAYER_COLOR";




	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		getDialog().setTitle(R.string.chess_glossary);
		return inflater.inflate(R.layout.chess_glossary_dialog_fragment, container, false);
	}


	@Override
	@SuppressWarnings("deprecation")
	public void onActivityCreated(final Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		final View view = getView();
		final TextView bishopView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_bishop);
		final TextView kingView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_king);
		final TextView knightView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_knight);
		final TextView pawnView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_pawn);
		final TextView queenView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_queen);
		final TextView rookView = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_rook);
		TypefaceUtilities.applyBlueHighway(bishopView);
		TypefaceUtilities.applyBlueHighway(kingView);
		TypefaceUtilities.applyBlueHighway(knightView);
		TypefaceUtilities.applyBlueHighway(pawnView);
		TypefaceUtilities.applyBlueHighway(queenView);
		TypefaceUtilities.applyBlueHighway(rookView);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			final BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.bg_bright);
			background.setAntiAlias(true);
			background.setDither(true);
			background.setFilterBitmap(true);
			background.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			view.setBackgroundDrawable(background);
		}

		final Bundle arguments = getArguments();

		if (arguments != null && !arguments.isEmpty())
		{
			final int playerColor = arguments.getInt(KEY_PLAYER_COLOR);

			switch (playerColor)
			{
				case R.string.blue:
					setViewDrawable(bishopView, R.drawable.piece_chess_bishop_blue);
					setViewDrawable(kingView, R.drawable.piece_chess_king_blue);
					setViewDrawable(knightView, R.drawable.piece_chess_knight_blue);
					setViewDrawable(queenView, R.drawable.piece_chess_queen_blue);
					setViewDrawable(pawnView, R.drawable.piece_chess_pawn_blue);
					setViewDrawable(rookView, R.drawable.piece_chess_rook_blue);
					break;

				case R.string.green:
					setViewDrawable(bishopView, R.drawable.piece_chess_bishop_green);
					setViewDrawable(kingView, R.drawable.piece_chess_king_green);
					setViewDrawable(knightView, R.drawable.piece_chess_knight_green);
					setViewDrawable(queenView, R.drawable.piece_chess_queen_green);
					setViewDrawable(pawnView, R.drawable.piece_chess_pawn_green);
					setViewDrawable(rookView, R.drawable.piece_chess_rook_green);
					break;

				case R.string.orange:
					setViewDrawable(bishopView, R.drawable.piece_chess_bishop_orange);
					setViewDrawable(kingView, R.drawable.piece_chess_king_orange);
					setViewDrawable(knightView, R.drawable.piece_chess_knight_orange);
					setViewDrawable(queenView, R.drawable.piece_chess_queen_orange);
					setViewDrawable(pawnView, R.drawable.piece_chess_pawn_orange);
					setViewDrawable(rookView, R.drawable.piece_chess_rook_orange);
					break;

				default:
					setViewDrawable(bishopView, R.drawable.piece_chess_bishop_pink);
					setViewDrawable(kingView, R.drawable.piece_chess_king_pink);
					setViewDrawable(knightView, R.drawable.piece_chess_knight_pink);
					setViewDrawable(queenView, R.drawable.piece_chess_queen_pink);
					setViewDrawable(pawnView, R.drawable.piece_chess_pawn_pink);
					setViewDrawable(rookView, R.drawable.piece_chess_rook_pink);
					break;
			}
		}
	}


	private void setViewDrawable(final TextView view, final int drawable)
	{
		view.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
	}


}
