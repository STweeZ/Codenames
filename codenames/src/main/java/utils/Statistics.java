package utils;

import java.sql.SQLException;
import java.util.List;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.persistance.GameTDG;
import fr.univartois.ili.jai.persistance.TDGRegistry;

public final class Statistics {

	private static final GameTDG gtdg = TDGRegistry.findTDG(Game.class);

	public static void insertNewGame(Game game) throws SQLException {
		gtdg.insert(game);
	}

	public static List<List<List<String>>> getStatisticsGames() throws SQLException {
		return gtdg.getStatisticsGames();
	}

	public static List<List<Object>> getStatisticsTeams() throws SQLException {
		return gtdg.getStatisticsTeams();
	}

	public static List<List<Object>> getStatisticsPlayers() throws SQLException {
		return gtdg.getStatisticsPlayers();
	}
}
