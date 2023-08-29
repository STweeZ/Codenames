Narrative:
As a player
I want to create a card
So that I can use it in the game

Scenario: Create a red card
Given I have a card type RED and a name "Card1"
When I create a new card with these values
Then the card should have type RED and name "Card1"

Scenario: Add a player to a card
Given I have a card with type RED and name "Card1"
And I have a player with username "player1"
When I add the player to the card
Then the card should have 1 player with username "player1"

Scenario: Remove a player from a card
Given I have a card with type RED and name "Card1"
And I have a player with username "player1"
And I add the player to the card
When I remove the player from the card
Then the card should not have any players

Scenario: Reset players of a card
Given I have a card with type RED and name "Card1"
And I have two players with username "player1" and "player2"
And I add the player to the card
When I reset the players of the card
Then the card should not have any players