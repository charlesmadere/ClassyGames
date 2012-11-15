package edu.selu.android.classygames.games.checkers;


import java.util.List;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.selu.android.classygames.games.Coordinate;
import edu.selu.android.classygames.games.GenericBoard;


public class Board extends GenericBoard
{


	private final static byte LENGTH_HORIZONTAL = 8;
	private final static byte LENGTH_VERTICAL = 8;


	public Board()
	{
		super();

		maxTeamSize = 12;
	}


	/**
	 * Creates a Board object from JSON data.
	 * 
	 * @param boardJSONData
	 * A String of JSON data that represents the Board object.
	 */
	@SuppressWarnings("unchecked")
	public Board(final String boardJSONData)
	// TODO
	{
		this();

		try
		{
			final Map<String, Object> data = (Map<String, Object>) new JSONParser().parse(boardJSONData);
			final Map<String, Object> board = (Map<String, Object>) data.get("board");
			final List<Object> teams = (List<Object>) board.get("teams");

			final int teamsSize = teams.size();
			for (byte i = 0; i < teamsSize; ++i)
			{
				parseTeamData((List<Object>) teams.get(i));
			}
		}
		catch (final ParseException e)
		{

		}
	}


	@Override
	public boolean checkValidity()
	// TODO
	{
		

		return true;
	}


	@Override
	public boolean checkValidity(final String boardJSONData)
	// TODO
	{
		final Board boardNew = new Board(boardJSONData);

		return true;
	}


	@Override
	public Position getPosition(final byte x, final byte y)
	{
		if ((x >= 0 && x < LENGTH_HORIZONTAL) && (y >= 0 && y < LENGTH_VERTICAL))
		{
			return (Position) positions[x][y];
		}
		else
		{
			return null;
		}
	}


	@Override
	protected void initializeBoard()
	{
		positions = new Position[LENGTH_HORIZONTAL][LENGTH_VERTICAL];

		for (byte i = 0; i < LENGTH_HORIZONTAL; ++i)
		{
			for (byte j = 0; j < LENGTH_VERTICAL; ++j)
			{
				positions[i][j] = new Position();
			}
		}
	}


	@SuppressWarnings("unchecked")
	private void parseTeamData(final List<Object> team)
	{
		final byte teamSize = (byte) team.size();

		for (byte i = 0, currentTeamSize = 0; i < teamSize && currentTeamSize < maxTeamSize; ++i, ++currentTeamSize)
		{
			final Map<String, Object> piece = (Map<String, Object>) team.get(i);
			final byte pieceType = Long.valueOf((Long) piece.get("type")).byteValue();
			final List<Byte> unparsedCoordinate = (List<Byte>) piece.get("coordinate");

			final Coordinate coordinate = new Coordinate(unparsedCoordinate.get(0), unparsedCoordinate.get(1));
			positions[coordinate.getX()][coordinate.getY()] = new Position(coordinate, i, pieceType);
		}
	}


}
