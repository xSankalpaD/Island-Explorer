package ca.mcmaster.se2aa4.island.team209;

import java.util.ArrayList;
import java.util.List;

import eu.ace_design.island.game.PointOfInterest;

public class IslandScanner {
    public static List<Point> scanIsland(int[][] terrain) {
        List<Point> scannedBiomes = new ArrayList<>();
        for (int i = 0; i < terrain.length; i++) {
            for (int j = 0; j < terrain[0].length; j++) {
                int terrainValue = terrain[i][j];
                Point biome = new Point(i, j); // still need to add terrain value
                scannedBiomes.add(biome);
            }
        }
        return scannedBiomes;
    }

}