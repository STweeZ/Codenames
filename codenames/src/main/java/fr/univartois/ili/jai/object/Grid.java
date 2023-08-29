package fr.univartois.ili.jai.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import utils.GridGenerator;

public class Grid {

	private Collection<Card> cards = new ArrayList<>();

	public Grid() {
		super();
	}

	public void initGrid() throws IOException {
		String[] grid_word = GridGenerator.generate_grid();
		for (int i = 0; i < 9; i++) {
			Card card = new Card(CardType.BLUE, grid_word[i]);
			cards.add(card);
		}
		for (int i = 9; i < 17; i++) {
			Card card = new Card(CardType.RED, grid_word[i]);
			cards.add(card);
		}
		for (int i = 17; i < 24; i++) {
			Card card = new Card(CardType.NEUTRAL, grid_word[i]);
			cards.add(card);
		}
		Card card = new Card(CardType.TRAP, grid_word[24]);
		cards.add(card);
		Collections.shuffle((List<?>) cards);
	}

	public boolean containCard(String word) {
		for (Card card : cards) {
			String cardName = card.getName().toLowerCase();
			if (cardName.contains(word) || cardName.contains(word.trim()) || word.contains(cardName)
					|| word.trim().contains(cardName)) {
				return true;
			}
		}
		return false;
	}

	public Card getCardByName(String name) {
		for (Card card : cards) {
			if (card.getName().toLowerCase().equals(name))
				return card;
		}
		return null;
	}

	public boolean cardFound(String name) {
		Card card = getCardByName(name);
		if (card != null && card.isFound())
			return true;
		return false;
	}

	public void removeVoteFromPlayer(Player player) {
		for (Card card : cards) {
			card.removePlayer(player);
		}
	}

	public void addVote(Player player, String name) {
		removeVoteFromPlayer(player);
		if (player != null && name != null && !name.trim().equals("")) {
			Card card = getCardByName(name);
			card.addPlayers(player);
		}
	}

	public boolean isPlayerVoted(Player player) {
		for (Card card : cards) {
			if (card.getPlayers().contains(player)) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfVote() {
		int qty = 0;
		for (Card card : cards) {
			qty += card.nbOfPlayers();
		}
		return qty;
	}

	public Card getMostVotedCard() {
		Card base = null;
		for (Card card : cards) {
			if (base == null || card.nbOfPlayers() > base.nbOfPlayers()) {
				base = card;
			}
		}
		return base;
	}

	public void resetVotes() {
		for (Card card : cards) {
			card.resetPlayers();
		}
	}

	public void clearGrid() {
		// TODO
	}

	public Collection<Card> getCards() {
		return cards;
	}

	public int teamCardsRemaining(CardType cardType) {
		int total = 0;
		for (Card card : cards) {
			if (card.getType() == cardType && !card.isFound()) {
				total += 1;
			}
		}
		return total;
	}

	public int blueCardsRemaining() {
		return teamCardsRemaining(CardType.BLUE);
	}

	public int redCardsRemaining() {
		return teamCardsRemaining(CardType.RED);
	}
}
