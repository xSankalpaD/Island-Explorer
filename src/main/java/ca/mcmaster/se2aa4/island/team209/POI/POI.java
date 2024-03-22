package ca.mcmaster.se2aa4.island.team209.POI;

import ca.mcmaster.se2aa4.island.team209.Point;
import java.lang.Math;

public class POI {
    final Point location;
    final String name;// locations dont change duh

    public POI(String the_name, Point the_location) {
        name = the_name;
        location = the_location;
    }

    public POI closerPoint(POI a, POI b) {
        long a_dist, b_dist;
        a_dist = (long) (Math.pow((a.location.x - this.location.x), 2))
                + (long) (Math.pow((a.location.y - this.location.y), 2));
        b_dist = (long) (Math.pow((b.location.x - this.location.x), 2))
                + (long) (Math.pow((b.location.y - this.location.y), 2));

        if (a_dist < b_dist) {
            return a;
        }
        return b;
    }
}
