package object;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.univartois.ili.jai.object.Card;
import fr.univartois.ili.jai.object.CardType;
import fr.univartois.ili.jai.object.Player;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class CardTest {
	@Test
	public void testCardProperties() {
		Card card = new Card(CardType.BLUE, "card1");
		assertEquals(CardType.BLUE, card.getType());
		assertEquals("card1", card.getName());
		assertFalse(card.isFound());
		assertEquals("blue", card.getColor());
	}

	@Test
	public void testAddPlayers() {
		Card card = new Card(CardType.BLUE, "card1");
		Player player1 = new Player("player1");
		Player player2 = new Player("player2");
		card.addPlayers(player1);
		card.addPlayers(player2);
		assertEquals(2, card.getPlayers().size());
		assertTrue(card.getPlayers().contains(player1));
		assertTrue(card.getPlayers().contains(player2));
	}

	@Test
	public void testRemovePlayer() {
		Card card = new Card(CardType.BLUE, "card1");
		Player player1 = new Player("player1");
		Player player2 = new Player("player2");
		card.addPlayers(player1);
		card.addPlayers(player2);
		card.removePlayer(player1);
		assertEquals(1, card.nbOfPlayers());
	}
}
