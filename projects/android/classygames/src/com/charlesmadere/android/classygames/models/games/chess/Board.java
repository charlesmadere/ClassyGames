package com.charlesmadere.android.classygames.models.games.chess;


import com.charlesmadere.android.classygames.models.games.Coordinate;
import com.charlesmadere.android.classygames.models.games.GenericBoard;
import com.charlesmadere.android.classygames.models.games.GenericPiece;
import com.charlesmadere.android.classygames.models.games.Position;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;


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

			switch (piece.getType())
			{
				case Piece.TYPE_BISHOP:
					isMoveValid = isMoveValidBishop(previous, current);
					break;

				case Piece.TYPE_KING:
					isMoveValid = isMoveValidKing(previous, current);
					break;

				case Piece.TYPE_KNIGHT:
					isMoveValid = isMoveValidKnight(previous, current);
					break;

				case Piece.TYPE_PAWN:
					isMoveValid = isMoveValidPawn(previous, current);
					break;

				case Piece.TYPE_QUEEN:
					isMoveValid = isMoveValidQueen(previous, current);
					break;

				case Piece.TYPE_ROOK:
					isMoveValid = isMoveValidRook(previous, current);
					break;
			}

			if (isMoveValid)
			{
				current.removePiece();
				current.setPiece(new Piece(piece));
				previous.removePiece();

				hasMoveBeenMade = true;
				isBoardLocked = true;
			}
		}

		return isMoveValid;
	}


	@Override
	protected void performGameSpecificJSONChecks(final JSONObject boardJSON) throws JSONException
	{
		hasCastled = boardJSON.getBoolean("has_castled");
	}


	@Override
	protected void resetBoard()
	{

	}


	private void addPositionToSurroundingPositionsIfEmpty(final LinkedList<Position> surroundingPositions,
		final int x, final int y)
	{
		if (isPositionValid(x, y))
		{
			final Position position = getPosition(x, y);

			if (!position.hasPiece() || position.getPiece().isTeamOpponent())
			{
				surroundingPositions.add(position);
			}
		}
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
	 * Searches the game board for the opponent's King piece and then returns
	 * the Position that he's on. Returns null if the King piece cannot be
	 * found... but this should never happen.
	 */
	private Position findOpponentKing()
	{
		for (byte x = 0; x < getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < getLengthVertical(); ++y)
			{
				final Position position = getPosition(x, y);

				if (position.hasPiece())
				{
					final Piece piece = (Piece) position.getPiece();

					if (piece.isTeamOpponent() && piece.isTypeKing())
					{
						return position;
					}
				}
			}
		}

		return null;
	}


	/**
	 * Creates a LinkedList of all Board positions that contain an opponent's
	 * piece on them. This method will never return null! (But it may return an
	 * empty LinkedList.)
	 */
	private LinkedList<Position> findOpponentsPieces()
	{
		return findTeamsPieces(GenericPiece.TEAM_OPPONENT);
	}


	/**
	 * Creates a LinkedList of all Board positions that contain a player's
	 * piece on them. This method will never return null! (But it may return an
	 * empty LinkedList.)
	 */
	private LinkedList<Position> findPlayersPieces()
	{
		return findTeamsPieces(GenericPiece.TEAM_PLAYER);
	}


	/**
	 * Creates a LinkedList of all Board positions
	 *
	 * @param whichTeam
	 * The team to build the LinkedList of pieces for.
	 *
	 * @return
	 * Returns a LinkedList of all of the Pieces belonging to the given team.
	 * This variable is never null! (But it may be empty.)
	 */
	private LinkedList<Position> findTeamsPieces(final byte whichTeam)
	{
		final LinkedList<Position> teamsPieces = new LinkedList<Position>();

		for (byte x = 0; x < getLengthHorizontal(); ++x)
		{
			for (byte y = 0; y < getLengthVertical(); ++y)
			{
				final Position position = getPosition(x, y);

				if (position.hasPiece())
				{
					final Piece piece = (Piece) position.getPiece();

					if (piece.isTeam(whichTeam))
					{
						teamsPieces.add(position);
					}
				}
			}
		}

		return teamsPieces;
	}


	/**
	 * Builds a LinkedList of Positions surrounding the given Position that are
	 * either empty or have an enemy Piece on them.
	 *
	 * @return
	 * Returns a LinkedList of Positions that fulfill the above requirement,
	 * will never return null (but could return an empty LinkedList).
	 */
	private LinkedList<Position> findSurroundingPositions(final Position positionToCheck)
	{
		final LinkedList<Position> surroundingPositions = new LinkedList<Position>();
		final Coordinate coordinate = positionToCheck.getCoordinate();
		final int originX = (int) coordinate.getX();
		final int originY = (int) coordinate.getY();

		// top right
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX + 1, originY + 1);

		// bottom right
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX + 1, originY - 1);

		// top left
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX - 1, originY + 1);

		// bottom left
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX - 1, originY - 1);

		// right
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX + 1, originY);

		// left
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX - 1, originY);

		// top
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX, originY + 1);

		// bottom
		addPositionToSurroundingPositionsIfEmpty(surroundingPositions, originX, originY - 1);

		return surroundingPositions;
	}


	/**
	 * Performs a series of checks on the game board to see if the opponent is
	 * in check or checkmate. https://en.wikipedia.org/wiki/Check_(chess)
	 * https://en.wikipedia.org/wiki/Checkmate
	 *
	 * @return
	 * Returns one of this class's BOARD_* bytes depending on the determined
	 * board condition. So there are three different values that could be
	 * returned: BOARD_NORMAL, BOARD_CHECK, or BOARD_CHECKMATE.
	 */
	public byte isBoardInCheckOrCheckmate()
	{
		byte boardStatus = BOARD_NORMAL;

		final Position opponentKingPosition = findOpponentKing();
		final LinkedList<Position> playersPositions = findPlayersPieces();
		final LinkedList<Position> playerPositionsThatCanHit = new LinkedList<Position>();

		for (final Position position : playersPositions)
		{
			boolean canHit = false;

			switch (position.getPiece().getType())
			{
				case Piece.TYPE_BISHOP:
					canHit = isMoveValidBishop(position, opponentKingPosition);
					break;

				case Piece.TYPE_KING:
					canHit = isMoveValidKing(position, opponentKingPosition);
					break;

				case Piece.TYPE_KNIGHT:
					canHit = isMoveValidKnight(position, opponentKingPosition);
					break;

				case Piece.TYPE_PAWN:
					canHit = isMoveValidPawn(position, opponentKingPosition);
					break;

				case Piece.TYPE_QUEEN:
					canHit = isMoveValidQueen(position, opponentKingPosition);
					break;

				case Piece.TYPE_ROOK:
					canHit = isMoveValidRook(position, opponentKingPosition);
					break;
			}

			if (canHit)
			{
				playerPositionsThatCanHit.add(position);
			}
		}

		if (playerPositionsThatCanHit.isEmpty())
		{
			boardStatus = BOARD_NORMAL;
		}
		else
		{
			final LinkedList<Position> opponentsPositions = findOpponentsPieces();

			for (final Position position : opponentsPositions)
			{
				for (int i = 0; i < playersPositions.size(); ++i)
				{
					final Position playersPosition = playersPositions.get(i);
					boolean canHit = false;

					switch (position.getPiece().getType())
					{
						case Piece.TYPE_BISHOP:
							canHit = isMoveValidBishop(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;

						case Piece.TYPE_KING:
							canHit = isMoveValidKing(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;

						case Piece.TYPE_KNIGHT:
							canHit = isMoveValidKnight(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;

						case Piece.TYPE_PAWN:
							canHit = isMoveValidPawn(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;

						case Piece.TYPE_QUEEN:
							canHit = isMoveValidQueen(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;

						case Piece.TYPE_ROOK:
							canHit = isMoveValidRook(position, playersPosition, GenericPiece.TEAM_PLAYER);
							break;
					}

					if (canHit)
					{
						playersPositions.remove(i);
					}
				}
			}
		}

		return boardStatus;
	}


	private boolean isMoveValidBishop(final Position previous, final Position current)
	{
		return isMoveValidBishop(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidBishop(final Position previous, final Position current, final byte whichTeam)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMoveValid = false;

		if (!isMovingThroughPiecesBishop(previous, current) &&
			(Math.abs(startX - endX) == Math.abs(startY - endY)))
		{
			if (current.hasPiece())
			{
				final Piece piece = (Piece) current.getPiece();

				if (piece.isTeam(whichTeam) && !piece.isTypeKing())
				{
					isMoveValid = true;
				}
			}
			else
			{
				isMoveValid = true;
			}
		}

		return isMoveValid;
	}


	private boolean isMoveValidKing(final Position previous, final Position current)
	{
		return isMoveValidKing(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidKing(final Position previous, final Position current, final byte whichTeam)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMoveValid = false;

		if ((Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 1) ||
			(Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 0) ||
			(Math.abs(startX - endX) == 0 && Math.abs(startY - endY) == 1))
		{
			if (current.hasPiece())
			{
				final Piece piece = (Piece) current.getPiece();

				if (piece.isTeam(whichTeam) && !piece.isTypeKing())
				{
					isMoveValid = true;
				}
			}
			else
			{
				isMoveValid = true;
			}
		}

		return isMoveValid;
	}


	private boolean isMoveValidKnight(final Position previous, final Position current)
	{
		return isMoveValidKnight(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidKnight(final Position previous, final Position current, final byte whichTeam)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMoveValid = false;

		if ((Math.abs(startX - endX) == 1 && Math.abs(startY - endY) == 2)
			|| (Math.abs(startX - endX) == 2 && Math.abs(startY - endY) == 1))
		{
			if (current.hasPiece())
			{
				final Piece piece = (Piece) current.getPiece();

				if (piece.isTeam(whichTeam) && !piece.isTypeKing())
				{
					isMoveValid = true;
				}
			}
			else
			{
				isMoveValid = true;
			}
		}

		return isMoveValid;
	}


	private boolean isMoveValidPawn(final Position previous, final Position current)
	{
		return isMoveValidPawn(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidPawn(final Position previous, final Position current, final byte whichTeam)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMoveValid = false;

		if (endY > startY)
		{
			if (startX == endX && !current.hasPiece())
			{
				if (endY - startY == 1)
				{
					isMoveValid = true;
				}
				else if (startY == 1 && endY - startY == 2 && !isMovingThroughPiecesPawn(previous, current))
				{
					isMoveValid = true;
				}
			}
			else if (Math.abs(endX - startX) == 1 && endY - startY == 1)
			{
				if (current.hasPiece())
				{
					final Piece p = (Piece) current.getPiece();

					if (p.isTeam(whichTeam) && !p.isTypeKing())
					{
						isMoveValid = true;
					}
				}
			}
		}

		return isMoveValid;
	}


	private boolean isMoveValidQueen(final Position previous, final Position current)
	{
		return isMoveValidQueen(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidQueen(final Position previous, final Position current, final byte whichTeam)
	{
		return isMoveValidBishop(previous, current, whichTeam) || isMoveValidRook(previous, current, whichTeam);
	}


	private boolean isMoveValidRook(final Position previous, final Position current)
	{
		return isMoveValidRook(previous, current, GenericPiece.TEAM_OPPONENT);
	}


	private boolean isMoveValidRook(final Position previous, final Position current, final byte whichTeam)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMoveValid = false;

		if (!isMovingThroughPiecesRook(previous, current) &&
			(startX == endX && startY != endY) || (startX != endX && startY == endY))
		{
			if (current.hasPiece())
			{
				final Piece piece = (Piece) current.getPiece();

				if (piece.isTeam(whichTeam) && !piece.isTypeKing())
				{
					isMoveValid = true;
				}
			}
			else
			{
				isMoveValid = true;
			}
		}

		return isMoveValid;
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
	private boolean isMovingThroughPiecesBishop(final Position previous, final Position current)
	{
		final Coordinate start = previous.getCoordinate();
		final byte startX = start.getX();
		final byte startY = start.getY();
		final Coordinate end = current.getCoordinate();
		final byte endX = end.getX();
		final byte endY = end.getY();

		if (endX > startX)
		// bishop is moving right
		{
			byte currentX = startX;
			byte currentY = startY;
			Position p;

			if (endY > startY)
			// bishop is moving right-up
			{
				while (++currentX < endX && ++currentY < endY)
				{
					p = getPosition(currentX, currentY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
			else if (startY > endY)
			// bishop is moving right-down
			{
				while (++currentX < endX && --currentY > endY)
				{
					p = getPosition(currentX, currentY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
		}
		else if (startX > endX)
		// bishop is moving left
		{
			byte currentX = startX;
			byte currentY = startY;
			Position p;

			if (endY > startY)
			// bishop is moving left-up
			{
				while (--currentX > endX && ++currentY < endY)
				{
					p = getPosition(currentX, currentY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
			else if (startY > endY)
			// bishop is moving left-down
			{
				while (--currentX > endX && --currentY > endY)
				{
					p = getPosition(currentX, currentY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
		}

		return false;
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
	private boolean isMovingThroughPiecesPawn(final Position previous, final Position current)
	{
		final Coordinate start = previous.getCoordinate();
		final int startX = (int) start.getX();
		final int startY = (int) start.getY();
		final Coordinate end = current.getCoordinate();
		final int endX = (int) end.getX();
		final int endY = (int) end.getY();

		boolean isMovingThroughPieces = false;

		if (startX == endX && endY - startY == 2)
		{
			final Position position = getPosition(startX, endY - 1);

			if (position.hasPiece())
			{
				isMovingThroughPieces = true;
			}
		}

		return isMovingThroughPieces;
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
	private boolean isMovingThroughPiecesRook(final Position previous, final Position current)
	{
		final Coordinate start = previous.getCoordinate();
		final byte startX = start.getX();
		final byte startY = start.getY();
		final Coordinate end = current.getCoordinate();
		final byte endX = end.getX();
		final byte endY = end.getY();

		if (startX == endX)
		// rook is moving vertically
		{
			byte currentY = startY;
			Position p;

			if (startY < endY)
			// rook is moving up
			{
				while (++currentY < endY)
				{
					p = getPosition(startX, currentY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
			else if (startY > endY)
			// rook is moving down
			{
				while (--currentY > endY)
				{
					p = getPosition(startX, currentY);

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
			byte currentX = startX;
			Position p;

			if (startX < endX)
			// rook is moving right
			{
				while (++currentX < endX)
				{
					p = getPosition(currentX, startY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
			else if (startX > endX)
			// rook is moving left
			{
				while (--currentX > endX)
				{
					p = getPosition(currentX, startY);

					if (p.hasPiece())
					{
						return true;
					}
				}
			}
		}

		return false;
	}


}
