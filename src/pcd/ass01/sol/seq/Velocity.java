/*
 *   V2d.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package pcd.ass01.sol.seq;

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
