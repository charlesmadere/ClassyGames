package com.charlesmadere.android.classygames;


import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.charlesmadere.android.classygames.utilities.Typefaces;


public final class ChessGlossaryDialogFragment extends SherlockDialogFragment
{


	private final static String KEY_PLAYER_COLOR = "KEY_PLAYER_COLOR";

	private ImageView bishopImage;
	private ImageView kingImage;
	private ImageView knightImage;
	private ImageView pawnImage;
	private ImageView queenImage;
	private ImageView rookImage;




	public static ChessGlossaryDialogFragment newInstance(final int playerColor)
	{
		final Bundle arguments = new Bundle();
		arguments.putInt(KEY_PLAYER_COLOR, playerColor);

		final ChessGlossaryDialogFragment fragment = new ChessGlossaryDialogFragment();
		fragment.setArguments(arguments);

		return fragment;
	}




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
		bishopImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_bishop_imageview);
		kingImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_king_imageview);
		knightImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_knight_imageview);
		pawnImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_pawn_imageview);
		queenImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_queen_imageview);
		rookImage = (ImageView) view.findViewById(R.id.chess_glossary_dialog_fragment_rook_imageview);

		final TextView bishopText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_bishop_textview);
		final TextView kingText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_king_textview);
		final TextView knightText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_knight_textview);
		final TextView pawnText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_pawn_textview);
		final TextView queenText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_queen_textview);
		final TextView rookText = (TextView) view.findViewById(R.id.chess_glossary_dialog_fragment_rook_textview);
		Typefaces.applyBlueHighway(bishopText);
		Typefaces.applyBlueHighway(kingText);
		Typefaces.applyBlueHighway(knightText);
		Typefaces.applyBlueHighway(pawnText);
		Typefaces.applyBlueHighway(queenText);
		Typefaces.applyBlueHighway(rookText);

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
					setViewDrawables(R.drawable.piece_chess_bishop_blue, R.drawable.piece_chess_king_blue,
						R.drawable.piece_chess_knight_blue, R.drawable.piece_chess_queen_blue,
						R.drawable.piece_chess_pawn_blue, R.drawable.piece_chess_rook_blue);
					break;

				case R.string.green:
					setViewDrawables(R.drawable.piece_chess_bishop_green, R.drawable.piece_chess_king_green,
						R.drawable.piece_chess_knight_green, R.drawable.piece_chess_queen_green,
						R.drawable.piece_chess_pawn_green, R.drawable.piece_chess_rook_green);
					break;

				case R.string.orange:
					setViewDrawables(R.drawable.piece_chess_bishop_orange, R.drawable.piece_chess_king_orange,
						R.drawable.piece_chess_knight_orange, R.drawable.piece_chess_queen_orange,
						R.drawable.piece_chess_pawn_orange, R.drawable.piece_chess_rook_orange);
					break;

				case R.string.pink:
				default:
					setViewDrawables(R.drawable.piece_chess_bishop_pink, R.drawable.piece_chess_king_pink,
						R.drawable.piece_chess_knight_pink, R.drawable.piece_chess_queen_pink,
						R.drawable.piece_chess_pawn_pink, R.drawable.piece_chess_rook_pink);
					break;
			}
		}
	}


	private void setViewDrawables(final int bishop, final int king,
		final int knight, final int queen, final int pawn, final int rook)
	{
		bishopImage.setImageResource(bishop);
		kingImage.setImageResource(king);
		knightImage.setImageResource(knight);
		queenImage.setImageResource(queen);
		pawnImage.setImageResource(pawn);
		rookImage.setImageResource(rook);
	}


}
