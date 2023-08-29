package fr.univartois.ili.jai.object;

import fr.univartois.ili.jai.persistance.PersistableConcrete;

import java.util.ArrayList;
import java.util.Collection;

public class Team extends PersistableConcrete {

	private Collection<Player> players = new ArrayList<>();
	private boolean won;
	private Player spy;

	public Team() {
		super();
		won = false;
		spy = null;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public boolean fullDecoders() {
		return this.getDecoders().size() >= 3;
	}

	public boolean checkPlayerInsertion(Role role) {
		return (role == Role.SPY && this.getSpy() == null) || (role == Role.DECODER && this.getDecoders().size() < 3)
				|| (role == null && !this.isFull());
	}

	public void addPlayer(Player player) {
		if (!this.isFull() && checkPlayerInsertion(player.getRole()) && !hasPlayer(player)) {
			players.add(player);
			if (player.isSpy()) {
				this.setSpy(player);
			}
		}
	}

	public void addPlayers(Player... players) {
		for (Player p : players) {
			this.addPlayer(p);
		}
	}

	public boolean removePlayer(Player player) {
		if (this.getSpy() != null && (this.getSpy().getUsername().equals(player.getUsername()))) {
			this.setSpy(null);
		}
		return players.removeIf(p -> p.getUsername().equals(player.getUsername()));
	}

	public void removeAllPlayers() {
		players = new ArrayList<>();
		this.setSpy(null);
	}

	public Player getPlayerByUsername(String username) {
		for (Player player : this.players) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}

	public Player getSpy() {
		return spy;
	}

	public void setSpy(Player player) {
		this.spy = player;
	}

	public Collection<Player> getDecoders() {
		Collection<Player> decoders = new ArrayList<>();
		for (Player player : this.players) {
			if (player.getRole() == Role.DECODER) {
				decoders.add(player);
			}
		}
		return decoders;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public boolean teamRequirement() {
		return this.getSize() > 1 && this.getSpy() != null;
	}

	public boolean hasPlayer(Player player) {
		if (player != null) {
			for (Player p : players) {
				if (p.getUsername().equals(player.getUsername())) {
					return true;
				}
			}
		}
		return false;
	}

	public int getSize() {
		return this.getPlayers().size();
	}

	public boolean isFull() {
		return this.getSize() >= 4;
	}

	public boolean isEmpty() {
		return this.getSize() == 0;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
