package bdd;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import fr.univartois.ili.jai.object.Player;

public class PlayerEval {

    private Player player;

    @Given("a player")
    public void newInterpreter() {
        player = new Player();
    }

    @When("the username assigned is $username")
    public void eval(String username) {
        player.setUsername(username);;
    }

    @Then("his username should be $expectedValue")
    public void checkValue(String expectedValue) {
        assertThat(player.getUsername(), equalTo(expectedValue));
    }
}
