package ca.mcmaster.se2aa4.island.team209;

public abstract class Drone {

    private int battery;

    public Drone(int Battery) {
        this.battery = Battery;
    }

    public int getBattery() {
        return battery;
    }
    public void loseBattery(int amount) {
        battery -= amount;
    }

}
