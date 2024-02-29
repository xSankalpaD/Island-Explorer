package ca.mcmaster.se2aa4.island.team209;

import java.net.http.WebSocket;

abstract class Drone {
    protected Point coords = new Point(0, 0);
    protected int battery;
    protected Direction myDir;

    // public abstract void Drone(int x, int y, int Battery, Direction dir);
    public int getBattery() {
        return battery;
    }

    public Direction getDirection() {
        return myDir;
    }

}
