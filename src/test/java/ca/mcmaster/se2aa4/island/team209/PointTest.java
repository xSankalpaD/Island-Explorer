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
        assertTrue(point1.equals(point1));

        // Test equals with an equal point
        assertTrue(point1.equals(point2));
        assertTrue(point2.equals(point1));

        // Test equals with different points
        assertFalse(point1.equals(point3));
        assertFalse(point2.equals(point3));
    }

    @Test
    public void testNotEqualsNull() {
        Point point1 = new Point(1, 2);
        assertFalse(point1.equals(null));
    }

    @Test
    public void testNotEqualsDifferentClass() {
        Point point1 = new Point(1, 2);
        assertFalse(point1.equals("test"));
    }
}
