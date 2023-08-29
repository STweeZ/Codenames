package fr.univartois.ili.jai.object;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.univartois.ili.jai.persistance.PersistableConcrete;
import utils.Statistics;

public class Game extends PersistableConcrete {

	private Player master;
	private Team blueTeam;
	private Team redTeam;
	private Team winnerTeam;
	private State state;
	private Grid grid;
	private String clue;
	private int occurence;
	private Collection<Player> voteEndRound = new ArrayList<>();

	public Game() {
		super();
		blueTeam = new Team();
		redTeam = new Team();
		state = State.START;
	}

	public int getHashId() {
		return System.identityHashCode(this);
	}

	public Team getBlueTeam() {
		return blueTeam;
	}

	public Team getRedTeam() {
		return redTeam;
	}

	public Team getWinnerTeam() {
		return winnerTeam;
	}

	public void setBlueTeam(Team t) {
		this.blueTeam = t;
	}

	public void setRedTeam(Team t) {
		this.redTeam = t;
	}

	public void setWinnerTeam(Team t) {
		this.winnerTeam = t;
		t.setWon(true);
	}

	public Player getMaster() {
		return this.master;
	}

	public void setMaster(Player player) {
		this.master = player;
	}

	public boolean amIMaster(Player player) {
		return getMaster() != null && player.getUsername().equals(getMaster().getUsername());
	}

	public boolean amIMaster(String username) {
		return getMaster() != null && username.equals(getMaster().getUsername());
	}

	public void initGame() throws IOException {
		Grid grid = new Grid();
		grid.initGrid();
		this.setGrid(grid);
		this.setState(State.BLUESPY);
	}

	public void resetGame() throws IOException {
		initGame();
		this.setId(0);
		this.winnerTeam = null;
		this.getBlueTeam().setWon(false);
		this.getRedTeam().setWon(false);

	}

	public boolean gameRequirement() {
		return this.getBlueTeam().teamRequirement() && this.getRedTeam().teamRequirement();
	}

	public boolean isBlueTurn() {
		return (this.state == State.BLUEDECODER || this.state == State.BLUESPY) ? true : false;
	}

	public boolean isSpyTurn() {
		return (this.state == State.BLUESPY || this.state == State.REDSPY) ? true : false;
	}

	public void spyTurn(String clue, int occurence) {
		this.setClue(clue);
		this.setOccurence(occurence);
		this.nextTurn();
	}

	public void nextTurn() {
		this.setState(this.state.getNext());
	}

	public boolean hasStart() {
		return this.state != State.START;
	}

	public boolean isOver() {
		return this.state == State.END;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int blueCardsRemaining() {
		return this.grid.blueCardsRemaining();
	}

	public int redCardsRemaining() {
		return this.grid.redCardsRemaining();
	}

	public String getClue() {
		return clue;
	}

	public void setClue(String clue) {
		this.clue = clue;
	}

	public int getOccurence() {
		return occurence;
	}

	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}

	public Player getPlayerByUsername(String username) {
		Player player = this.getBlueTeam().getPlayerByUsername(username);
		return player != null ? player : this.getRedTeam().getPlayerByUsername(username);
	}

	public Player removePlayer(String username) {
		Player player = this.getBlueTeam().getPlayerByUsername(username);
		if (player != null) {
			return this.getBlueTeam().removePlayer(player) ? player : null;
		}
		player = this.getRedTeam().getPlayerByUsername(username);
		if (player != null) {
			return this.getRedTeam().removePlayer(player) ? player : null;
		}
		return null;
	}

	public Player playerLeft(String username) {
		Player player = removePlayer(username);
		if (player != null)
			assignRandomMaster();
		return player;
	}

	public boolean isSpy(String username) {
		Player player = this.getPlayerByUsername(username);
		return player != null ? player.isSpy() : false;
	}

	public boolean isMyTurn(String username) {
		Player player = this.getBlueTeam().getPlayerByUsername(username);
		if (player != null) {
			if ((this.getState() == State.BLUESPY && player.getRole() == Role.SPY)
					|| (this.getState() == State.BLUEDECODER && player.getRole() == Role.DECODER)) {
				return true;
			} else {
				return false;
			}

		}
		player = this.getRedTeam().getPlayerByUsername(username);
		if (player != null) {
			if ((this.getState() == State.REDSPY && player.getRole() == Role.SPY)
					|| (this.getState() == State.REDDECODER && player.getRole() == Role.DECODER)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean checkClueValidity(String clue, String occurence) {
		try {
			if (clue != null && occurence != null && !clue.trim().isEmpty() && Integer.parseInt(occurence) > 0
					&& !grid.containCard(clue)) {
				return true;
			}
		} finally {
		}
		return false;

	}

	public void playerCardVote(String username, String card) {
		Player player = getPlayerByUsername(username);
		this.grid.addVote(player, card);
		checkCardVoteImpact();
	}

	public void checkCardVoteImpact() {
		if (this.grid.getNumberOfVote() >= (this.isBlueTurn() ? this.getBlueTeam().getPlayers().size() - 1
				: this.getRedTeam().getPlayers().size() - 1)) {
			Card evaluate = this.grid.getMostVotedCard();
			evaluate.setFound(true);
			resetVoteEndRound();
			grid.resetVotes();
			if (evaluate.getType() == CardType.TRAP) {
				nextTurn();
				endGame();
				return;
			} else if ((evaluate.getType() == CardType.BLUE && !this.isBlueTurn())
					|| (evaluate.getType() == CardType.RED && this.isBlueTurn())) {
				nextTurn();
				checkWinner();
				return;
			}
			if (--this.occurence == 0) {
				nextTurn();
			}
			checkWinner();
		}
	}

	public boolean checkCardVoteValidity(String card) {
		return card != null && this.grid.containCard(card) && !this.grid.cardFound(card);
	}

	public void checkWinner() {
		if (this.grid.blueCardsRemaining() == 0) {
			declareWinner(this.getBlueTeam());
		} else if (this.grid.redCardsRemaining() == 0) {
			declareWinner(this.getRedTeam());
		}
	}

	public boolean isGameEmpty() {
		return this.getBlueTeam().isEmpty() && this.getRedTeam().isEmpty();
	}

	public boolean isGameFull() {
		return this.getBlueTeam().isFull() && this.getRedTeam().isFull();
	}

	public int getNumberOfPlayer() {
		return this.getBlueTeam().getSize() + this.getRedTeam().getSize();
	}

	public Grid getGrid() {
		return this.grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public boolean playerInGame(Player player) {
		return this.getBlueTeam().hasPlayer(player) || this.getRedTeam().hasPlayer(player);
	}

	public boolean playerInGameByUsername(String username) {
		return playerInGame(getPlayerByUsername(username));
	}

	public boolean playerIsBlueDecoder(String username) {
		Player player = getPlayerByUsername(username);
		if (player != null) {
			return this.getBlueTeam().hasPlayer(player) && !player.isSpy();
		}
		return false;
	}

	public boolean playerIsRedDecoder(String username) {
		Player player = getPlayerByUsername(username);
		if (player != null) {
			return this.getRedTeam().hasPlayer(player) && !player.isSpy();
		}
		return false;
	}

	public void addVoteEndRound(Player player) {
		if (checkEndRoundVoteValidity(player))
			voteEndRound.add(player);
		checkEndRoundVoteImpact();
	}

	public void checkEndRoundVoteImpact() {
		if (this.voteEndRound.size() >= (this.isBlueTurn() ? this.getBlueTeam().getPlayers().size() - 1
				: this.getRedTeam().getPlayers().size() - 1)) {
			resetVoteEndRound();
			grid.resetVotes();
			nextTurn();
		}
	}

	public boolean checkEndRoundVoteValidity(Player player) {
		return isMyTurn(player.getUsername()) && this.playerInGame(player) && !checkPlayerAlreadyVoted(player);
	}

	public void resetVoteEndRound() {
		voteEndRound = new ArrayList<>();
	}

	public int getNumberVoteEndRound() {
		return voteEndRound.size();
	}

	public boolean checkPlayerAlreadyVoted(Player player) {
		for (Player vote : voteEndRound) {
			if (vote.getUsername().equals(player.getUsername())) {
				return true;
			}
		}
		return false;
	}

	public void endGame() {
		declareWinner(this.isBlueTurn() ? this.getBlueTeam() : this.getRedTeam());
	}

	public void declareWinner(Team team) {
		setWinnerTeam(team);
		this.state = State.END;
		try {
			Statistics.insertNewGame(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void playerJoinTeamInGame(Team team, Role role, String username) {
		if (isOver() || !playerInGame(getPlayerByUsername(username)))
			playerJoinTeam(team, role, username);
	}

	public void playerJoinTeam(Team team, Role role, String username) {
		if (!team.isFull() && team.checkPlayerInsertion(role)) {
			Player player = removePlayer(username);
			if (player == null) {
				player = new Player(username, role);
			} else {
				player.setRole(role);
			}
			team.addPlayer(player);
			if (getMaster() == null) {
				this.setMaster(player);
			}
		}
	}

	public boolean assignRandomMasterFromTeam(Team team) {
		for (Player player : team.getPlayers()) {
			setMaster(player);
			return true;
		}
		return false;
	}

	public boolean playerMadeAChoice(String username) {
		return getGrid().isPlayerVoted(getPlayerByUsername(username));
	}

	public boolean assignRandomMaster() {
		return assignRandomMasterFromTeam(getBlueTeam()) || assignRandomMasterFromTeam(getRedTeam());
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
