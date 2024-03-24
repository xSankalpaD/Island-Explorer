package ca.mcmaster.se2aa4.island.team209;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team209.Position.Direction;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONMoverTest {
    Movement mover;
    ExploringDrone drone;

    @BeforeEach
    public void init() {
        drone = new ExploringDrone(0, 0, 10000, Direction.E);
        mover = new JSONMover(drone);
    }

    @Test
    public void emptyTest() {
        assertTrue(mover.needsInstruction());
        mover.goForward();
        assertFalse(mover.needsInstruction());
        mover.getNextInstruction();
        assertTrue(mover.needsInstruction());
    }

    @Test
    public void orderTest() {
        mover.goForward();
        mover.goRight();
        mover.stop();
        String s = mover.getNextInstruction();
        assertTrue(s.contains("fly"));
        s = mover.getNextInstruction();
        assertTrue(s.contains("heading") && s.contains("S"));
        s = mover.getNextInstruction();
        assertTrue(s.contains("stop"));
    }

    @Test
    public void radarScanTest() {
        mover.useRadar(drone.getDirection());
        mover.scan();
        String s = mover.getNextInstruction();
        assertTrue(s.contains("echo") &&
                s.contains("E"));
        s = mover.getNextInstruction();
        assertTrue(s.contains("scan"));
    }
}
