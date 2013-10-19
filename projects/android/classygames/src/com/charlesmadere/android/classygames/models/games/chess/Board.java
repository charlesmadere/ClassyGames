package com.charlesmadere.android.classygames.models.games.chess;


import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.models.games.Position;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Class representing a Chess board. This board is made up of a bunch of
 * positions. Chess is 8 by 8, so that's 64 positions.
 */
public final class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;


	public final static byte BOARD_NORMAL = 0;
	public final static byte BOARD_CHECK = 1;
	public final static byte BOARD_CHECKMATE = 2;


	/**
	 * The castle move can only be performed once per game. The value of this
	 * variable will be true if it has already been used. Otherwise, this will
	 * only be set when the user actually does the castle technique.
	 */
	private boolean hasCastled;




	/**
	 * Creates a Chess board object.
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
	 * If a glitch or something happened while trying to create this JSONObject
	 * then a JSONException will be thrown.
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
		return (byte) 0;
	}


	@Override
	public byte checkValidity(final GenericBoard board)
	{
		return (byte) 0;
	}


	@Override
	protected void initializeDefaultBoard()
	{
		// player team
		getPosition(0, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_ROOK));
		getPosition(1, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_KNIGHT));
		getPosition(2, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_BISHOP));
		getPosition(3, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_QUEEN));
		getPosition(4, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_KING));
		getPosition(5, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_BISHOP));
		getPosition(6, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_KNIGHT));
		getPosition(7, 0).setPiece(new Piece(Piece.TEAM_PLAYER, Piece.TYPE_ROOK));
		getPosition(0, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(1, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(2, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(3, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(4, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(5, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(6, 1).setPiece(new Piece(Piece.TEAM_PLAYER));
		getPosition(7, 1).setPiece(new Piece(Piece.TEAM_PLAYER));

		// opponent team
		getPosition(0, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_ROOK));
		getPosition(1, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_KNIGHT));
		getPosition(2, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_BISHOP));
		getPosition(3, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_QUEEN));
		getPosition(4, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_KING));
		getPosition(5, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_BISHOP));
		getPosition(6, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_KNIGHT));
		getPosition(7, 7).setPiece(new Piece(Piece.TEAM_OPPONENT, Piece.TYPE_ROOK));
		getPosition(0, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(1, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(2, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(3, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(4, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(5, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(6, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
		getPosition(7, 6).setPiece(new Piece(Piece.TEAM_OPPONENT));
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
		else if (previous.hasPiece() && previous.getPiece().isTeamOpponent())
		// If the first position that the user selected has an opponent's piece
		// on it then this is an invalid move.
		{
			return false;
		}
		else if (previous.hasPiece() && previous.getPiece().isTeamPlayer())
		// This is the one way that a chess move can actually be valid.
		{
			final Piece piece = (Piece) previous.getPiece();
			final Coordinate start = previous.getCoordinate();
			final int startX = (int) start.getX();
			final int startY = (int) start.getY();
			final Coordinate end = current.getCoordinate();
			final int endX = (int) end.getX();
			final int endY = (int) end.getY();

			switch (piece.getType())
			{
				case Piece.TYPE_BISHOP:
					if (!isMovingThroughPieces(previous, current) &&
						(Math.abs(startX - endX) == Math.abs(startY - endY)))
					{
						if (current.hasPiece())
						{
							final Piece p = (Piece) current.getPiece();

							if (p.isTeamOpponent() && !p.isTypeKing())
							{
								current.removePiece();
								current.setPiece(new Piece(piece));
								previous.removePiece();
								isMoveValid = true;
							}
							else
							{
								return false;
							}
						}
					}
					break;

				case Piece.TYPE_KING:
					if (!isMovingThroughPieces(previous, current) &&
						((Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 1)
						|| (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 0)
						|| (Math.abs(startX - endX) == 0 && Math.abs(startY - endY) == 1)))
					{
						if (current.hasPiece())
						{
							final Piece p = (Piece) current.getPiece();

							if (p.isTeamOpponent() && !p.isTypeKing())
							{
								current.removePiece();
								current.setPiece(new Piece(piece));
								previous.removePiece();
								isMoveValid = true;
							}
							else
							{
								return false;
							}
						}
					}
					break;

				case Piece.TYPE_KNIGHT:
					if ((Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 2)
						|| (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 1))
					{
						if (current.hasPiece())
						{
							final Piece p = (Piece) current.getPiece();

							if (p.isTeamOpponent() && !p.isTypeKing())
							{
								current.removePiece();
								current.setPiece(new Piece(piece));
								previous.removePiece();
								isMoveValid = true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							current.setPiece(new Piece(piece));
							previous.removePiece();
							isMoveValid = true;
						}
					}
					else
					{
						return false;
					}
					break;

				case Piece.TYPE_QUEEN:
					// Sorry for this kinda nasty if statement, but the Queen
					// can move in a bunch of different ways... Don't try to
					// decipher this unless you're already familiar with Chess.
					if (!isMovingThroughPieces(previous, current) &&
						((Math.abs(startX - endX) == Math.abs(startY - endY))
						// bishop logic
						|| ((Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 1)
						|| (Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 0)
						|| (Math.abs(startX - endX) == 0 && Math.abs(startY - endY) == 1))
						// king logic
						|| ((startX == endX && startY != endY) || (startX != endX && startY == endY))))
						// rook logic
					{
						if (current.hasPiece())
						{
							final Piece p = (Piece) current.getPiece();

							if (p.isTeamOpponent() && !p.isTypeKing())
							{
								current.removePiece();
								current.setPiece(new Piece(piece));
								previous.removePiece();
								isMoveValid = true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							current.setPiece(new Piece(piece));
							previous.removePiece();
							isMoveValid = true;
						}
					}
					else
					{
						return false;
					}
					break;

				case Piece.TYPE_PAWN:
					if (!isMovingThroughPieces(previous, current) &&
						((startY == 1 && Math.abs(startY - endY) == 2) || Math.abs(startY - endY) == 1))
					{
						if (startX == endX)
						{
							if (current.hasPiece())
							{
								final Piece p = (Piece) current.getPiece();

								if (p.isTeamOpponent() && !p.isTypeKing() && Math.abs(startX - endX) == 1)
								{
									current.removePiece();
								}
							}

							current.setPiece(new Piece(piece));
							previous.removePiece();
							isMoveValid = true;
						}
					}
					break;

				case Piece.TYPE_ROOK:
					if (!isMovingThroughPieces(previous, current) &&
						(startX == endX && startY != endY) || (startX != endX && startY == endY))
					{
						if (current.hasPiece())
						{
							final Piece p = (Piece) current.getPiece();

							if (p.isTeamOpponent() && !p.isTypeKing())
							{
								current.removePiece();
								current.setPiece(new Piece(piece));
								previous.removePiece();
								isMoveValid = true;
							}
							else
							{
								return false;
							}
						}
						else
						{
							current.setPiece(new Piece(piece));
							previous.removePiece();
							isMoveValid = true;
						}
					}
					else
					{
						return false;
					}
					break;
			}

			if (isMoveValid)
			{
				hasMoveBeenMade = true;
				isBoardLocked = true;
			}
		}

		return isMoveValid;
	}


	@Override
	public void performGameSpecificJSONChecks() throws JSONException
	{
		// TODO
		// find the has_castled key-value pair in the boardJSON object
	}


	@Override
	protected void resetBoard()
	{

	}


	/**
	 * Performs a check to see if the user can perform a castle. Note this move
	 * can only be performed once per game. More on the castle technique can be
	 * found here: https://en.wikipedia.org/wiki/Chess#Castling
	 *
	 * @return
	 * Returns true if the user can perform a castle.
	 */
	public boolean canCastle()
	{
		if (hasCastled)
		{
			return false;
		}
		else
		{
			// TODO
			// Check to see if the castle technique can be used.

			return false;
		}
	}


	/**
	 * Performs a series of checks on the game board to see if the opponent is
	 * in check or checkmate. https://en.wikipedia.org/wiki/Check_(chess)
	 * https://en.wikipedia.org/wiki/Checkmate
	 *
	 * @return
	 * Returns one of this class's BOARD_* bytes depending on the determined
	 * board condition.
	 */
	public int isBoardInCheckOrCheckmate()
	{
		// TODO
		return BOARD_NORMAL;
	}


	/**
	 * Checks to see if the Piece at Position previous has to move through any
	 * other pieces on the Chess board in order to arrive at Position current.
	 * This algorithm does not check for the existence of a Piece at Position
	 * current.
	 *
	 * @param previous
	 * The Position that the Piece is trying to move from. This absolutely can
	 * not be null and must also have a Piece object associated with it.
	 *
	 * @param current
	 * The Position on the game board that the given Piece is attempting to
	 * travel to. This absolutely can not be null! It's fine if this Position
	 * does or does not have a Piece object associated with it.
	 *
	 * @return
	 * Returns true if the Piece at Position previous has to move through other
	 * pieces on the game board to arrive at Position current.
	 */
	private boolean isMovingThroughPieces(final Position previous, final Position current)
	{
		// TODO
		// this method is still under major construction!

		final Piece piece = (Piece) previous.getPiece();
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		switch (piece.getType())
		{
			case Piece.TYPE_BISHOP:
				break;

			case Piece.TYPE_KING:
				break;

			case Piece.TYPE_PAWN:
				break;

			case Piece.TYPE_QUEEN:
				break;

			case Piece.TYPE_ROOK:
				if (startX == endX)
				// rook is moving vertically
				{
					if (startY < endY)
					// rook is moving up
					{
						int currentY = startY;

						while (++currentY < endY)
						{
							final Position p = getPosition(startX, currentY);

							if (p.hasPiece())
							{
								return true;
							}
						}
					}
					else if (startY > endY)
					// rook is moving down
					{
						int currentY = startY;

						while (--currentY > endY)
						{
							final Position p = getPosition(startX, currentY);

							if (p.hasPiece())
							{
								return true;
							}
						}
					}
				}
				else if (startY == endY)
				// rook is moving horizontally
				{
					if (startX < endX)
					// rook is moving right
					{
						int currentX = startX;

						while (++currentX < endY)
						{
							final Position p = getPosition(currentX, startY);

							if (p.hasPiece())
							{
								return true;
							}
						}
					}
					else if (startX > endX)
					// rook is moving left
					{
						int currentX = startX;

						while (--currentX > endY)
						{
							final Position p = getPosition(currentX, startY);

							if (p.hasPiece())
							{
								return true;
							}
						}
					}
				}
				break;
		}

		return false;
	}


}
