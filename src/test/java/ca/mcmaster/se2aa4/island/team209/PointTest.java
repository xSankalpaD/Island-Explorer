package ca.mcmaster.se2aa4.island.team209;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.team209.Position.Point;

import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    @Test
    public void testEquals() {
        Point point1 = new Point(1, 2);
        Point point2 = new Point(1, 2);
        Point point3 = new Point(3, 4);

        // Test equals with itself
        assertEquals(point1, point1);

        // Test equals with an equal point
        assertEquals(point1, point2);
        assertEquals(point2, point1);

        // Test equals with different points
        assertNotEquals(point1, point3);
        assertNotEquals(point2, point3);
    }

    @Test
    public void testNotEqualsNull() {
        Point point1 = new Point(1, 2);
        assertNotEquals(null, point1);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        Point point1 = new Point(1, 2);
        assertNotEquals("test", point1);
    }
}
