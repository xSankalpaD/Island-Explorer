package ca.mcmaster.se2aa4.island.team209;

import java.util.ArrayList;
import java.util.List;

import eu.ace_design.island.game.PointOfInterest;

public class IslandRunner {
    public static void main(String[] args) {
        String filename = args[0];
        try {
            Map islandMap = loadIslandMap(filename);

            int[][] terrain = islandMap.getTerrain();
            List<Point> scannedBiomes = new ArrayList<>();
            for (int i = 0; i < terrain.length; i++) {
                for (int j = 0; j < terrain[0].length; j++) {
                    int terrainValue = terrain[i][j];
                    Point biome = new Point(i, j); // need to add terrain value
                    scannedBiomes.add(biome);
                }
            }

            Explorer explorer = new Explorer();
            explorer.initialize("Initialization data goes here");

            String finalReport = explorer.deliverFinalReport();
            System.out.println("Final report: " + finalReport);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static Map loadIslandMap(String filename) {
        // Implement logic to load island map from file
        // For demonstration purposes, creating dummy data
        int[][] terrain = { { 0, 0, 0 }, { 1, 1, 1 }, { 2, 2, 2 } };
        List<Point> creeks = new ArrayList<>();
        Point emergencySite = new Point(3, 3); // add terrain value here too

        return new Map(terrain, creeks, emergencySite);
    }
}
