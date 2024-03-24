package ca.mcmaster.se2aa4.island.team209.POI;

import ca.mcmaster.se2aa4.island.team209.Position.Point;

public class POI {
    final Point location;
    final String name;

    public POI(String the_name, Point the_location) {
        name = the_name;
        location = the_location;
    }

    public POI closerPoint(POI a, POI b) {
        long distanceFromA;
        long distanceFromB;
        distanceFromA = (long) (Math.pow((a.location.x - this.location.x), 2))
                + (long) (Math.pow((a.location.y - this.location.y), 2));
        distanceFromB = (long) (Math.pow((b.location.x - this.location.x), 2))
                + (long) (Math.pow((b.location.y - this.location.y), 2));

        if (distanceFromA < distanceFromB) {
            return a;
        }
        return b;
    }
}
