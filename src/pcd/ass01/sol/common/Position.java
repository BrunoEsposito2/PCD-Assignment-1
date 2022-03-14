package pcd.ass01.sol.common;

public class Position { 

    private double x, y;

    public Position(double x,double y){
        this.x = x;
        this.y = y;
    }

    public void change(double x, double y){
    	this.x = x;
    	this.y = y;
    }
    
    public double getX() {
    	return x;
    }

    public double getY() {
    	return y;
    }
}
