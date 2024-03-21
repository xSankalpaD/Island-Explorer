package ca.mcmaster.se2aa4.island.team209;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testToString() {
        assertEquals("N", Direction.N.toString());
        assertEquals("E", Direction.E.toString());
        assertEquals("S", Direction.S.toString());
        assertEquals("W", Direction.W.toString());
    }

    @Test
    public void testRight() {
        assertEquals(Direction.S, Direction.E.right());
        assertEquals(Direction.W, Direction.S.right());
        assertEquals(Direction.N, Direction.W.right());
        assertEquals(Direction.E, Direction.N.right());
    }

    @Test
    public void testLeft() {
        assertEquals(Direction.N, Direction.E.left());
        assertEquals(Direction.W, Direction.N.left());
        assertEquals(Direction.S, Direction.W.left());
        assertEquals(Direction.E, Direction.S.left());
    }
}
