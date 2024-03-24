package ca.mcmaster.se2aa4.island.team209;

import ca.mcmaster.se2aa4.island.team209.Position.Point;
import ca.mcmaster.se2aa4.island.team209.POI.POI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class POITest {
    @Test
    public void closerTest() {
        POI testPoint = new POI("test", new Point(100, 100));
        POI a = new POI("a", new Point(10, 10));
        POI b = new POI("b", new Point(1001, 1001));
        assertEquals(testPoint.closerPoint(a, b), a);
    }
}
