package fr.univartois.ili.jai.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Team;

public class TeamTDG extends AbstractTDG<Team> {

    private static final String CREATE = "CREATE TABLE Team (ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY)";
    private static final String DROP = "DROP TABLE Team ";

    private static final String INSERT = "INSERT INTO Team VALUES (default) ";
    private static final String DELETE = "DELETE FROM Team WHERE ID = ? ";
    private static final String FIND_BY_ID = "SELECT ID FROM Team WHERE ID=? ";
    private static final String WHERE = "SELECT ID FROM Team t WHERE ";

    private static final String CREATE_TEAM_PLAYER = "CREATE TABLE TeamPlayer (ID_TEAM BIGINT NOT NULL REFERENCES Team(ID) ON DELETE CASCADE, ID_PLAYER BIGINT NOT NULL REFERENCES Player(ID) ON DELETE CASCADE, PRIMARY KEY (ID_TEAM,ID_PLAYER))";
    private static final String DROP_TEAM_PLAYER = "DROP TABLE TeamPlayer ";

    private static final String INSERT_TEAM_PLAYER = "INSERT INTO TeamPlayer(ID_TEAM,ID_PLAYER) VALUES (?,?) ";
    private static final String DELETE_TEAM_PLAYER_BY_TEAM_ID = "DELETE FROM TeamPlayer WHERE ID_TEAM = ? ";
    private static final String FIND_PLAYER_BY_TEAM_ID = "SELECT ID_PLAYER FROM TeamPlayer WHERE ID_TEAM=? ";
    private static final String FIND_TEAM_ID_BY_PLAYER_IDS = "SELECT ID_TEAM FROM TeamPlayer tp1 WHERE ID_PLAYER IN (%s) GROUP BY ID_TEAM HAVING COUNT(*) = ( SELECT COUNT(*) FROM TeamPlayer tp2 WHERE tp1.ID_TEAM = tp2.ID_TEAM ) and COUNT(*) = ?";

    public void createTable() throws SQLException {
        try (var stm = TDGRegistry.getConnection().createStatement()) {
            stm.executeUpdate(CREATE);
            stm.executeUpdate(CREATE_TEAM_PLAYER);
        }
    }

    public void deleteTable() throws SQLException {
        try (var stm = TDGRegistry.getConnection().createStatement()) {
            stm.executeUpdate(DROP_TEAM_PLAYER);
            stm.executeUpdate(DROP);
        }
    }

    protected Team findByIdIntoDB(long id) throws SQLException {
        Team t = null;
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    t = new Team();
                    t.setId(rs.getLong(1));
                    refreshIntoTeamPlayerDB(t);
                }
            }
        }
        return t;
    }

    protected Team insertIntoDB(Team t) throws SQLException {
        findExisting(t);
        if (t.getId() == 0) {
            try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
                    Statement.RETURN_GENERATED_KEYS)) {
                int result = pst.executeUpdate();
                assert result == 1;
                try (ResultSet keys = pst.getGeneratedKeys()) {
                    if (keys.next()) {
                        t.setId(keys.getLong(1));
                    }
                }
                insertIntoTeamPlayerDB(t);
            }
        }
        return t;
    }

    protected Team findExisting(Team t) throws SQLException {
        List<Long> playerIds = new ArrayList<>();
        for (Player p : t.getPlayers()) {
            if (p.getId() == 0) {
                TDGRegistry.findTDG(Player.class).insert(p);
            }
            playerIds.add(p.getId());
        }
        var sql = String.format(FIND_TEAM_ID_BY_PLAYER_IDS,
                playerIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        if (!playerIds.isEmpty()) {
            try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(sql)) {
                pst.setInt(1, t.getPlayers().size());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        t.setId(rs.getLong(1));
                    }
                }
            }
        }
        return t;
    }

    protected void insertIntoTeamPlayerDB(Team t) throws SQLException {
        for (Player p : t.getPlayers()) {
            if (p.getId() == 0) {
                TDGRegistry.findTDG(Player.class).insert(p);
            }
            if (!isPlayerAlreadyInTeamDB(t, p)) {
                try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT_TEAM_PLAYER)) {
                    pst.setLong(1, t.getId());
                    pst.setLong(2, p.getId());
                    int result = pst.executeUpdate();
                    assert result == 1;
                }
            }
        }
    }

    private boolean isPlayerAlreadyInTeamDB(Team t, Player p) throws SQLException {
        if (p.getId() != 0) {
            try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_PLAYER_BY_TEAM_ID)) {
                pst.setLong(1, t.getId());
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        if (rs.getLong(1) == p.getId()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected Team updateIntoDB(Team t) throws SQLException {
        for (Player p : t.getPlayers()) {
            if (p.getId() != 0) {
                TDGRegistry.findTDG(Player.class).update(p);
            }
        }
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE_TEAM_PLAYER_BY_TEAM_ID)) {
            assert findById(t.getId()).equals(t);
            pst.setLong(1, t.getId());
            pst.executeUpdate();
            insertIntoTeamPlayerDB(t);
            return t;
        }
    }

    protected Team deleteFromDB(Team t) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
            assert findById(t.getId()).equals(t);
            pst.setLong(1, t.getId());
            int result = pst.executeUpdate();
            assert result == 1;
            return t;
        }
    }

    protected Team refreshIntoDB(Team t) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
            pst.setLong(1, t.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    t.setId(rs.getLong(1));
                    refreshIntoTeamPlayerDB(t);
                }
            }
        }
        return t;
    }

    protected Team refreshIntoTeamPlayerDB(Team t) throws SQLException {
        try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_PLAYER_BY_TEAM_ID)) {
            pst.setLong(1, t.getId());
            t.removeAllPlayers();
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    t.addPlayer(TDGRegistry.findTDG(Player.class).findById(rs.getLong(1)));
                }
            }
        }
        return t;
    }

    protected String getWherePrefix() {
        return WHERE;
    }
}
