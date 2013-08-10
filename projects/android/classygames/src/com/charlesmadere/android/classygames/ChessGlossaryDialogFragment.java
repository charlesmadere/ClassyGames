package com.charlesmadere.android.classygames;


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
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		return inflater.inflate(R.layout.chess_glossary_dialog_fragment, container, false);
	}


	@Override
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

		final Bundle arguments = getArguments();

		if (arguments != null && !arguments.isEmpty())
		{
			final int playerColor = arguments.getInt(KEY_PLAYER_COLOR);

			switch (playerColor)
			{
				case R.string.blue:
					bishopView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_bishop_blue, 0, 0);
					kingView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_king_blue, 0, 0);
					knightView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_knight_blue, 0, 0);
					queenView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_queen_blue, 0, 0);
					pawnView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_pawn_blue, 0, 0);
					rookView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_rook_blue, 0, 0);
					break;

				case R.string.green:
					bishopView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_bishop_green, 0, 0);
					kingView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_king_green, 0, 0);
					knightView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_knight_green, 0, 0);
					queenView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_queen_green, 0, 0);
					pawnView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_pawn_green, 0, 0);
					rookView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_rook_green, 0, 0);
					break;

				case R.string.orange:
					bishopView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_bishop_orange, 0, 0);
					kingView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_king_orange, 0, 0);
					knightView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_knight_orange, 0, 0);
					queenView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_queen_orange, 0, 0);
					pawnView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_pawn_orange, 0, 0);
					rookView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_rook_orange, 0, 0);
					break;

				default:
					bishopView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_bishop_pink, 0, 0);
					kingView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_king_pink, 0, 0);
					knightView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_knight_pink, 0, 0);
					queenView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_queen_pink, 0, 0);
					pawnView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_pawn_pink, 0, 0);
					rookView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.piece_chess_rook_pink, 0, 0);
					break;
			}
		}
	}


}
