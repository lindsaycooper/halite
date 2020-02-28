// This Java API uses camelCase instead of the snake_case as documented in the API docs.
//     Otherwise the names of methods are consistent.

import hlt.Command;
import hlt.Game;

import java.util.ArrayList;
import java.util.Random;

public class HeapBot {

    public static void main(final String[] args) {
        final long rngSeed;
        if (args.length > 1) {
            rngSeed = Integer.parseInt(args[1]);
        } else {
            rngSeed = System.nanoTime();
        }
        final Random rng = new Random(rngSeed);

        Game game = new Game();
        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.

        HeapStrategy heapStrategy = new HeapStrategy(game, rng);

        game.ready("HeapBot");

        for (;;) {
            game.updateFrame();

            if (game.turnNumber % 200 == 0) {
                HeapStrategy.generateHeap(game.gameMap);
            }

            ArrayList<Command> commandQueue = heapStrategy.getCommand();

            game.endTurn(commandQueue);
        }
    }
}
