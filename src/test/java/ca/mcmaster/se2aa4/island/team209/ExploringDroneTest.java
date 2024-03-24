package ca.mcmaster.se2aa4.island.team209;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team209.Position.Direction;
import ca.mcmaster.se2aa4.island.team209.Position.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExploringDroneTest {
    ExploringDrone drone;

    @BeforeEach
    public void init() {
        drone = new ExploringDrone(0, 0, 10000, Direction.E);
    }

    @Test
    public void initTest() {
        assertEquals(new Point(0, 0), drone.getCoordinates());
        assertEquals(10000, drone.getBattery());
        assertEquals(Direction.E, drone.getDirection());
    }

    @Test
    public void forwardTest() {
        drone.goForward();
        assertEquals(new Point(1, 0), drone.getCoordinates());
    }

    @Test
    public void turnTest() {
        drone.turnRight();
        assertEquals(new Point(1, 1), drone.getCoordinates());
        assertEquals(Direction.S, drone.getDirection());
        drone.turnLeft();
        assertEquals(new Point(2, 2), drone.getCoordinates());
        assertEquals(Direction.E, drone.getDirection());
    }

    @Test
    public void scanTest() {
        drone.setLastScan(Direction.W);
        assertEquals(Direction.W, drone.getLastScan());
    }

    @Test
    public void batteryTest() {
        assertEquals(10000, drone.getBattery());
        drone.loseBattery(1000);
        assertEquals(9000, drone.getBattery());
    }
}
