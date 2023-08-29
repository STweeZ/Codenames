Some player creation

Narrative:
Our application should be able
to create players.

Scenario: built-in creations

Given a player

When the username assigned is <username>
Then his username should be <result>

Examples:

|username|result|
|Grégoire|Grégoire|
|Théo|Théo|
