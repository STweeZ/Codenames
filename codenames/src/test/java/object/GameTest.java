package object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.State;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class GameTest {
	@Test
	public void testGameProperties() {
		Game game = new Game();
		assertNotNull(game.getBlueTeam());
		assertNotNull(game.getRedTeam());
		assertNull(game.getWinnerTeam());
		assertEquals(State.START, game.getState());
	}

	@Test
	public void testInitGame() throws IOException {
		Game game = new Game();
		game.initGame();
		assertNotNull(game.getGrid());
		assertEquals(State.BLUESPY, game.getState());
	}

	@Test
	public void testResetGame() throws IOException {
		Game game = new Game();
		game.setState(State.END);
		game.getBlueTeam().setWon(true);
		game.getRedTeam().setWon(true);
		game.setWinnerTeam(game.getBlueTeam());
		game.resetGame();
		assertNotNull(game.getGrid());
		assertEquals(State.BLUESPY, game.getState());
		assertNull(game.getWinnerTeam());
		assertFalse(game.getBlueTeam().isWon());
		assertFalse(game.getRedTeam().isWon());
	}
}
