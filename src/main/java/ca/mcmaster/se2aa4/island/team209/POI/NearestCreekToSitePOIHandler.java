package ca.mcmaster.se2aa4.island.team209.POI;

import java.util.ArrayList;
import java.util.List;

public class NearestCreekToSitePOIHandler implements POIHandler {
    private final List<POI> creeks;
    private POI site;

    public NearestCreekToSitePOIHandler() {
        creeks = new ArrayList<>();
    }

    @Override
    public void addPoint(String type, POI poi) {
        if (type.equals("creek")) {
            creeks.add(poi);
        } else if (type.equals("site")) {
            site = poi;
        }
    }

    @Override
    public String getReport() {
        if (creeks.isEmpty()) {
            return "no site or creek found";
        }
        if (site == null) {
            return "no site found\n Possible creek:" + creeks.get(0).name;
        }

        POI nearestCreek = creeks.get(0);
        for (POI creek : creeks) {
            nearestCreek = site.closerPoint(creek, nearestCreek);
        }
        return nearestCreek.name + " " + site.name;
    }
}
