package ca.mcmaster.se2aa4.island.team209;

public class Point{
    public int x;
    public int y;
    public int value;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean closerToOrigin(Point p){
        double a_dist,b_dist;
        a_dist = this.x^2 + this.y^2;
        b_dist= p.x^2 + p.y^2;
        return a_dist < b_dist;
        }
    }
