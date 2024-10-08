package ca.mcmaster.se2aa4.island.team209.Position;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object ob) {
        if (ob instanceof Point p) {
            return ((this.x == p.x) && (this.y == p.y));
        }
        return false;
    }
}