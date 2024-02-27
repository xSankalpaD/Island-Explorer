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
            case NORTH:
                myDir = Direction.EAST;
                coords.x++;
                coords.y--;
                break;
            case EAST:
                myDir = Direction.SOUTH;
                coords.x++;
                coords.y++;
                break;
            case WEST:
                myDir = Direction.NORTH;
                coords.x--;
                coords.y--;
                break;
            case SOUTH:
                myDir = Direction.WEST;
                coords.x--;
                coords.y++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }

    public void turnLeft() {
        switch (myDir) {
            case NORTH:
                myDir = Direction.WEST;
                coords.x--;
                coords.y--;
                break;
            case EAST:
                myDir = Direction.NORTH;
                coords.x++;
                coords.y--;
                break;
            case WEST:
                myDir = Direction.SOUTH;
                coords.x--;
                coords.y++;
                break;
            case SOUTH:
                myDir = Direction.EAST;
                coords.x++;
                coords.y++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }

    public void goForward() {
        switch (myDir) {
            case NORTH:
                coords.y--;
                break;
            case EAST:
                coords.x++;
                break;
            case WEST:
                coords.x--;
                break;
            case SOUTH:
                coords.y++;
                break;
            default:
                myDir = Direction.NORTH;
        }
    }
}