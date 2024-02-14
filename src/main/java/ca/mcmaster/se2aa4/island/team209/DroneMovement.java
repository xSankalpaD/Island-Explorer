package ca.mcmaster.se2aa4.island.team209;

public class DroneMovement extends Drone {
    
    public int batteryUsagePerTurn = 0;
    public void __init__(int x, int y, int Battery, Direction dir){
        coords[0] = x;
        coords[1] = y;
        battery = Battery;
        myDir = dir; 
    }
    public void turnRight(){
        switch (myDir){
            case NORTH:
                myDir = Direction.EAST;
                coords[0]++;
                coords[1]--;
                break;
            case EAST:
                myDir = Direction.SOUTH;
                coords[0]++;
                coords[1]++;
                break;
            case WEST:
                myDir = Direction.NORTH;
                coords[0]--;
                coords[1]--;
                break;
            case SOUTH:
                myDir = Direction.WEST;
                coords[0]--;
                coords[1]++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }
    
    public void turnLeft(){
        switch (myDir){
            case NORTH:
                myDir = Direction.WEST;
                coords[0]--;
                coords[1]--;
                break;
            case EAST:
                myDir = Direction.NORTH;
                coords[0]++;
                coords[1]--;
                break;
            case WEST:
                myDir = Direction.SOUTH;
                coords[0]--;
                coords[1]++;
                break;
            case SOUTH:
                myDir = Direction.EAST;
                coords[0]++;
                coords[1]++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }
    public void goForward(){
        switch (myDir){
            case NORTH:
                coords[1]--;
                break;
            case EAST:
                coords[0]++;
                break;
            case WEST:
                coords[0]--;
                break;
            case SOUTH:
                coords[1]++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }  
}