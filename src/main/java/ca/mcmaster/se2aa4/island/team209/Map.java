package ca.mcmaster.se2aa4.island.team209;

import eu.ace_design.island.game.PointOfInterest;
import java.util.*;

public class Map {
    private int[][] terrain; // Representation of the terrain
    private List<Point> creeks; // List of creeks on the island
    private Point emergencySite; // Location of the emergency site

    public Map(List<Point> creeks, Point emergencySite) {
        // this.terrain = terrain;
        this.creeks = creeks;
        this.emergencySite = emergencySite;
    }

    public int[][] getTerrain() {
        return terrain;
    }

    public void setTerrain(int[][] terrain) {
        this.terrain = terrain;
    }

    public List<Point> getCreeks() {
        return creeks;
    }

    public void setCreeks(List<Point> creeks) {
        this.creeks = creeks;
    }

    // get site
    public Point getEmergencySite() {
        return emergencySite;
    }

    public void setEmergencySite(Point emergencySite) {
        this.emergencySite = emergencySite;
    }

}
