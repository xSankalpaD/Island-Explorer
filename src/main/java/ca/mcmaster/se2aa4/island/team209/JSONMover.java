package ca.mcmaster.se2aa4.island.team209;

import java.util.ArrayDeque;
import java.util.Queue;

import ca.mcmaster.se2aa4.island.team209.Position.Direction;

public class JSONMover implements Movement {
    private final Queue<String> decisions;
    private final ExploringDrone drone;

    public JSONMover(ExploringDrone Drone) {
        decisions = new ArrayDeque<>();
        drone = Drone;
    }

    @Override
    public void goRight() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().right().toString() + "\" } }");
        drone.turnRight();
    }

    @Override
    public void goLeft() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().left().toString() + "\" } }");
        drone.turnLeft();
    }

    @Override
    public void goForward() {
        decisions.add("{ \"action\": \"fly\" }");
        drone.goForward();
    }

    @Override
    public void useRadar(Direction d) {
        decisions.add("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"" + d.toString() + "\" } }");
        drone.setLastScan(d);
    }

    @Override
    public boolean needsInstruction() {
        return decisions.isEmpty();
    }

    @Override
    public void goDirection(Direction direction) {
        if (direction == drone.getDirection()) {
            goForward();
        } else if (direction == drone.getDirection().right()) {
            decisions.add(
                    "{ \"action\": \"heading\", \"parameters\": { \"direction\": \"" + direction.toString() + "\" } }");
            drone.turnRight();
        } else if (direction == drone.getDirection().left()) {
            decisions.add(
                    "{ \"action\": \"heading\", \"parameters\": { \"direction\": \"" + direction.toString() + "\" } }");
            drone.turnLeft();
        }

    }

    @Override
    public void scan() {
        decisions.add("{ \"action\": \"scan\" }");
    }

    @Override
    public void stop() {
        decisions.add("{ \"action\": \"stop\" }");
    }

    @Override
    public String getNextInstruction() {
        return decisions.remove();
    }
}
