package ca.mcmaster.se2aa4.island.team209;

import java.net.http.WebSocket;

abstract class Drone {
    protected int[] coords = new int[2];
    protected int battery;
    protected Direction myDir; 
    public enum Direction{
        NORTH,
        EAST,
        WEST,
        SOUTH
    }
    public abstract void __init__(int x, int y, int Battery, Direction dir);
    public void setCoordinates(int x, int y){
        coords[0] = x;
        coords[1] = y;
    }
    public int[] getCoordinates(){
        return coords;
    } 
    public void setBattery(int Battery){
        battery = Battery;
    }
    public int getBattery(){
        return battery;
    }
    public void setDirection(Direction newDir){
        myDir = newDir;   
    }
    public Direction getDirection(){
        return myDir;
    }
}
