package ca.mcmaster.se2aa4.island.team209;

import java.util.Queue;

public class JSONMover implements Movement {
    private Queue<String> decisions;
    private ExploringDrone drone;
    public JSONMover(Queue<String> Decisions, ExploringDrone Drone){
        decisions=Decisions;
        drone=Drone;
    }
    public void goRight() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().right().toString() + "\" } }");
        drone.turnRight();
    }

    public void goLeft() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().left().toString() + "\" } }");
        drone.turnLeft();
    }

    public void goForward() {
        decisions.add("{ \"action\": \"fly\" }");
        drone.goForward();
    }

    public void useRadar(Direction d) {
        decisions.add("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"" + d.toString() + "\" } }");
        drone.setLastScan(d);
    }

    public void scan() {
        decisions.add("{ \"action\": \"scan\" }");
    }

    public void stop() {
        decisions.add("{ \"action\": \"stop\" }");
    } 
}
