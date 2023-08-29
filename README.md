# CodenameILI2022

## Instruction Java pour l'Internet

La branche à corriger est `main`.
L'application Codenames se trouve dans le répertoire `codenames`. Pour executer l'application vous pouvez l'ouvrir via Eclipse, IntelliJ, ou alors utiliser gretty et effectuer la commande `gradle appRun`.

## Instructions devops

La branche à corriger est `main`.
Pour lancer l'application, il vous suffit de réaliser un `docker-compose build`, puis un `docker-compose up`.

Une fois cela réalisé, vous serez en mesure d'accéder à l'application :
- localhost:8000 vous amènera sur la page web d'accueil.
- localhost:8000/***app1*** est la première instance de l'application *Codenames*.
- localhost:8000/***app2*** est la seconde instance de l'application *Codenames*.
- localhost:8000/***kibana*** permet l'accès à kibana retraçant ainsi la totalité des logs des applications *Codenames*.

## UML persistance concept

```plantuml
hide empty members
skinparam classAttributeIconSize 0


class Team {
    - int id
}

class Game{
    - int id
    - Team id_team_blue
    - Team id_team_red
    - Team id_team_winner
}

class Player {
    - int id
    - String username
}

class TeamPlayer{
    - int id_team
    - int id_player
}

TeamPlayer "2..*" -- "1" Team
TeamPlayer "1..*" -- "1" Player
Game "1..*" -- "2" Team
```


## UML Java concept

```plantuml
hide empty members
skinparam classAttributeIconSize 0

enum Role {
    SPY
    DECODER
}

enum CardType {
    BLUE
    RED
    TRAP
    NEUTRAL
}

enum State {
    START
    BLUESPY
    REDSPY
    BLUEDECODER
    REDDECODER
    END
}

class Game {
    - Team teamA
    - Team teamB
    - State state
    - Grid grid
    - String clue
    - int clueOccurence
    void initGame()
}

class Team {
    - ArrayList<Player> players
    - boolean hasWon
}

class Player {
    - String username
    - Role role
}

class Grid {
    - ArrayList<Card> cards
    void initGrid(CardType cardType)
    void clearGrid()
}

class Card {
    - CardType type
    - String name
    - ArrayList<Player> players
    - boolean found
}

Team " " --> "2..4" Player : contains
Game " " --> "2" Team : include
Game " " --> "1" Grid : has
Grid " " --> "25" Card : contains
Card " " --> "0..*" Player : is chosen by
```
