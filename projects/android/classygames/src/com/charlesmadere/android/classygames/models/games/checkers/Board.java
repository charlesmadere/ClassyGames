package com.charlesmadere.android.classygames.models.games.checkers;


import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.models.games.Position;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Class representing a Checkers board. This board is made up of a bunch of
 * positions. Checkers is 8 by 8, so that's 64 positions.
 */
public class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;
	private final static byte MAX_TEAM_SIZE = 12;




	/**
	 * Holds a handle to the piece that the user last moved. This is needed for
	 * multijump purposes.
	 */
	private Piece lastMovedPiece;




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
	public byte checkValidity()
	{
		byte piecesCountOpponent = 0;
		byte piecesCountPlayer = 0;

		for (byte x = 0; x < lengthHorizontal; ++x)
		{
			for (byte y = 0; y < lengthVertical; ++y)
			{
				final Coordinate coordinate = new Coordinate(x, y);

				final Position position = getPosition(coordinate);
				final Piece piece = (Piece) position.getPiece();

				if (piece != null && coordinate.areBothEitherEvenOrOdd())
				// check to see if this piece is in an invalid position on the
				// board
				{
					return BOARD_INVALID;
				}

				if (piece != null)
				// count the size of the teams
				{
					if (piece.isTeamOpponent())
					{
						++piecesCountOpponent;
					}
					else if (piece.isTeamPlayer())
					{
						++piecesCountPlayer;
					}
				}

				if (!coordinate.areBothEitherEvenOrOdd())
				{
					if (y > 4)
					{
						if (piece == null)
						{
							return BOARD_INVALID;
						}
						else if (piece.isTeamPlayer())
						{
							return BOARD_INVALID;
						}
						else if (piece.isTypeKing())
						{
							return BOARD_INVALID;
						}
					}
					else if (y == 2)
					{
						if (piece != null)
						{
							if (piece.isTeamOpponent())
							{
								return BOARD_INVALID;
							}
							else if (piece.isTypeKing())
							{
								return BOARD_INVALID;
							}
						}
					}
					else if (y < 2)
					{
						if (piece == null)
						{
							return BOARD_INVALID;
						}
						else if (piece.isTeamOpponent())
						{
							return BOARD_INVALID;
						}
						else if (piece.isTypeKing())
						{
							return BOARD_INVALID;
						}
					}
				}

				if (y == 7 && x % 2 == 0 || y == 6 && x % 2 != 0 || y == 5 && x % 2 == 0 || y == 1 && x % 2 == 0 || y == 0 && x % 2 != 0)
				// only the first row should have a piece moved from a valid
				// spot at first
				{
					if (piece == null)
					{
						return BOARD_INVALID;
					}
				}

				if (y == 3)
				// make sure that the first move came from a valid place
				{
					if (piece != null)
					{
						if (x == 0)
						// check that farthest left piece moved to this position
						{
							if (getPosition(x + 1, y - 1).hasPiece())
							{
								return BOARD_INVALID;
							}
						}
						else if (x % 2 == 0)
						// check the rest of the pieces for a valid first turn
						// move
						{
							if (getPosition(x - 1, y - 1).hasPiece() && getPosition(x + 1, y - 1).hasPiece())
							{
								return BOARD_INVALID;
							}
						}
					}
				}
			}
		}

		if (piecesCountOpponent != MAX_TEAM_SIZE || piecesCountPlayer != MAX_TEAM_SIZE)
		{
			return BOARD_INVALID;
		}

		return BOARD_NEW_GAME;
	}


	@Override
	public byte checkValidity(final JSONObject boardJSON)
	{
		try
		{
			final Board board = new Board(boardJSON);
			byte piecesCountOpponent = 0;
			byte piecesCountPlayer = 0;

			for (byte x = 0; x < lengthHorizontal; ++x)
			{
				for (byte y = 0; y < lengthVertical; ++y)
				{
					final Coordinate coordinate = new Coordinate(x, y);
					final Position positionNew = board.getPosition(coordinate);
					final Piece pieceNew = (Piece) positionNew.getPiece();

					if (coordinate.areBothEitherEvenOrOdd())
					// check to see if this piece is in an invalid position on
					// the board
					{
						if (pieceNew != null)
						{
							return BOARD_INVALID;
						}
					}

					if (pieceNew != null)
					// count the size of the teams
					{
						if (pieceNew.isTeamOpponent())
						{
							++piecesCountOpponent;
						}
						else if (pieceNew.isTeamPlayer())
						{
							++piecesCountPlayer;
						}
					}

					if (piecesCountOpponent > MAX_TEAM_SIZE || piecesCountPlayer > MAX_TEAM_SIZE)
					{
						return BOARD_INVALID;
					}
				}
			}

			if (piecesCountOpponent == 0)
			{
				return BOARD_WIN;
			}
		}
		catch (final JSONException e)
		{
			return BOARD_INVALID;
		}

		return BOARD_NEW_MOVE;
	}


	@Override
	protected void initializeDefaultBoard()
	{
		// player team
		getPosition(1, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(3, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(5, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(7, 0).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(0, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(4, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(6, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(3, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(5, 2).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(7, 2).setPiece(new Piece(Piece.TEAM_PLAYER));

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




	@Override
	public boolean move(final Position previous, final Position current)
	{
		boolean isMoveValid = false;

		if (isBoardLocked)
		// If the board is locked, then that means that no piece is allowed to
		// move around.
		{
			return false;
		}
		else if (current.hasPiece())
		// If the second position that the user selected has a piece on it then
		// this is an invalid move.
		{
			return false;
		}
		else if (previous.hasPiece() && previous.getPiece().isTeamOpponent())
		// If the first position that the user selected has an opponent's piece
		// on it then this is an invalid move.
		{
			return false;
		}
		else if (previous.hasPiece() && previous.getPiece().isTeamPlayer() && !current.hasPiece())
		// This is the one way that a checkers move can actually be valid. The
		// first selected position must have a friendly piece and the second
		// position not have a piece.
		{
			final Piece piece = (Piece) previous.getPiece();

			// The if statements that we're about to see perform calculations
			// on the player's attempted move. We're trying to see how far the
			// jump is in order to travel from the previous position to the
			// current one.

			if (((previous.getCoordinate().getX() == current.getCoordinate().getX() - 1)
				|| (previous.getCoordinate().getX() == current.getCoordinate().getX() + 1))
				&& !hasMoveBeenMade)
			// Checks to see if the move from the previous position to the
			// current one has a difference of just 1 when comparing the two X
			// values. Then it also checks to make sure that a move has not yet
			// been made on the board.
			{
				switch (piece.getType())
				// Depending on the type of Piece that this is (Normal or King)
				// we will perform various move calculations.
				{
					case Piece.TYPE_KING:
						if (previous.getCoordinate().getY() == current.getCoordinate().getY() + 1)
						// Checks to see if the move from the previous position
						// to the current one has a difference of just 1 when
						// comparing the two Y values. Note the + 1 at the end
						// of the if statement and the lack of a break at the
						// end of this case statement; a King piece can move
						// either up or down on the game board.
						{
							isMoveValid = true;
							isBoardLocked = true;
						}

					case Piece.TYPE_NORMAL:
						if (previous.getCoordinate().getY() == current.getCoordinate().getY() - 1)
						// Checks to see if the move from the previous position
						// to the current one has a difference of just 1 when
						// comparing the two Y values. Note the - 1 at the end
						// of the if statement; a Normal piece can only move up
						// on the game board.
						{
							isMoveValid = true;
							isBoardLocked = true;
						}
				}
			}
			else if ((previous.getCoordinate().getX() == current.getCoordinate().getX() - 2)
				|| (previous.getCoordinate().getX() == current.getCoordinate().getX() + 2))
			// Checks to see if the move from the previous position to the
			// current one has a difference of 2 when comparing the two X
			// values. Note that here, unlike in the if statement directly
			// above this one, we're not checking the state of the
			// hasMoveBeenMade variable. This is for double jumping purposes.
			// Also note how a difference of 2 is being checked this time. This
			// should be thought of as the jump condition. In order for a
			// checkers piece to jump over another, their X coordinate must
			// change by 2.
			{
				boolean isJumpValid = false;

				switch (piece.getType())
				// Depending on the type of Piece that this is (Normal or King)
				// we will perform various move calculations.
				{
					case Piece.TYPE_KING:
						if (previous.getCoordinate().getY() == current.getCoordinate().getY() + 2)
						//
						{
							isJumpValid = true;
						}

					case Piece.TYPE_NORMAL:
						if (previous.getCoordinate().getY() == current.getCoordinate().getY() - 2)
						//
						{
							isJumpValid = true;
						}
				}

				if (isJumpValid)
				// If the jump was valid then we still need to make sure that
				// the actual move was valid. A valid move is one where the
				// checkers piece has actually passed over the top of an
				// opponent's checkers piece.
				{
					// Calculate the X and Y coordinates for the position that
					// is between the previous position and the current
					// position. Note that we're using Math.abs(), that method
					// insures that the resulting value is definitely going to
					// be a positive number.
					final byte middleX = (byte) Math.abs((previous.getCoordinate().getX() + current.getCoordinate().getX()) / 2);
					final byte middleY = (byte) Math.abs((previous.getCoordinate().getY() + current.getCoordinate().getY()) / 2);

					// This is the position on the checkers board that is
					// between the previous position and the current position.
					final Position middlePosition = getPosition(middleX, middleY);

					if (middlePosition.hasPiece() && middlePosition.getPiece().isTeamOpponent())
					// Check to see that this middle position has a piece and
					// that the piece is an opponent's.
					{
						if (!hasMoveBeenMade)
						// Here we check to see if a move has already been
						// made. This is for double jumping purposes.
						{
							lastMovedPiece = piece;
							middlePosition.getPiece().kill();
							isMoveValid = true;
						}
						else if (lastMovedPiece == piece)
						// A move has already been made and as such we need to
						// verify that the piece that is being moved right now
						// is the same piece that moved last time.
						{
							middlePosition.getPiece().kill();
							isMoveValid = true;
						}
					}
				}
			}

			if (isMoveValid)
			// This jump move has been calculated as being valid. That means
			// that the user jumped one of the opposing team's pieces. Let's
			// kill that piece and set the piece that the user jumped with as
			// the last moved piece (this is for double jump purposes).
			{
				current.setPiece(new Piece(piece));
				previous.removePiece();

				// Grab a reference to the current position's piece. This is
				// the last moved piece.
				lastMovedPiece = (Piece) current.getPiece();

				if (current.getCoordinate().getY() == lengthVertical - 1)
				// Check to see if the position that we just now landed on is
				// the top of the board. In checkers, if a piece lands at the
				// top of the board then that means that the piece becomes a
				// King.
				{
					((Piece) current.getPiece()).ascendToKing();
				}
			}
		}

		if (isMoveValid)
		// The move has been calculated as being valid. Mark that a move has
		// been made.
		{
			hasMoveBeenMade = true;
		}

		return isMoveValid;
	}


	@Override
	public void performGameSpecificJSONChecks() throws JSONException
	{

	}


	@Override
	protected void resetBoard()
	{
		lastMovedPiece = null;
	}


}
