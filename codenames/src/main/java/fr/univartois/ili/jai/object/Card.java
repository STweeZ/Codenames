package fr.univartois.ili.jai.object;

import java.util.ArrayList;
import java.util.Collection;

public class Card {

	private CardType type;
	private String name;
	private Collection<Player> players = new ArrayList<>();
	private boolean found = false;

	public Card(CardType type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public CardType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player player) {
		players.removeIf(p -> p.getUsername().equals(player.getUsername()));
	}

	public void resetPlayers() {
		this.players = new ArrayList<>();
	}

	public int nbOfPlayers() {
		return players.size();
	}

	public void addPlayers(Player player) {
		this.players.add(player);
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public String getColor() {
		switch (this.type) {
			case RED:
				return "red";
			case BLUE:
				return "blue";
			case NEUTRAL:
				return "green";
			case TRAP:
				return "white";
			default:
				return "black";

		}
	}
}
