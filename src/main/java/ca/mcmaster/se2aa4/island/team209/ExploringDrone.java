package ca.mcmaster.se2aa4.island.team209;

public class ExploringDrone extends Drone {

    public int batteryUsagePerTurn = 0;

    public ExploringDrone(int x, int y, int Battery, Direction dir) {
        coords.x = x;
        coords.y = y;
        battery = Battery;
        myDir = dir;
    }

    public void turnRight() {
        switch (myDir) {
            case N:
                myDir = Direction.E;
                coords.x++;
                coords.y--;
                break;
            case E:
                myDir = Direction.S;
                coords.x++;
                coords.y++;
                break;
            case W:
                myDir = Direction.N;
                coords.x--;
                coords.y--;
                break;
            case S:
                myDir = Direction.W;
                coords.x--;
                coords.y++;
                break;
            default:
                myDir = Direction.N;
        }
    }

    public void turnLeft() {
        switch (myDir) {
            case N:
                myDir = Direction.W;
                coords.x--;
                coords.y--;
                break;
            case E:
                myDir = Direction.N;
                coords.x++;
                coords.y--;
                break;
            case W:
                myDir = Direction.S;
                coords.x--;
                coords.y++;
                break;
            case S:
                myDir = Direction.E;
                coords.x++;
                coords.y++;
                break;
            default:
                myDir = Direction.N;
        }
    }

    public void goForward() {
        switch (myDir) {
            case N:
                coords.y--;
                break;
            case E:
                coords.x++;
                break;
            case W:
                coords.x--;
                break;
            case S:
                coords.y++;
                break;
            default:
                myDir = Direction.N;
        }
    }
}