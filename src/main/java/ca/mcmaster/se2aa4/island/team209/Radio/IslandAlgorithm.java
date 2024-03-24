package ca.mcmaster.se2aa4.island.team209.Radio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team209.Direction;
import ca.mcmaster.se2aa4.island.team209.ExploringDrone;
import ca.mcmaster.se2aa4.island.team209.JSONMover;
import ca.mcmaster.se2aa4.island.team209.Movement;
import ca.mcmaster.se2aa4.island.team209.Point;
import ca.mcmaster.se2aa4.island.team209.POI.NearestCreekToSitePOIHandler;
import ca.mcmaster.se2aa4.island.team209.POI.POI;
import ca.mcmaster.se2aa4.island.team209.POI.POIHandler;

import java.io.StringReader;

public class IslandAlgorithm implements ExploreAlgorithm {
    private int distance_to_edge, distance_to_land;
    private final POIHandler poiHandler;
    private final ExploringDrone drone;
    private JSONObject data;
    private State state;
    private final Logger logger = LogManager.getLogger();
    private Direction scan_direction;
    private Point scan_start_location;
    private boolean scan_start;
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
        drone = new ExploringDrone(1, 1, info.getInt("budget"), direction);
        data = new JSONObject();
        state = State.findWidth;
        mover = new JSONMover(drone);
        poiHandler = new NearestCreekToSitePOIHandler();
        scan_start = false;
    }

    @Override
    public String decision() {
        if (drone.getBattery() < Math.abs(drone.getCoordinates().x) + Math.abs(drone.getCoordinates().y))
            return "{ \"action\": \"stop\" }";// if battery is getting low based on distance
        if (mover.needsInstruction()) {//
            switch (state) {

                case findWidth -> mover.useRadar(drone.getDirection());

                case findLand -> decision_findLand();
                case moveToIsland -> decision_moveToIsland();
                case scanStrip -> decision_scanStrip();
                case turn -> decision_turn();
                case preTurn -> decision_preTurn();
                case turnToOther -> decision_turnToOther();
                case stop -> mover.stop();
                case checkTurn -> mover.stop();
            }

        }
        if (mover.needsInstruction()) {// if something has gone and the drone does not have valid instructions
            logger.info("no valid instructions, Stopping");
            mover.stop();
        }
        return mover.getNextInstruction();
    }

    @Override
    public void takeInfo(String s) {
        JSONObject mixed_info = new JSONObject(new JSONTokener(new StringReader(s)));
        drone.loseBattery(mixed_info.getInt("cost"));
        data = mixed_info.getJSONObject("extras");
        if (scan_start && scan_start_location.equals(drone.getCoordinates())) {// if whole island is scanned will arrive
            // at same location.
            state = State.stop;
        }
        if (data.has("creeks")) { // check for creeks
            JSONArray creek_array = data.getJSONArray("creeks");
            if (!creek_array.isEmpty()) {
                String creek_name = creek_array.getString(0);
                POI found_creek = new POI(creek_name, drone.getCoordinates());
                poiHandler.addPoint("creek", found_creek);
            }
        }

        if (data.has("sites")) {// check for sites
            JSONArray site_array = data.getJSONArray("sites");
            if (!site_array.isEmpty()) {
                POI site = new POI(site_array.getString(0), drone.getCoordinates());
                poiHandler.addPoint("site", site);
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
                        scan_direction = drone.getDirection().left();
                    }
                }
            }
            case findLand -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("GROUND")) {
                        state = State.moveToIsland;
                        distance_to_land = data.getInt("range");
                        scan_direction = drone.getDirection();
                    }
                }
            }
            case scanStrip -> {
                if (data.has("biomes")) {
                    JSONArray biomeArray = data.getJSONArray("biomes");
                    if (biomeArray.length() == 1 && biomeArray.getString(0).equals("OCEAN")) {
                        mover.useRadar(drone.getDirection());
                    }
                } else if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE")) {
                        state = State.preTurn;
                        mover.useRadar(scan_direction);
                    } else if (data.getString("found").equals("GROUND")) {
                        distance_to_land = data.getInt("range");
                        state = State.moveToIsland;
                        drone.setLastScan(drone.getDirection());
                        mover.goForward();

                    }
                }
            }
            case preTurn -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE") ||
                            data.getString("found").equals("GROUND") && data.getInt("range") > 2) {
                        state = State.turn;
                    }
                }
            }
            case checkTurn -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("GROUND")) {
                        // Changed to add step in before scanStrip
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
                        state = State.preTurn;

                    }
                }
            }

        }
    }

    @Override
    public String finalReport() {
        logger.info("Battery: " + drone.getBattery());
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
                mover.goLeft(); // go other way
            } else {
                mover.goRight();
            }
        }
    }

    private void decision_moveToIsland() {
        if (distance_to_land != 0) {
            mover.goDirection(drone.getLastScan());
            distance_to_land--;
        } else {
            if (!scan_start) {
                scan_start_location = drone.getCoordinates();
                scan_start = true;
                mover.goDirection(drone.getLastScan());
            }
            mover.scan();
            state = State.scanStrip;

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
