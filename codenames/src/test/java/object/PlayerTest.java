package object;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Role;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class PlayerTest {
	@Test
	public void testPlayerProperties() {
		Player player = new Player("testuser", Role.SPY);
		assertEquals("testuser", player.getUsername());
		assertEquals(Role.SPY, player.getRole());
	}

	@Test
	public void testIsSpy() {
		Player player = new Player("testuser", Role.SPY);
		assertTrue(player.isSpy());
		player.setRole(Role.DECODER);
		assertFalse(player.isSpy());
	}

	@Test
	public void testEquals() {
		Player player1 = new Player("testuser", Role.SPY);
		Player player2 = new Player("testuser", Role.SPY);
		assertTrue(player1.equals(player2));
	}

	@Test
	public void testPlayerWithId() {
		Player player = new Player(123, "testuser", Role.SPY);
		assertEquals(123, player.getId());
		assertEquals("testuser", player.getUsername());
		assertEquals(Role.SPY, player.getRole());
	}
}
