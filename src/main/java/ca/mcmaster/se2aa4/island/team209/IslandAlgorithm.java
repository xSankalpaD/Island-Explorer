package ca.mcmaster.se2aa4.island.team209;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;
public class IslandAlgorithm implements ExploreAlgorithm {
    private int distance_to_edge, distance_to_land;
    private final POIHandler poiHandler;
    private final ExploringDrone drone;
    private JSONObject data;
    private State state;
    private Direction scan_direction;
    private Point scan_start_location;
    private final Movement mover;
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
        //drone = new ExploringDrone(info.getInt("x"), info.getInt("y"), info.getInt("budget"), direction);
        drone = new ExploringDrone(1, 1, info.getInt("budget"), direction);
        data = new JSONObject();
        state = State.findWidth;
        mover = new JSONMover(drone);
        poiHandler = new NearestCreekToSitePOIHandler();
        scan_start_location = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public String decision() {
        if (drone.battery < Math.abs(drone.coords.x) + Math.abs(drone.coords.y))
            return "{ \"action\": \"stop\" }";// if battery is getting low based on distance
        if (mover.needsInstruction()) {//
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
        if (mover.needsInstruction())//if something has gone and the drone does not have valid instructions
            mover.stop();
        return mover.getNextInstruction();
    }

    @Override
    public void takeInfo(String s) {
        JSONObject mixed_info = new JSONObject(new JSONTokener(new StringReader(s)));
        drone.battery -= mixed_info.getInt("cost");
        data = mixed_info.getJSONObject("extras");
        if (scan_start_location.equals(drone.coords)){//if whole island is scanned will arrive at same location.
            state = State.stop;
        }
        if (data.has("creeks")) { //check for creeks
            JSONArray creek_array = data.getJSONArray("creeks");
            if (!creek_array.isEmpty()){
                String creek_name = creek_array.getString(0);
                POI found_creek = new POI(creek_name, drone.staticPoint());
                poiHandler.addPoint("creek", found_creek);
            }
        }

        if (data.has("sites")) {//check for sites
            JSONArray site_array = data.getJSONArray("sites");
            if (!site_array.isEmpty()){
                POI site = new POI(site_array.getString(0),drone.staticPoint());
                poiHandler.addPoint("site",site);
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
                if (data.has("biomes")){
                    JSONArray biomeType = data.getJSONArray("biomes");
                    if (biomeType.length()==1 && biomeType.getString(0).equals("OCEAN")){
                        mover.useRadar(drone.getDirection());
                    }
                }
                else if (data.has("found")) {
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
                        // Changed to add step in before scanstrip
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
                    }
                    if (data.getString("found").equals("OUT_OF_RANGE")) {
                        state = State.preTurn;
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
        return poiHandler.getReport();
    }
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
                mover.goDirection(drone.getDirection().left()); // go other way
            } else {
                mover.goDirection(drone.getDirection().right());
            }
        }
    }

    private void decision_moveToIsland() {
        if (distance_to_land != 1) {
            mover.goDirection(drone.getLastScan());
            distance_to_land--;

        } else {
            scan_start_location = drone.staticPoint();
            mover.goDirection(drone.getLastScan());
            mover.scan();
            state = State.scanStrip;
            scan_direction = drone.getDirection().left();
        }
    }

    private void decision_scanStrip() {
        mover.goForward();
        mover.scan();
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
