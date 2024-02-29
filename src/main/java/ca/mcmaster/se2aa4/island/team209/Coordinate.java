package ca.mcmaster.se2aa4.island.team209;

public class Coordinate {
    private int x;
    private int y;
    public Coordinate(int the_x,int the_y){
        x = the_x;
        y = the_y;
    }
    public void setX(int new_x){
        x = new_x;
    }
    public void setY(int new_y){
        y =new_y;
    }
    public int getY(){
        return y;
    }
    public int getX(){
        return x;
    }

}
