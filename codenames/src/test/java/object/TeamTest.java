package object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Role;
import fr.univartois.ili.jai.object.Team;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TeamTest {
	@Test
	public void testAddPlayer() {
		Team team = new Team();
		Player player = new Player("player1", Role.DECODER);
		team.addPlayer(player);
		assertEquals(1, team.getSize());
		assertEquals(player, team.getPlayerByUsername("player1"));
		Player player2 = new Player("player2", Role.DECODER);
		team.addPlayer(player2);
		assertEquals(2, team.getSize());
		Player player3 = new Player("player3", Role.DECODER);
		team.addPlayer(player3);
		assertEquals(3, team.getSize());
		Player player4 = new Player("player4", Role.SPY);
		team.addPlayer(player4);
		assertEquals(4, team.getSize());
		Player player5 = new Player("player5", Role.SPY);
		team.addPlayer(player5);
		assertEquals(4, team.getSize());
	}

	@Test
	public void testAddPlayers() {
		Team team = new Team();
		Player player1 = new Player("player1", Role.DECODER);
		Player player2 = new Player("player2", Role.DECODER);
		Player player3 = new Player("player3", Role.SPY);
		team.addPlayers(player1, player2, player3);
		assertEquals(3, team.getSize());
		assertEquals(player1, team.getPlayerByUsername("player1"));
		assertEquals(player2, team.getPlayerByUsername("player2"));
		assertEquals(player3, team.getPlayerByUsername("player3"));
		team.addPlayer(player2);
		assertEquals(3, team.getSize());
		assertEquals(player1, team.getPlayerByUsername("player1"));
		assertEquals(player2, team.getPlayerByUsername("player2"));
		assertEquals(player3, team.getPlayerByUsername("player3"));
	}
}
