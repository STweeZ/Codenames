Some team creation

Narrative:

In order to have a complete team
As a player
I want to add players to the team

Scenario: Add player to team

Given a team with no players
When I add a player to the team
Then the team should have one player

Scenario: Add multiple players to team

Given a team with no players
When I add multiple players to the team
Then the team should have the correct number of players
