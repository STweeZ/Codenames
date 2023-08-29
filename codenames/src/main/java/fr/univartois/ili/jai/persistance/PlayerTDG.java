package fr.univartois.ili.jai.persistance;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.univartois.ili.jai.object.Player;

public class PlayerTDG extends AbstractTDG<Player> {

	private static final String CREATE = "CREATE TABLE Player (ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY, USERNAME VARCHAR(100) NOT NULL, CONSTRAINT USERNAME_UNIQUE UNIQUE (USERNAME))";
	private static final String DROP = "DROP TABLE Player";

	private static final String INSERT = "INSERT INTO Player (USERNAME) VALUES(?)";
	private static final String UPDATE = "UPDATE Player p SET p.USERNAME = ? WHERE p.ID = ?";
	private static final String DELETE = "DELETE FROM Player WHERE ID = ?";
	private static final String FIND_BY_ID = "SELECT ID,USERNAME FROM Player WHERE ID=?";
	private static final String WHERE = "SELECT ID FROM Player p WHERE ";

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

	protected Player findByIdIntoDB(long id) throws SQLException {
		Player p = null;
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					try {
						Class<?>[] types = { long.class, String.class };
						Object[] args = { rs.getLong(1), rs.getString(2) };
						p = Player.class.getConstructor(types).newInstance(args);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						return null;
					}
				}
			}
		}
		return p;
	}

	protected Player insertIntoDB(Player p) throws SQLException {
		List<Player> p2 = selectWhere("USERNAME LIKE ?", p.getUsername());
		if (p2.isEmpty()) {
			try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(INSERT,
					Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, p.getUsername());
				int result = pst.executeUpdate();
				assert result == 1;
				try (ResultSet keys = pst.getGeneratedKeys()) {
					if (keys.next()) {
						p.setId(keys.getLong(1));
					}
				}
			}
		} else {
			p.setId(p2.get(0).getId());
		}
		return p;
	}

	protected Player updateIntoDB(Player p) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(UPDATE)) {
			assert findById(p.getId()).equals(p);
			pst.setString(1, p.getUsername());
			pst.setLong(2, p.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return p;
		}
	}

	protected Player deleteFromDB(Player p) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(DELETE)) {
			assert findById(p.getId()).equals(p);
			pst.setLong(1, p.getId());
			int result = pst.executeUpdate();
			assert result == 1;
			return p;
		}
	}

	protected Player refreshIntoDB(Player p) throws SQLException {
		try (PreparedStatement pst = TDGRegistry.getConnection().prepareStatement(FIND_BY_ID)) {
			pst.setLong(1, p.getId());
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					p.setUsername(rs.getString(2));
				}
			}
		}
		return p;
	}

	protected String getWherePrefix() {
		return WHERE;
	}
}
