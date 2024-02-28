package ca.mcmaster.se2aa4.island.team209;

import java.util.ArrayList;
import java.util.List;

import eu.ace_design.island.game.PointOfInterest;

public class IslandRunner implements ExploreAlgorithm {

    Explorer radio = new Explorer();

    ExploringDrone myDrone = new ExploringDrone(1, 1, 999999, Direction.EAST);

    public void topLeftIslandFinder() {
        while (true) {
            String a = radio.takeDecision();
            radio.acknowledgeResults(a);
        }
        // String b = radio.takeDecision(1);
        // System.out.println(b);
    }

    public void IslandTransversal() {
    }

    public static void main(String[] args) {
        String filename = args[0];
        try {

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

        List<Point> creeks = new ArrayList<>();
        Point emergencySite = new Point(3, 3); // add terrain value here too

        return new Map(creeks, emergencySite);
    }
}
