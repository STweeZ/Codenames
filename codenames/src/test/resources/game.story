Narrative: Game tests

Scenario: Initialize a game
Given I have a game
When I initialize the game
Then the game should have a grid
And the game state should be BLUESPY

Scenario: Reset a game
Given I have a game
And the game has started
When I reset the game
Then the game should have a grid
And the game state should be BLUESPY
And the game should not have a winner team


Scenario: Check game requirement
Given I have a game
And the blue and red teams have the required number of players
When I check the game requirement
Then the game should be ready to start

Scenario: Check game requirement with insufficient players
Given I have a game
And the blue team has the required number of players
And the red team has fewer players than required
When I check the game requirement
Then the game should not be ready to start


Scenario: Check if the game has started
Given I have a game
When I check if the game has started
Then the game should have started

Scenario: Check if the game is over
Given I have a game
And the game state is END
When I check if the game is over
Then the game should be over
