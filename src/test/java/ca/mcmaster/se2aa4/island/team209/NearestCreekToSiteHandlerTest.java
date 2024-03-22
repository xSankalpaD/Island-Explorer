package ca.mcmaster.se2aa4.island.team209;

import ca.mcmaster.se2aa4.island.team209.POI.NearestCreekToSitePOIHandler;
import ca.mcmaster.se2aa4.island.team209.POI.POI;
import ca.mcmaster.se2aa4.island.team209.POI.POIHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class NearestCreekToSiteHandlerTest {
    POIHandler poiHandler;
    @BeforeEach
    public void init(){
        poiHandler= new NearestCreekToSitePOIHandler();
    }
    @Test
    public void testSites(){
        poiHandler.addPoint("site", new POI("SITENAME", new Point(1,1)));
        poiHandler.addPoint("creek", new POI("CREEK1", new Point(4,7)));
        poiHandler.addPoint("creek", new POI("CREEK2", new Point(-43,-30)));
        poiHandler.addPoint("creek", new POI("CREEK3", new Point(2222,22222)));
        poiHandler.addPoint("creek", new POI("CREEK4", new Point(3,4)));
        String report = poiHandler.getReport();
        assertTrue(report.contains("SITENAME")&& report.contains("CREEK4"));
    }
    @Test
    public void testNoSites(){
        poiHandler.addPoint("creek", new POI("CREEK1", new Point(4,7)));
        poiHandler.addPoint("creek", new POI("CREEK2", new Point(-43,-30)));
        poiHandler.addPoint("creek", new POI("CREEK3", new Point(2222,22222)));
        poiHandler.addPoint("creek", new POI("CREEK4", new Point(3,4)));
        String report = poiHandler.getReport();
        assertTrue( report.contains("CREEK4")||report.contains("CREEK3")||report.contains("CREEK2")||report.contains("CREEK1"));
    }
    @Test
    public void testNothing(){
        assertEquals("no site or creek found" , poiHandler.getReport());
    }
}

