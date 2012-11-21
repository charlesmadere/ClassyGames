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


	@SuppressWarnings("unchecked")
	public static String flipTeams(final String board)
	{
		String flippedBoard = null;

		try
		{
			final Map<String, Object> data = (Map<String, Object>) new JSONParser().parse(board);
			final Map<String, Object> boardData = (Map<String, Object>) data.get("board");
			final List<Object> teamData = (List<Object>) boardData.get("teams");

			// flip the teams
			final Object team0 = teamData.get(0);
			final Object team1 = teamData.get(1);
			teamData.set(0, team1);
			teamData.set(1, team0);

			final byte teamsSize = (byte) teamData.size();

			for (byte i = 0; i < teamsSize; ++i)
			// loop through each team
			{
				final List<Object> team = (List<Object>) teamData.get(i);;
				final byte teamSize = (byte) team.size();

				for (byte j = 0; j < teamSize; ++j)
				// flip the team's piece's coordinates
				{
					final Map<String, Object> piece = (Map<String, Object>) team.get(j);
					final List<Long> coordinate = (List<Long>) piece.get("coordinate");

					Byte x = coordinate.get(0).byteValue();
					Byte y = coordinate.get(1).byteValue();

					x = Byte.valueOf((byte) (LENGTH_HORIZONTAL - 1 - x));
					y = Byte.valueOf((byte) (LENGTH_VERTICAL - 1 - y));

					coordinate.set(0, x.longValue());
					coordinate.set(1, y.longValue());
				}

				teamData.set(i, team);
			}

			flippedBoard = data.toString();
		}
		catch (final ParseException e)
		{

		}

		return flippedBoard;
	}


}
