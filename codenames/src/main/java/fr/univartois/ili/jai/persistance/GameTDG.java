package fr.univartois.ili.jai.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Team;

public class GameTDG extends AbstractTDG<Game> {

    private static final String CREATE = "CREATE TABLE Game (ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,ID_BLUE_TEAM BIGINT NOT NULL REFERENCES Team(ID) ON DELETE CASCADE,ID_RED_TEAM BIGINT NOT NULL REFERENCES Team(ID) ON DELETE CASCADE,ID_WINNER_TEAM BIGINT NOT NULL REFERENCES Team(ID) ON DELETE CASCADE, CHECK (ID_BLUE_TEAM != ID_RED_TEAM), CHECK ((ID_WINNER_TEAM = ID_BLUE_TEAM) or (ID_WINNER_TEAM = ID_RED_TEAM)))";
    private static final String DROP = "DROP TABLE Game";

    private static final String INSERT = "INSERT INTO Game(ID_BLUE_TEAM,ID_RED_TEAM,ID_WINNER_TEAM) VALUES (?,?,?) ";
    private static final String UPDATE = "UPDATE Game g SET g.ID_BLUE_TEAM=?,g.ID_RED_TEAM=?,g.ID_WINNER_TEAM=? WHERE g.ID = ?";
    private static final String DELETE = "DELETE FROM Game WHERE ID = ?";
    private static final String FIND_BY_ID = "SELECT ID,ID_BLUE_TEAM,ID_RED_TEAM,ID_WINNER_TEAM FROM Game WHERE ID=?";
    private static final String FIND_PLAYER_USERNAME_BY_TEAM_ID = "SELECT username FROM Player p JOIN TeamPlayer tp ON p.ID = tp.ID_PLAYER WHERE ID_TEAM = ?";
    private static final String TEAM_IDS = "SELECT ID_BLUE_TEAM, ID_RED_TEAM, ID_WINNER_TEAM FROM Game";
    private static final String BEST_TEAMS = "SELECT ID_WINNER_TEAM, COUNT(ID_WINNER_TEAM) FROM Game GROUP BY ID_WINNER_TEAM ORDER BY COUNT(ID_WINNER_TEAM) DESC";
    private static final String BEST_PLAYERS = "SELECT p.USERNAME, COUNT(p.USERNAME) FROM Game g JOIN TeamPlayer tp ON g.ID_WINNER_TEAM = tp.ID_TEAM JOIN Player p on tp.ID_PLAYER = p.ID GROUP BY p.USERNAME ORDER BY COUNT(p.USERNAME) DESC";
    private static final String WHERE = "SELECT ID FROM Game t WHERE ";

    public void createTable() throws SQLException {
        try (var stm = TDGRegistry.getConnection().createStatement()) {
            stm.executeUpdate(CREATE);
        }
    }

    public void deleteTable() throws SQLException {
        try (var stm = TDGRegistry.getConnection().createStatement()) {
            stm.executeUpdate(DROP);
        }
    }

    protected Game findByIdIntoDB(long id) throws SQLException {
        Game g = null;
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    g = new Game();
                    g.setId(rs.getLong(1));
                    var blueTeamID = rs.getLong(2);
                    if (blueTeamID != 0) {
                        g.setBlueTeam(TDGRegistry.findTDG(Team.class).findById(blueTeamID));
                    }
                    var redTeamID = rs.getLong(3);
                    if (redTeamID != 0) {
                        g.setRedTeam(TDGRegistry.findTDG(Team.class).findById(redTeamID));
                    }
                    var winnerTeamID = rs.getLong(4);
                    if (winnerTeamID != 0) {
                        g.setWinnerTeam(TDGRegistry.findTDG(Team.class).findById(winnerTeamID));
                    }
                }
            }
        }
        return g;
    }

    protected Game insertIntoDB(Game g) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
                Statement.RETURN_GENERATED_KEYS)) {
            if (g.getBlueTeam() == null) {
                pst.setNull(1, Types.BIGINT);
            } else {
                var t = g.getBlueTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                }
                pst.setLong(1, t.getId());
            }
            if (g.getRedTeam() == null) {
                pst.setNull(2, Types.BIGINT);
            } else {
                var t = g.getRedTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                }
                pst.setLong(2, t.getId());
            }
            if (g.getWinnerTeam() == null) {
                pst.setNull(3, Types.BIGINT);
            } else {
                var t = g.getWinnerTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                }
                pst.setLong(3, t.getId());
            }
            int result = pst.executeUpdate();
            assert result == 1;
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    g.setId(keys.getLong(1));
                }
            }
            return g;
        }
    }

    protected Game updateIntoDB(Game g) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
            assert findById(g.getId()).equals(g);
            if (g.getBlueTeam() == null) {
                pst.setNull(1, Types.BIGINT);
            } else {
                var t = g.getBlueTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                } else {
                    TDGRegistry.findTDG(Team.class).update(t);
                }
                pst.setLong(1, t.getId());
            }
            if (g.getRedTeam() == null) {
                pst.setNull(2, Types.BIGINT);
            } else {
                var t = g.getRedTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                } else {
                    TDGRegistry.findTDG(Team.class).update(t);
                }
                pst.setLong(2, t.getId());
            }
            if (g.getWinnerTeam() == null) {
                pst.setNull(3, Types.BIGINT);
            } else {
                var t = g.getWinnerTeam();
                if (t.getId() == 0) {
                    TDGRegistry.findTDG(Team.class).insert(t);
                } else {
                    TDGRegistry.findTDG(Team.class).update(t);
                }
                pst.setLong(3, t.getId());
            }
            pst.setLong(4, g.getId());
            int result = pst.executeUpdate();
            assert result == 1;
            return g;
        }
    }

    protected Game deleteFromDB(Game g) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
            assert findById(g.getId()).equals(g);
            pst.setLong(1, g.getId());
            int result = pst.executeUpdate();
            assert result == 1;
            return g;
        }
    }

    protected Game refreshIntoDB(Game g) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
            pst.setLong(1, g.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    g.setId(rs.getLong(1));
                    var blueTeamID = rs.getLong(2);
                    if (blueTeamID != 0) {
                        g.setBlueTeam(TDGRegistry.findTDG(Team.class).findById(blueTeamID));
                    } else {
                        g.setBlueTeam(null);
                    }
                    var redTeamID = rs.getLong(3);
                    if (redTeamID != 0) {
                        g.setRedTeam(TDGRegistry.findTDG(Team.class).findById(redTeamID));
                    } else {
                        g.setRedTeam(null);
                    }
                    var winnerTeamID = rs.getLong(4);
                    if (winnerTeamID != 0) {
                        g.setWinnerTeam(TDGRegistry.findTDG(Team.class).findById(winnerTeamID));
                    } else {
                        g.setWinnerTeam(null);
                    }
                }
            }
        }
        return g;
    }

    protected String getWherePrefix() {
        return WHERE;
    }

    private List<String> getPlayersFromTeam(long teamID) throws SQLException {
        List<String> players = new ArrayList<String>();
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_PLAYER_USERNAME_BY_TEAM_ID)) {
            pst.setLong(1, teamID);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    players.add(rs.getString(1));
                }
            }
        }
        return players;
    }

    public List<List<List<String>>> getStatisticsGames() throws SQLException {
        List<List<List<String>>> games = new ArrayList<List<List<String>>>();
        try (Statement pst = TDGRegistry.getConnection().createStatement()) {
            try (ResultSet rs = pst.executeQuery(TEAM_IDS)) {
                while (rs.next()) {
                    games.add(Arrays.asList(getPlayersFromTeam(rs.getLong(1)), getPlayersFromTeam(rs.getLong(2)),
                            getPlayersFromTeam(rs.getLong(3))));
                }
            }
        }
        return games;
    }

    public List<List<Object>> getStatisticsTeams() throws SQLException {
        List<List<Object>> teams = new ArrayList<List<Object>>();
        try (Statement pst = TDGRegistry.getConnection().createStatement()) {
            try (ResultSet rs = pst.executeQuery(BEST_TEAMS)) {
                while (rs.next()) {
                    teams.add(Arrays.asList(getPlayersFromTeam(rs.getLong(1)), rs.getString(2)));
                }
            }
        }
        return teams;
    }

    public List<List<Object>> getStatisticsPlayers() throws SQLException {
        List<List<Object>> players = new ArrayList<List<Object>>();
        try (Statement st = TDGRegistry.getConnection().createStatement()) {
            try (ResultSet rs = st.executeQuery(BEST_PLAYERS)) {
                while (rs.next()) {
                    players.add(Arrays.asList(Arrays.asList(rs.getString(1)), rs.getString(2)));
                }
            }
        }
        return players;
    }
}
