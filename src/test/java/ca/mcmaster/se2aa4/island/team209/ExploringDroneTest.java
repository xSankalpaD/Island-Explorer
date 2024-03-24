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
        assertEquals(drone.getCoordinates(), new Point(0, 0));
        assertEquals(drone.getBattery(), 10000);
        assertEquals(drone.getDirection(), Direction.E);
    }

    @Test
    public void forwardTest() {
        drone.goForward();
        assertEquals(drone.getCoordinates(), new Point(1, 0));

    }

    @Test
    public void turnTest() {
        drone.turnRight();
        assertEquals(drone.getCoordinates(), new Point(1, 1));
        assertEquals(drone.getDirection(), Direction.S);
        drone.turnLeft();
        assertEquals(drone.getCoordinates(), new Point(2, 2));
        assertEquals(drone.getDirection(), Direction.E);
    }

    @Test
    public void scanTest() {
        drone.setLastScan(Direction.W);
        assertEquals(drone.getLastScan(), Direction.W);
    }

    @Test
    public void batteryTest() {
        assertEquals(drone.getBattery(), 10000);
        drone.loseBattery(1000);
        assertEquals(drone.getBattery(), 9000);
    }
}
