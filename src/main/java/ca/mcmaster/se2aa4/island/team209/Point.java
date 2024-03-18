package ca.mcmaster.se2aa4.island.team209;

public class Point{
    public int x;
    public int y;
    public int value;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point staticPoint(){//deep copy
        return new Point(this.x,this.y);
    }
}
