package edu.selu.android.classygames.games.checkers;


import org.json.JSONException;
import org.json.JSONObject;

import edu.selu.android.classygames.games.GenericBoard;


/**
 * Class representing a Checkers board. This board is made up of a bunch of
 * positions. Checkers is 8 by 8, so that's 64 positions.
 */
public class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;




	/**
	 * Creates a Checkers board object.
	 * 
	 * @throws JSONException
	 * If a glitch or something happened while trying to create some JSON data
	 * then this JSONException will be thrown. When using this particular
	 * constructor this should never happen.
	 */
	public Board() throws JSONException
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL);
	}


	/**
	 * Creates a Checkers board object using the given JSON String.
	 * 
	 * @param boardJSON
	 * JSONObject that represents the board.
	 * 
	 * @throws JSONException
	 * If a glitch or something happened while trying to create some JSON data
	 * then this JSONException will be thrown.
	 */
	public Board(final JSONObject boardJSON) throws JSONException
	{
		super(LENGTH_HORIZONTAL, LENGTH_VERTICAL, boardJSON);
	}




	@Override
	protected Piece buildPiece(final byte whichTeam, final int type)
	{
		return new Piece(whichTeam, type);
	}


	@Override
	protected void initializeDefaultBoard()
	{
		// player team
		getPosition(0, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(0, 3).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(0, 5).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(0, 7).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 4).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 6).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 3).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 5).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 7).setPiece(new Piece(Piece.TEAM_PLAYER));

		// opponent team
		getPosition(0, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(2, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(4, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(6, 7).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(1, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(3, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(5, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(7, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(0, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(2, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(4, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(6, 5).setPiece(new Piece(Piece.TEAM_OPPONENT));
	}


}
