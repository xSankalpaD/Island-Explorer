package ca.mcmaster.se2aa4.island.team209;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.StringReader;

import java.util.ArrayDeque;
import java.util.Queue;


public class IslandAlgorithm implements ExploreAlgorithm {
    String nearestCreek;
    boolean island_found,width_found;
    int map_width; //since we know island is in center
    Queue<String> decisions = new ArrayDeque<>();
    ExploringDrone drone;
    public IslandAlgorithm(String s){
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        String given_direction = info.getString("heading");
        Direction direction = switch (given_direction) {
            case "W" -> Direction.W;
            case "N" -> Direction.N;
            case "S" -> Direction.S;
            default -> Direction.E;
        };
        map_width = 0;
        drone = new ExploringDrone(0,0,info.getInt("budget"), direction);
        island_found = false;
        width_found = false;

    }
    @Override
    public String decision() {
        if (decisions.isEmpty()){//
            if (!width_found){
                useRadar(drone.getDirection());
            }else if (!island_found){
                goRight();
                goRight();
                useRadar(drone.getDirection());

            }else{
                scan();
            }
        }
        return decisions.remove();
    }

    @Override
    public void takeInfo(String s) {
        JSONObject data = new JSONObject(new JSONTokener(new StringReader(s)));
        if (data.has("found")){
            if (data.getString("found").equals("OUT_OF_RANGE")&& !width_found)  {
                map_width = data.getInt("range");
                for (int i = 0 ; i <map_width;i+=2){
                    goForward();
                }
                goRight();
                width_found = true;
            }
            else if (data.getString("found").equals("GROUND")){
                island_found = true;
                width_found = true;
                int distance = data.getInt("range");
                for (int i = 0 ; i <=distance;i++) {
                    goForward();
                }
            }
        }

    }

    @Override
    public String finalReport() {
        return nearestCreek;
    }
    private void goRight(){
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""+ Direction.toString( Direction.right(drone.getDirection()) ) +"\" } }");
        drone.turnRight();
    }
    private void goLeft(){
        decisions.add("{ \"action\": \"heading\", \"parameters\": { \"direction\": \""+ Direction.toString( Direction.left(drone.getDirection()) ) +"\" } }");
        drone.turnLeft();
    }
    private void goForward(){
        decisions.add("{ \"action\": \"fly\" }");
        drone.goForward();
    }
    private void useRadar(Direction d){
        decisions.add("{ \"action\": \"echo\", \"parameters\": { \"direction\": \"" + Direction.toString(d) + "\" } }");
    }
    private void scan(){
        decisions.add("{ \"action\": \"scan\" }");
    }
}
