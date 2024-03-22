package ca.mcmaster.se2aa4.island.team209;

public abstract class Drone {
    protected Point coords;
    private int battery;
    protected Direction myDir;

    public Drone(int x, int y, int Battery, Direction dir) {
        this.coords = new Point(x, y);
        this.battery = Battery;
        this.myDir = dir;
    }

    public Point getCoordinates() {
        return coords;
    }

    public int getBattery() {
        return battery;
    }

    public Direction getDirection() {
        return myDir;
    }

    public void loseBattery(int amount) {
        battery -= amount;
    }

}
