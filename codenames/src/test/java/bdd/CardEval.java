package bdd;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.Story;
import org.junit.Test;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Card;
import fr.univartois.ili.jai.object.CardType;

public class CardEval {

    private Card card;
    private CardType type;
    private String name;
    private Player player;

    @Given("I have a card type $type and a name $name")
    public void givenICreateCard(@Named("type") String type, @Named("name") String name) {
        this.type = CardType.valueOf(type);
        this.name = name;
    }

    @Given("I have a card with type $type and name $name")
    public void givenICreateCardWithTypeAndName(@Named("type") String type, @Named("name") String name) {
        this.card = new Card(CardType.valueOf(type), name);
    }

    @Given("I have a player with username $username")
    public void givenIHaveAPlayer(@Named("username") String username) {
        this.player = new Player(username);
    }

    @When("I create a new card with these values")
    public void whenICreateANewCard() {
        this.card = new Card(this.type, this.name);
    }



    @When("I add the player to the card")
    public void whenIAddPlayerToCard() {
        this.card.addPlayers(this.player);
    }

    @Given("I add the player to the card")
    public void GivenIAddPlayerToCard() {
        this.card.addPlayers(this.player);
    }

    @When("I remove the player from the card")
    public void whenIRemovePlayerFromCard() {
        this.card.removePlayer(this.player);
    }

    @When("I reset the players of the card")
    public void whenIResetPlayersOfCard() {
        this.card.resetPlayers();
    }

    @Then("the card should have type $type and name $name")
    public void thenCardShouldHaveTypeAndName(@Named("type") String type, @Named("name") String name) {
        assertEquals(this.card.getType(), CardType.valueOf(type));
        assertEquals(this.card.getName(), name);
    }

    @Then("the card should have $nbOfPlayers player with username $username")
    public void thenCardShouldHaveNbOfPlayers(@Named("nbOfPlayers") int nbOfPlayers, @Named("username") String username) {
        Collection<Player> players = this.card.getPlayers();
        assertEquals(players.size(), nbOfPlayers);
        if (nbOfPlayers > 0) {
            assertTrue(players.stream().anyMatch(p -> p.getUsername().equals(username)));
        }
    }

    @Then("the card should not have any players")
    public void thenTheCardShouldNotHaveAnyPlayers() {
        assertTrue(this.card.getPlayers().isEmpty());
    }

    @Given("I have two players with username \"player1\" and \"player2\"")
    public void givenIHaveTwoPlayersWithUsernameplayer1Andplayer2() {
        this.card.addPlayers(new Player("player1"));
        this.card.addPlayers(new Player("player2"));
    }
}