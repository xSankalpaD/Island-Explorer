package ca.mcmaster.se2aa4.island.team209;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;

import java.util.ArrayDeque;
import java.util.Queue;


public class IslandAlgorithm implements ExploreAlgorithm {
    String nearestCreek;
    int distance_from_last_scan, distance_to_edge,distance_to_land;
    Queue<String> decisions = new ArrayDeque<>();
    ExploringDrone drone;
    JSONObject data;
    State state;

    private enum State{
        findWidth, findLand, moveToIsland, scanIsland, stop
    }
    public IslandAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String given_direction = info.getString("heading");
        Direction direction = switch (given_direction) {
            case "W" -> Direction.W;
            case "N" -> Direction.N;
            case "S" -> Direction.S;
            default -> Direction.E;
        };
        distance_from_last_scan = 0;
        drone = new ExploringDrone(0,0,info.getInt("budget"), direction,0);
        data = new JSONObject();
        state = State.findWidth;
    }
    @Override
    public String decision() {
        if (decisions.isEmpty()){//
            switch(state){
                case findWidth: useRadar(drone.getDirection());
                break;
                case findLand:{
                    if (distance_to_edge > 1){
                        goForward();
                        if (drone.getLastScan() == drone.getDirection().right()) {
                            useRadar(drone.getDirection().left()); //alternate right and left.
                        }
                        else useRadar(drone.getDirection().right());
                        distance_to_edge--;
                    }
                    else {
                        if (distance_from_last_scan==0 &&
                                (drone.getLastScan() == drone.getDirection().right())){ // if near a wall and cant turn in direction
                            goDirection(drone.getDirection().left()); //go other way
                        }
                        else{
                            goDirection(drone.getDirection().right());
                        }
                    }

                }
                break;
                case moveToIsland:{
                    if (distance_to_land != 0){
                        goDirection(drone.getLastScan());
                        distance_to_land--;
                    }
                    else {
                        scan();
                        state = State.scanIsland;
                    }
                }
                break;
                case scanIsland:{
                    scan();
                    state = State.stop;
                }
                break;
                case stop: decisions.add("{ \"action\": \"stop\" }");
                break;
                default: return"";
            }
        }
        return decisions.remove();
    }

    @Override
    public void takeInfo(String s) {
        JSONObject mixed_info = new JSONObject(new JSONTokener(new StringReader(s)));
        drone.battery-=mixed_info.getInt("cost");
        data = mixed_info.getJSONObject("extras");
        if (data.has("found")){
            distance_from_last_scan =data.getInt("range");
            if (data.getString("found").equals("OUT_OF_RANGE")&&state == State.findWidth )  {
                distance_to_edge = distance_from_last_scan;
                state = State.findLand;
            }
            else if (data.getString("found").equals("GROUND")){
                state = State.moveToIsland;
                distance_to_land = data.getInt("range");
            }
        }

    }

    @Override
    public String finalReport() {
        return nearestCreek;
    }
    private void goDirection(Direction direction){
        if (direction == drone.getDirection())  {
            goForward();
        }
        else{
            decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""+ direction.toString() +"\" } }");
            drone.turnRight();
        }

    }
    private void goRight(){
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""+ drone.getDirection().right().toString() +"\" } }");
        drone.turnRight();
    }
    private void goLeft(){
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""+ drone.getDirection().left().toString() +"\" } }");
        drone.turnLeft();
    }
    private void goForward(){
        decisions.add("{ \"action\": \"fly\" }");
        drone.goForward();
    }
    private void useRadar(Direction d){
        decisions.add("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"" + d.toString() + "\" } }");
        drone.setLastScan(d);
    }
    private void scan(){
        decisions.add("{ \"action\": \"scan\" }");
    }
}
