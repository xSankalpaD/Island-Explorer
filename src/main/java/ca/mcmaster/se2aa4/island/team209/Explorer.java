package ca.mcmaster.se2aa4.island.team209;

import java.io.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Explorer implements IExplorerRaid {
    Direction direction;
    private final Logger logger = LogManager.getLogger();
    int state = 0;
    int counter = 0;
    String found;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}", info.toString(2));
        String direction = info.getString("heading");
        this.direction = Direction.valueOf(direction);
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();
        if (state == 0) {
            decision.put("action", "fly");
            logger.info("** Decision: {}", decision.toString());
            state = 1;

        } else if (state == 1) {
            JSONObject dir = new JSONObject();

            decision.put("action", "echo");
            dir.put("direction", "S");
            decision.put("parameters", dir);
            state = 2;

        } else if (state == 2) {

            decision.put("action", "scan");
            state = 0;
            counter++;
            if (counter == 50) {
                decision.put("action", "stop");
            }

        }

        logger.info("** Decision: {}");
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n" + response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);

    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
