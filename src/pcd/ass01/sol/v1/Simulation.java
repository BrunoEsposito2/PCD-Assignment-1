package pcd.ass01.sol.v1;

import java.util.ArrayList;
import java.util.Random;

import pcd.ass01.sol.common.Body;
import pcd.ass01.sol.common.Boundary;
import pcd.ass01.sol.common.Position;
import pcd.ass01.sol.common.Velocity;

public class Simulation {
	
	ArrayList<Body> bodies;

	private double time;
	private long nIter;
	private int nBodies;
	private Boundary bounds; 
	private int nIterations;
	private double dt;
	
	public Simulation(int nBodies, int nIterations, double dt) {
		this.nIterations = nIterations;
		this.nBodies = nBodies;
		this.dt = dt;
		
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        bodies = new ArrayList<Body>();
	}

	public void init() {
        Random rand = new Random(System.currentTimeMillis());
        bodies.clear();
        for (int i = 0; i < nBodies; i++) {
            double x = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double y = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double dx = -1 + rand.nextDouble()*2;
            double speed = rand.nextDouble()*0.01;
            double dy = rand.nextDouble() > 0.5 ? Math.sqrt(1 - dx*dx) : -Math.sqrt(1 - dx*dx);
            Body b = new Body(new Position(x, y), new Velocity(dx*speed,dy*speed), 0.01);
            bodies.add(b);
        }
	
        time = 0;
		nIter = 0;
	}
	
	public void updateBodies() {
    	for (Body b: bodies) {
    		b.updatePos(dt);
    		b.checkAndSolveBoundaryCollision(bounds);
	    }
	}
	
	public void nextFrame() {
		time += dt;
		nIter++;
	}
	
	public boolean isCompleted() {
		return nIter >= nIterations;
	}
	
	public double getTime() {
		return time;
	}
	
	public long getNumIter() {
		return nIter;
	}

	public long getNumIterations() {
		return nIterations;
	}

	public ArrayList<Body> getBodies(){
		return bodies;
	}

	public int getNumBodies() {
		return bodies.size();
	}

	public double getDT() {
		return dt;
	}
	
	public Boundary getBounds() {
		return bounds;
	}
	
}
