package ca.mcmaster.se2aa4.island.team209;

public class ExploringDrone extends Drone {
    
    public int batteryUsagePerTurn = 0;
    public void __init__(int x, int y, int Battery, Direction dir){
        coords = new Coordinate(x,y);
        battery = Battery;
        myDir = dir; 
    }
    public void turnRight(){
        switch (myDir){
            case NORTH:
                myDir = Direction.EAST;
                coords.setX(coords.getX()+1);
                coords.setY(coords.getY()-1);
                break;
            case EAST:
                myDir = Direction.SOUTH;
                coords.setX(coords.getX()+1);
                coords.setY(coords.getY()+1);
                break;
            case WEST:
                myDir = Direction.NORTH;
                coords.setX(coords.getX()-1);
                coords.setY(coords.getY()-1);
                break;
            case SOUTH:
                myDir = Direction.WEST;
                coords.setX(coords.getX()-1);
                coords.setY(coords.getY()+1);
                break;
            default:
                myDir = Direction.NORTH;
        }
    }
    
    public void turnLeft(){
        switch (myDir){
            case NORTH:
                myDir = Direction.WEST;
                coords.setX(coords.getX()-1);
                coords.setY(coords.getY()-1);
                break;
            case EAST:
                myDir = Direction.NORTH;
                coords.setX(coords.getX()+1);
                coords.setY(coords.getY()-1);
                break;
            case WEST:
                myDir = Direction.SOUTH;
                coords.setX(coords.getX()-1);
                coords.setY(coords.getY()+1);
                break;
            case SOUTH:
                myDir = Direction.EAST;
                coords.setX(coords.getX()+1);
                coords.setY(coords.getY()+1);
                break;
            default:
                myDir = Direction.NORTH;
        }
    }
    public void goForward(){
        switch (myDir){
            case NORTH:
                coords.setY(coords.getY()-1);
                break;
            case EAST:
                coords.setX(coords.getX()+1);
                break;
            case WEST:
                coords.setX(coords.getX()-1);
                break;
            case SOUTH:
                coords.setY(coords.getY()+1);
                break;
            default:
                myDir = Direction.NORTH;
        }
    }  
}