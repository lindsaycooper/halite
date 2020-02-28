import hlt.Command;
import hlt.Constants;
import hlt.Direction;
import hlt.EntityId;
import hlt.Game;
import hlt.GameMap;
import hlt.Log;
import hlt.MapCell;
import hlt.Player;
import hlt.Position;
import hlt.Ship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class HeapStrategy {
    private static final int MIN_SHIPS = 10;
    private static final int MAX_SHIPS = 25;
    private static final int LAST_TURN_CREATE_SHIPS = 200;

    private static MaxHeap MAX_HEAP;
    private static Game game;
    private static Random random;

    private Map<EntityId, Position> shipCell = new HashMap<>();

    public HeapStrategy(Game game, Random random) {
        HeapStrategy.game = game;
        HeapStrategy.random = random;

        generateHeap(game.gameMap);
    }

    public static void generateHeap(GameMap map) {
        int heapSize = map.width * map.height;
        MAX_HEAP = new MaxHeap(heapSize, map.cells[0][0]);

        for (int y = 0; y < 32; ++y) {
            for (int x = 0; x < 32; ++x) {
                MapCell currentCell = map.cells[y][x];
                MAX_HEAP.insert(currentCell);
            }
        }
    }


    private static boolean haveEnoughHalite(Game game) {
        return game.me.halite >= Constants.SHIP_COST;
    }

    private boolean removeDeadShips(Iterator<EntityId> shipIterator) {
        boolean removed = false;
        while(shipIterator.hasNext()){
            EntityId shipId = shipIterator.next();
            if(!game.me.ships.containsKey(shipId)){
                shipIterator.remove();;
                Log.log("removing collided ship "+ shipId);
                removed = true;
            }
        }
        return removed;
    }

    public ArrayList<Command> getCommand() {
        final ArrayList<Command> commandQueue = new ArrayList<>();
        final Player me = game.me;
        final GameMap gameMap = game.gameMap;

        removeDeadShips(me.ships.keySet().iterator());

        for (final Ship ship : me.ships.values()) {
            if (ship.isFull()) {
                shipCell.put(ship.id, me.shipyard.position);
            }

            if (gameMap.at(ship).halite < Constants.MAX_HALITE / 10 || ship.isFull()) {
                if (ship.position.equals(me.shipyard.position)) {
                    shipCell.put(ship.id, MAX_HEAP.extractMax().position);
                }

                Position shipIntendedPosition = shipCell.get(ship.id);
                if (ship.position.equals(shipIntendedPosition)) {
                    shipIntendedPosition = me.shipyard.position;
                    shipCell.put(ship.id, shipIntendedPosition);
                }

                Direction direction = navigate(ship, shipIntendedPosition);
                commandQueue.add(ship.move(direction));
            } else {
                commandQueue.add(ship.stayStill());
            }
        }

        if (shouldSpawn()) {
            commandQueue.add(game.me.shipyard.spawn());
        }

        return commandQueue;
    }

    private static boolean shouldSpawn() {
        if (!haveEnoughHalite(game)) {
            return false;
        }

        if (game.me.ships.values().size() > MAX_SHIPS) {
            return false;
        }

        if (game.gameMap.at(game.me.shipyard.position).isOccupied()) {
            return false;
        }

        if (game.turnNumber % 10 == 0 || game.turnNumber == 1) {
            if (game.me.ships.size() < MIN_SHIPS) {
                return true;
            }
            return game.turnNumber < LAST_TURN_CREATE_SHIPS;
        }
        return false;
    }

    public Direction navigate(Ship ship, Position targetPosition){

        for (Direction direction : game.gameMap.getUnsafeMoves(ship.position, targetPosition)) {
            Position targetPos = ship.position.directionalOffset(direction);

            if (!game.gameMap.at(targetPos).isOccupied()) {
                game.gameMap.at(targetPos).markUnsafe(ship);
                return direction;
            }
        }

        ArrayList<Direction> directionOptions = new ArrayList<>(Direction.ALL_CARDINALS);
        while(directionOptions.size() > 0){
            Direction direction = directionOptions.remove(random.nextInt(directionOptions.size()));
            final Position targetPos = ship.position.directionalOffset(direction);

            if (!game.gameMap.at(targetPos).isOccupied()) {
                game.gameMap.at(targetPos).markUnsafe(ship);
                return direction;
            }
        }
        return Direction.STILL;
    }
}
