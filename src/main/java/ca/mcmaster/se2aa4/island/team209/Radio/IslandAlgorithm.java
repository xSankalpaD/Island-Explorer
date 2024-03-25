package ca.mcmaster.se2aa4.island.team209.Radio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team209.Position.Direction;
import ca.mcmaster.se2aa4.island.team209.Position.Point;
import ca.mcmaster.se2aa4.island.team209.ExploringDrone;
import ca.mcmaster.se2aa4.island.team209.JSONMover;
import ca.mcmaster.se2aa4.island.team209.Movement;
import ca.mcmaster.se2aa4.island.team209.POI.NearestCreekToSitePOIHandler;
import ca.mcmaster.se2aa4.island.team209.POI.POI;
import ca.mcmaster.se2aa4.island.team209.POI.POIHandler;

import java.io.StringReader;

public class IslandAlgorithm implements ExploreAlgorithm {
    private int distancetoEdge;
    private int distancetoLand;
    private final POIHandler poiHandler;
    private final ExploringDrone drone;
    private JSONObject data;
    private State state;
    private final Logger logger = LogManager.getLogger();
    private Direction scanDirection;
    private Point scanstartLocation;
    private boolean scanStart;
    private final Movement mover;

    private enum State {
        findWidth, findLand, moveToIsland, scanStrip, preTurn, turn, checkTurn, turnToOther, stop
    }

    public IslandAlgorithm(String s) {
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String givenDirection = info.getString("heading");
        Direction direction = switch (givenDirection) {
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
        scanStart = false;
    }

    @Override
    public String decision() {
        if (drone.getBattery() < Math.abs(drone.getCoordinates().x) + Math.abs(drone.getCoordinates().y))
            return "{ \"action\": \"stop\" }";// if battery is getting low based on distance
        if (mover.needsInstruction()) {//
            switch (state) {

                case findWidth -> mover.useRadar(drone.getDirection());

                case findLand -> decisionFindLand();
                case moveToIsland -> decisionMoveToIsland();
                case scanStrip -> decisionScanStrip();
                case turn -> decisionTurn();
                case preTurn -> decisionPreTurn();
                case turnToOther -> decisionTurnToOther();
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

    @SuppressWarnings("incomplete-switch")
    @Override
    public void takeInfo(String s) {
        JSONObject mixedInfo = new JSONObject(new JSONTokener(new StringReader(s)));
        drone.loseBattery(mixedInfo.getInt("cost"));
        data = mixedInfo.getJSONObject("extras");
        if (scanStart && scanstartLocation.equals(drone.getCoordinates())) {// if whole island is scanned will arrive
            // at same location.
            state = State.stop;
        }
        if (data.has("creeks")) { // check for creeks
            JSONArray creekArray = data.getJSONArray("creeks");
            if (!creekArray.isEmpty()) {
                String creek_name = creekArray.getString(0);
                POI found_creek = new POI(creek_name, drone.getCoordinates());
                poiHandler.addPoint("creek", found_creek);
            }
        }

        if (data.has("sites")) {// check for sites
            JSONArray siteArray = data.getJSONArray("sites");
            if (!siteArray.isEmpty()) {
                POI site = new POI(siteArray.getString(0), drone.getCoordinates());
                poiHandler.addPoint("site", site);
            }

        }
        switch (state) {
            case findWidth -> {
                if (data.has("found")) {
                    if (data.getString("found").equals("OUT_OF_RANGE")) {
                        distancetoEdge = data.getInt("range");
                        state = State.findLand;
                    } else if (data.getString("found").equals("GROUND")) {
                        state = State.moveToIsland;
                        distancetoLand = data.getInt("range");
                        scanDirection = drone.getDirection().left();
                    }
                }
            }
            case findLand -> {
                if (data.has("found") && (data.getString("found").equals("GROUND"))) {
                    state = State.moveToIsland;
                    distancetoLand = data.getInt("range");
                    scanDirection = drone.getDirection();

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
                        mover.useRadar(scanDirection);
                    } else if (data.getString("found").equals("GROUND")) {
                        distancetoLand = data.getInt("range");
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

    private void decisionFindLand() {
        if (distancetoEdge > 1) {
            mover.goForward();
            if (drone.getLastScan() == drone.getDirection().right()) {
                mover.useRadar(drone.getDirection().left()); // alternate right and left.
            } else
                mover.useRadar(drone.getDirection().right());
            distancetoEdge--;
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

    private void decisionMoveToIsland() {
        if (distancetoLand == 1)
            mover.scan(); // Later addition because some land edges were not scanned
        if (distancetoLand != 0) {
            mover.goDirection(drone.getLastScan());
            distancetoLand--;
        } else {
            if (!scanStart) {
                scanstartLocation = drone.getCoordinates();
                scanStart = true;
                mover.goDirection(drone.getLastScan());
            }
            mover.scan();
            state = State.scanStrip;

        }
    }

    private void decisionScanStrip() {
        mover.goForward();
        mover.scan();
    }

    private void decisionTurn() {
        if (drone.getDirection().right() == scanDirection) {
            mover.goRight();
            mover.goRight();
        } else if (drone.getDirection().left() == scanDirection) {
            mover.goLeft();
            mover.goLeft();
        }
        state = State.checkTurn;
        mover.useRadar(drone.getDirection());
    }

    private void decisionPreTurn() {
        mover.goForward();
        mover.useRadar(scanDirection);
    }

    private void decisionTurnToOther() {
        if (drone.getDirection().right() == scanDirection) {
            mover.goLeft();
            mover.goForward();
            mover.goLeft();
            mover.goLeft();
            mover.goLeft();
        } else if (drone.getDirection().left() == scanDirection) {
            mover.goRight();
            mover.goForward();
            mover.goRight();
            mover.goRight();
            mover.goRight();
        }
        scanDirection = scanDirection.right().right();
        mover.useRadar(drone.getDirection());
    }
}
