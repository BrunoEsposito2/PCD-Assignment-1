package pcd.ass01.sol.common;

/**
 *
 * 2-dimensional vector
 * objects are completely state-less
 *
 */
public class Velocity  {

    public double x,y;

    public Velocity(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void change(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
    public double getX() {
    	return x;
    }

    public double getY() {
    	return y;
    }
    
    public double getModule() {
    	return x*x + y*y;
    }
    
}
