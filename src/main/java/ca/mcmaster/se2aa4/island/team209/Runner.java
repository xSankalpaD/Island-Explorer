package ca.mcmaster.se2aa4.island.team209;

import static eu.ace_design.island.runner.Runner.run;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team209.Radio.Explorer;

public class Runner {

    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {

        String filename = args[0];
        try {
            run(Explorer.class)
                    .exploring(new File(filename))
                    .withSeed(42L)
                    .startingAt(1, 1, "EAST")
                    .backBefore(50000)
                    .withCrew(5)
                    .collecting(1000, "WOOD")
                    .storingInto("./outputs")
                    .withName("Island")
                    .fire();
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage());
            e.printStackTrace(System.err);
            logger.error("Exiting application with error status");
        }
    }

}
