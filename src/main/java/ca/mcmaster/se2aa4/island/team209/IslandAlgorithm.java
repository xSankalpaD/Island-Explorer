package ca.mcmaster.se2aa4.island.team209;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class IslandAlgorithm implements ExploreAlgorithm {
    POI nearestCreek;
    ArrayList<POI> creeks;
    int distance_to_edge, distance_to_land;
    Queue<String> decisions = new ArrayDeque<>();
    ExploringDrone drone;
    JSONObject data;
    State state;
    Direction scan_direction;
    Point scan_start_location;
    Movement mover;
    POI site;

    private enum State {
        findWidth, findLand, moveToIsland, scanStrip, preTurn, turn, checkTurn, turnToOther, stop
    }

    public IslandAlgorithm(String s) {
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String given_direction = info.getString("heading");
        Direction direction = switch (given_direction) {
            case "W" -> Direction.W;
            case "N" -> Direction.N;
            case "S" -> Direction.S;
            default -> Direction.E;
        };
        drone = new ExploringDrone(0, 0, info.getInt("budget"), direction, 0);
        data = new JSONObject();
        state = State.findWidth;
        nearestCreek = new POI("", new Point(Integer.MAX_VALUE/2,Integer.MAX_VALUE/2));
        mover = new JSONMover(decisions,drone);
        creeks = new ArrayList<>();
        site = new POI("",new Point(0,0));
    }

    @Override
    public String decision() {
        if (drone.battery < Math.abs(drone.coords.x) + Math.abs(drone.coords.y))
            return "{ \"action\": \"stop\" }";// if battery is getting low based on distance
        if (decisions.isEmpty()) {//
            switch (state) {
                case findWidth -> mover.useRadar(drone.getDirection());
                case findLand -> {
                    decision_findLand();
                }
                case moveToIsland -> {
                    decision_moveToIsland();
                }
                case scanStrip -> {
                    decision_scanStrip();
                }
                case turn -> {
                    decision_turn();
                }
                case preTurn -> {
                    decision_preTurn();
                }
                case turnToOther -> {
                    decision_turnToOther();
                }
                case stop -> mover.stop();
                default -> mover.stop();
            }
        }
        return decisions.remove();
    }

    @Override
    public void takeInfo(String s) {
        JSONObject mixed_info = new JSONObject(new JSONTokener(new StringReader(s)));
        drone.battery -= mixed_info.getInt("cost");
        data = mixed_info.getJSONObject("extras");
        if (data.has("creeks")) { //check for creeks
            JSONArray creek_array = data.getJSONArray("creeks");
            if (!creek_array.isEmpty()){
                String creek_name = creek_array.getString(0);
                POI found_creek = new POI(creek_name, drone.coords.staticPoint());
                nearestCreek = site.closerPoint(found_creek,nearestCreek);
                creeks.add(found_creek);
            }

        }
        if (data.has("sites")) {//check for sites
            JSONArray site_array = data.getJSONArray("sites");
            if (!site_array.isEmpty()){
                site = new POI(site_array.getString(0),drone.coords.staticPoint());
                for (POI creek: creeks){
                    nearestCreek = site.closerPoint(creek,nearestCreek);
                }
            }

        }
        switch (state) {
            case findWidth -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE")) {
                        distance_to_edge = data.getInt("range");
                        state = State.findLand;
                    } else if (data.getString("found").equals("GROUND")) {
                        state = State.moveToIsland;
                        distance_to_land = data.getInt("range");
                    }
                }
            }
            case findLand -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("GROUND")) {
                        state = State.moveToIsland;
                        distance_to_land = data.getInt("range");
                    }
                }
            }
            case scanStrip -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE")) {
                        state = State.preTurn;
                        mover.useRadar(scan_direction);
                    }
                }
            }
            case preTurn -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE")||
                            data.getString("found").equals("GROUND") && data.getInt("range") > 2) {
                        state = State.turn;
                    }
                }
            }
            case checkTurn -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("GROUND")) {
                        state = State.scanStrip;
                        mover.scan();
                    } else {
                        state = State.turnToOther;
                    }
                }
            }
            case turnToOther -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("GROUND")) {
                        state = State.scanStrip;
                        mover.scan();
                    } else if (data.getString("found").equals("OUT_OF_RANGE")) {// if ground is not found
                        if (drone.getDirection().right() == scan_direction) {
                            mover.goRight();
                            mover.goRight();
                            mover.goLeft();
                            mover.goRight();
                            mover.goRight();
                            mover.goRight();
                        } else if (drone.getDirection().left() == scan_direction) {
                            mover.goLeft();
                            mover.goLeft();
                            mover.goRight();
                            mover.goLeft();
                            mover.goLeft();
                            mover.goLeft();
                        }
                   }
                }
            }
            case moveToIsland, turn -> {
                return;
            }
            case stop -> {
                return;
            }
        }
    }

    @Override
    public String finalReport() {
        return nearestCreek.name;
    }

    private void goDirection(Direction direction) {
        if (direction == drone.getDirection()) {
            mover.goForward();
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
/* 
    private void goRight() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().right().toString() + "\" } }");
        drone.turnRight();
    }

    private void goLeft() {
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""
                + drone.getDirection().left().toString() + "\" } }");
        drone.turnLeft();
    }

    private void goForward() {
        decisions.add("{ \"action\": \"fly\" }");
        drone.goForward();
    }

    private void useRadar(Direction d) {
        decisions.add("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"" + d.toString() + "\" } }");
        drone.setLastScan(d);
    }

    private void scan() {
        decisions.add("{ \"action\": \"scan\" }");
    }

    private void stop() {
        decisions.add("{ \"action\": \"stop\" }");
    }
*/
    private void decision_findLand() {
        if (distance_to_edge > 1) {
            mover.goForward();
            if (drone.getLastScan() == drone.getDirection().right()) {
                mover.useRadar(drone.getDirection().left()); // alternate right and left.
            } else
                mover.useRadar(drone.getDirection().right());
            distance_to_edge--;
        } else {
            if (data.getInt("range") == 0 &&
                    (drone.getLastScan() == drone.getDirection().right())) { // if near a wall and cant turn in
                                                                             // direction
                goDirection(drone.getDirection().left()); // go other way
            } else {
                goDirection(drone.getDirection().right());
            }
        }
    }

    private void decision_moveToIsland() {
        if (distance_to_land != 1) {
            goDirection(drone.getLastScan());
            distance_to_land--;
        } else {
            mover.scan();
            scan_start_location = drone.coords;
            state = State.scanStrip;
            scan_direction = drone.getDirection().left();
        }
    }

    private void decision_scanStrip() {
        mover.goForward();
        mover.scan();
        mover.useRadar(drone.getDirection());
    }

    private void decision_turn() {
        if (drone.getDirection().right() == scan_direction) {
            mover.goRight();
            mover.goRight();
        } else if (drone.getDirection().left() == scan_direction) {
            mover.goLeft();
            mover.goLeft();
        }
        state = State.checkTurn;
        mover.useRadar(drone.getDirection());
    }

    private void decision_preTurn() {
        mover.goForward();
        mover.useRadar(scan_direction);
    }

    private void decision_turnToOther() {
        if (drone.getDirection().right() == scan_direction) {
            mover.goLeft();
            mover.goForward();
            mover.goLeft();
            mover.goLeft();
            mover.goLeft();
        } else if (drone.getDirection().left() == scan_direction) {
            mover.goRight();
            mover.goForward();
            mover.goRight();
            mover.goRight();
            mover.goRight();
        }
        scan_direction = scan_direction.right().right();
        mover.useRadar(drone.getDirection());
    }
}
