package ca.mcmaster.se2aa4.island.team209;

public class ExploringDrone extends Drone {
    private Direction lastScan;
    private final Point coords;
    private Direction myDir;

    public ExploringDrone(int x, int y, int Battery, Direction dir){
        super(Battery);
        this.coords = new Point(x, y);
        this.myDir = dir;
    }
    public void turnRight() {
        switch (myDir) {
            case N -> {
                this.myDir = Direction.E;
                coords.x++;
                coords.y--;
            }
            case E -> {
                myDir = Direction.S;
                coords.x++;
                coords.y++;
            }
            case W -> {
                myDir = Direction.N;
                coords.x--;
                coords.y--;
            }
            case S -> {
                myDir = Direction.W;
                coords.x--;
                coords.y++;
            }
            default -> myDir = Direction.N;
        }
    }

    public void turnLeft() {
        switch (myDir) {
            case N -> {
                myDir = Direction.W;
                coords.x--;
                coords.y--;
            }
            case E -> {
                myDir = Direction.N;
                coords.x++;
                coords.y--;
            }
            case W -> {
                myDir = Direction.S;
                coords.x--;
                coords.y++;
            }
            case S -> {
                myDir = Direction.E;
                coords.x++;
                coords.y++;
            }
            default -> myDir = Direction.N;
        }
    }

    public void goForward() {
        switch (myDir) {
            case N -> coords.y--;
            case E -> coords.x++;
            case W -> coords.x--;
            case S -> coords.y++;
            default -> myDir = Direction.N;
        }
    }
    public Direction getLastScan(){
        return lastScan;
    }
    public void setLastScan(Direction d){
        lastScan = d;
    }
    public Direction getDirection() {
        return myDir;
    }
    public Point getCoordinates(){//deep copy prevent modification
        return new Point(coords.x,coords.y);
    }
}