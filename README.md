# Halite - Hack Days

Code written as part of a work-hosted [Halite-III](https://2018.halite.io)
competition.

This project used the Java starter kit from the official 
[Halite-III repo](https://github.com/HaliteChallenge/Halite-III)

### Setup
You can use a local halite executable, found on the official 
[Halite-III repo](https://github.com/HaliteChallenge/Halite-III)

1. Clone the [Halite-III repo](https://github.com/HaliteChallenge/Halite-III)
2. Follow README instructions to compile the executable on the
[Halite-III game engine](https://github.com/HaliteChallenge/Halite-III/blob/master/game_engine/README.md).
3. Move the executable to this repo.
    
    Example:
    ```
    cp ~/Halite-III/game_engine/halite .
    ```

### Run a game
Using the run_game script allows you to compile and run a game.

Example:
```
sh run_game.sh StarterBot HeapBot
```

When you run a game, some output is generated in log files and an 
`.hlt` file. The `.hlt` file can be dropped in 
[Halite visualizer](https://2018.halite.io/watch-games) 
to watch the game.
