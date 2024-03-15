package ca.mcmaster.se2aa4.island.team209;


public interface Movement {
    void goRight();

    void goLeft();

    void goForward();

    void useRadar(Direction d);

    void scan();

    void stop();
}
