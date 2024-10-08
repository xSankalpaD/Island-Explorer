package ca.mcmaster.se2aa4.island.team209;

import ca.mcmaster.se2aa4.island.team209.Position.Direction;

public interface Movement {
    boolean needsInstruction();

    void goDirection(Direction direction);

    void goRight();

    void goLeft();

    void goForward();

    void useRadar(Direction d);

    void scan();

    void stop();

    String getNextInstruction();
}
