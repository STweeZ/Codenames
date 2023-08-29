package bdd;

import static org.junit.Assert.assertEquals;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Team;

public class TeamEval {

    private Team team;
    private Player player;

    @Given("a team with no players")
    public void createEmptyTeam() {
        team = new Team();
    }

    @When("I add a player to the team")
    public void addPlayerToTeam() {
        player = new Player("player1");
        team.addPlayer(player);
    }

    @Then("the team should have one player")
    public void checkTeamSize() {
        assertEquals(1, team.getSize());
    }

    @When("I add multiple players to the team")
    public void addMultiplePlayersToTeam() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        team.addPlayers(player1, player2, player3);
    }

    @Then("the team should have the correct number of players")
    public void checkTeamSizeAfterAddingMultiplePlayers() {
        assertEquals(3, team.getSize());
    }

}
