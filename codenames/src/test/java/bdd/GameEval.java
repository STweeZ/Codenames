package bdd;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import fr.univartois.ili.jai.object.CardType;
import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Card;
import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.State;
import fr.univartois.ili.jai.object.Team;
import fr.univartois.ili.jai.object.Role;
import java.util.ArrayList;
import java.util.List;

public class GameEval {

    private Game game;

    @Given("I have a game")
    public void givenIHaveAGame() {
        this.game = new Game();
    }

    @When("I initialize the game")
    public void whenIInitializeTheGame() throws IOException {
        this.game.initGame();
    }

    @Then("the game should have a grid")
    public void thenTheGameShouldHaveAGrid() {
        assertNotNull(this.game.getGrid());
    }

    @Then("the game state should be BLUESPY")
    public void thenTheGameStateShouldBeBLUESPY() {
        assertEquals(State.BLUESPY, this.game.getState());
    }

    @When("I reset the game")
    public void whenIResetTheGame() throws IOException {
        this.game.resetGame();
    }

    @Then("the game should not have a winner team")
    public void thenTheGameShouldNotHaveAWinnerTeam() {
        assertEquals(null, this.game.getWinnerTeam());
    }


    @Given("the blue and red teams have the required number of players")
    public void givenTheBlueAndRedTeamsHaveTheRequiredNumberOfPlayers() {
        this.game.getBlueTeam().addPlayers(new Player("player1",Role.SPY));
        this.game.getBlueTeam().addPlayers(new Player("player2"));
        this.game.getBlueTeam().addPlayers(new Player("player3"));
        this.game.getBlueTeam().addPlayers(new Player("player4"));
        this.game.getBlueTeam().addPlayers(new Player("player5"));

        this.game.getRedTeam().addPlayers(new Player("player6",Role.SPY));
        this.game.getRedTeam().addPlayers(new Player("player7"));
        this.game.getRedTeam().addPlayers(new Player("player8"));
        this.game.getRedTeam().addPlayers(new Player("player9"));
        this.game.getRedTeam().addPlayers(new Player("player10"));
    }

    @When("I check the game requirement")
    public void whenICheckTheGameRequirement() {
        // Nothing to do here, this step is only used to change the game state
    }

    @Then("the game should be ready to start")
    public void thenTheGameShouldBeReadyToStart() {
        assertTrue(this.game.gameRequirement());
    }

    @Given("the blue team has the required number of players")
    public void givenTheBlueTeamHasTheRequiredNumberOfPlayers() {
        this.game.getBlueTeam().addPlayers(new Player("player1"));
        this.game.getBlueTeam().addPlayers(new Player("player2"));
        this.game.getBlueTeam().addPlayers(new Player("player3"));
        this.game.getBlueTeam().addPlayers(new Player("player4"));
        this.game.getBlueTeam().addPlayers(new Player("player5"));
    }

    @Given("the red team has fewer players than required")
    public void givenTheRedTeamHasFewerPlayersThanRequired() {
        this.game.getRedTeam().addPlayers(new Player("player6"));
        this.game.getRedTeam().addPlayers(new Player("player7"));
        this.game.getRedTeam().addPlayers(new Player("player8"));
    }

    @Then("the game should not be ready to start")
    public void thenTheGameShouldNotBeReadyToStart() {
        assertFalse(this.game.gameRequirement());
    }

    @Given("the game has started")
    public void givenTheGameHasStarted() {
        assertNotNull(game);
    }

    @When("I change the turn")
    public void whenIChangeTheTurn() {
        this.game.nextTurn();
    }

    @Then("the game state should be BLUEDECODER")
    public void thenTheGameStateShouldBeBLUEDECODER() {
        assertEquals(State.BLUEDECODER, this.game.getState());
    }

    @Then("the game should not be in a spy turn")
    public void thenTheGameShouldNotBeInASpyTurn() {
        assertFalse(this.game.isSpyTurn());
    }

    @Then("the game should be in a blue turn")
    public void thenTheGameShouldBeInABlueTurn() {
        assertTrue(this.game.isBlueTurn());
    }

    @When("I set the clue and occurence")
    public void whenISetTheClueAndOccurence() {
        this.game.spyTurn("clue", 1);
    }

    @Then("the game state should be REDSPY")
    public void thenTheGameStateShouldBeREDSPY() {
        assertEquals(State.REDSPY, this.game.getState());
    }

    @Then("the game should be in a spy turn")
    public void thenTheGameShouldBeInASpyTurn() {
        assertTrue(this.game.isSpyTurn());
    }

    @Then("the clue and occurence should be set")
    public void thenTheClueAndOccurenceShouldBeSet() {
        assertEquals("clue", this.game.getClue());
        assertEquals(1, this.game.getOccurence());
    }

    @When("I set the game state to END")
    public void whenISetTheGameStateToEND() {
        this.game.setState(State.END);
    }

    @Then("the game should be over")
    public void thenTheGameShouldBeOver() {
        assertTrue(this.game.isOver());
    }

    @When("I set the blue team as the winner")
    public void whenISetTheBlueTeamAsTheWinner() {
        this.game.setWinnerTeam(this.game.getBlueTeam());
    }

    @Then("the blue team should be the winner")
    public void thenTheBlueTeamShouldBeTheWinner() {
        assertEquals(this.game.getBlueTeam(), this.game.getWinnerTeam());
    }

    @Then("the blue team should have won")
    public void thenTheBlueTeamShouldHaveWon() {
        assertTrue(this.game.getBlueTeam().isWon());
    }

    @Then("the red team should not have won")
    public void thenTheRedTeamShouldNotHaveWon() {
        assertFalse(this.game.getRedTeam().isWon());
    }

    @Given("I have a player with username \"player1\"")
    public void givenIHaveAPlayerWithUsernameplayer1() {
        Player player = new Player("player1");
        this.game.setMaster(player);
    }

    @When("I check if the player is the master")
    public void whenICheckIfThePlayerIsTheMaster() {
        // Nothing to do here, this step is only used to change the game state
    }

    @When("I check if the game has started")
    public void whenICheckIfTheGameHasStarted() {
        // Nothing to do here, this step is only used to change the game state
    }

    @Then("the game should have started")
    public void thenTheGameShouldHaveStarted() {
        assertFalse(this.game.hasStart());
    }

    @Then("the player should be the master")
    public void thenThePlayerShouldBeTheMaster() {
        assertTrue(this.game.amIMaster(new Player("player1")));
    }

    @When("I check if the username is the master")
    public void whenICheckIfTheUsernameIsTheMaster() {
        // Nothing to do here, this step is only used to change the game state
    }

    @Then("the username should be the master")
    public void thenTheUsernameShouldBeTheMaster() {
        assertTrue(this.game.amIMaster("player2"));
    }

    @Given("I have a player with username \"player3\"")
    public void givenIHaveAPlayerWithUsernameplayer3() {
        // Nothing to do here, this step is only used to change the game state
    }

    @Then("the player should not be the master")
    public void thenThePlayerShouldNotBeTheMaster() {
        assertFalse(this.game.amIMaster(new Player("player3")));
    }

    @Given("I have a username \"player4\"")
    public void givenIHaveAUsernameplayer4() {
        // Nothing to do here, this step is only used to change the game state
    }

    @Then("the username should not be the master")
    public void thenTheUsernameShouldNotBeTheMaster() {
        assertFalse(this.game.amIMaster("player4"));
    }

    @Given("the game state is END")
    public void givenTheGameStateIsEND() {
        this.game = new Game();
        this.game.setState(State.END);
    }

    @When("I check if the game is over")
    public void whenICheckIfTheGameIsOver() {
        this.game.isOver();
    }

    @Then("the game state should be START")
    public void thenTheGameStateShouldBeSTART() {
        assertEquals(State.START, this.game.getState());
    }

    @Then("the blue team should not have won")
    public void thenTheBlueTeamShouldNotHaveWon() {
        assertFalse(this.game.getBlueTeam().isWon());
    }

    @Then("the red team should not have won either")
    public void thenTheRedTeamShouldNotHaveWonEither() {
        assertFalse(this.game.getRedTeam().isWon());
    }

    @Then("the winner team should be null")
    public void thenTheWinnerTeamShouldBeNull() {
        assertNull(this.game.getWinnerTeam());
    }

    @Then("the game should not be over")
    public void thenTheGameShouldNotBeOver() {
        assertFalse(this.game.isOver());
    }


    @Then("the game should have the correct number of blue and red cards")
    public void thenTheGameShouldHaveTheCorrectNumberOfBlueAndRedCards() {
        assertEquals(8, this.game.blueCardsRemaining());
        assertEquals(7, this.game.redCardsRemaining());
    }

    @When("I add a player to the blue team")
    public void whenIAddAPlayerToTheBlueTeam() {
        this.game.getBlueTeam().addPlayers(new Player("player1"));
    }

    @Then("the blue team should have a player")
    public void thenTheBlueTeamShouldHaveAPlayer() {
        assertEquals(1, this.game.getBlueTeam().getSize());
    }

    @When("I add a player to the red team")
    public void whenIAddAPlayerToTheRedTeam() {
        this.game.getRedTeam().addPlayers(new Player("player2"));
    }

    @Then("the red team should have a player")
    public void thenTheRedTeamShouldHaveAPlayer() {
        assertEquals(1, this.game.getRedTeam().getSize());
    }

    @Given("I have a game with blue and red teams")
    public void givenIHaveAGameWithBlueAndRedTeams() {
        // Nothing to do here, this step is only used to change the game state
    }

    @When("I add a player to the vote end round")
    public void whenIAddAPlayerToTheVoteEndRound() {
        this.game.addVoteEndRound(new Player("player1"));
    }

    @Then("the vote end round should have a player")
    public void thenTheVoteEndRoundShouldHaveAPlayer() {
        assertEquals(1, this.game.getNumberVoteEndRound());
    }

    @When("I reset the vote end round")
    public void whenIResetTheVoteEndRound() {
        this.game.resetVoteEndRound();
    }

    @Then("the vote end round should not have any players")
    public void thenTheVoteEndRoundShouldNotHaveAnyPlayers() {
        assertEquals(0, this.game.getNumberVoteEndRound());
    }
}
