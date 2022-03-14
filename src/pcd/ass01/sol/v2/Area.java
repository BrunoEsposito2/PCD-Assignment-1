package pcd.ass01.sol.v2;

import java.util.ArrayList;
import java.util.Iterator;

import pcd.ass01.sol.common.Body;

public class Area {

	private ArrayList<Body> bodies;
	private ArrayList<Body> bodiesToAdd;

	private Area leftArea;
	private Area rightArea;
	private double leftBound, rightBound;
	private Simulation sim;
	
	private int nBodiesRemoved;
	
	public Area(Simulation sim, double from, double to) {
		this.sim = sim;
		bodies = new ArrayList<Body>();
		bodiesToAdd = new ArrayList<Body>();

		this.leftBound = from;
		this.rightBound = to;
	}

	public void setNeighbours(Area left, Area right) {
		this.leftArea = left;
		this.rightArea = right;
	}
		
	public void updateBodies() {
        for (Body b: bodies) {
        	b.updatePos(sim.getDT());
    	}
	}
	
	public void checkCollisions() {
    	for (int i = 0; i < bodies.size() - 1; i++) {
	    	Body b1 = bodies.get(i);
        	for (int j = i + 1; j < bodies.size(); j++) {
	        	Body b2 = bodies.get(j);
	            if (b1.collideWith(b2)) {
	            	Body.solveCollision(b1, b2);
	            }
        	}
    	}
	}
	
	public void checkBodiesToMoveOut() {
    	nBodiesRemoved = 0;    	
    	Iterator<Body> it = bodies.iterator();
    	while (it.hasNext()) {
    		Body b = it.next();

    		b.checkAndSolveBoundaryCollision(sim.getBounds());

	    	double x = b.getPos().getX();	        		
    		double distToRightBorder = rightBound - x;
    		double distToLeftBorder = x - leftBound;
    		
    		if (distToRightBorder < 0 && rightArea != null) {
    			rightArea.moveInBody(b);
    			it.remove();
    			nBodiesRemoved++;
    		} else if (distToLeftBorder < 0 && leftArea != null) {
    			leftArea.moveInBody(b);
    			it.remove();
    			nBodiesRemoved++;
    		}	
    		/*
    		 * @TODO checking body on boundary: 
    		 * (distToRightBorder < b.getRadius()) || (distToLeftBorder < b.getRadius())
    		 */		  
	    }		
	}
	
	public void checkBodiesMovingIn() {
		for (Body b: bodiesToAdd) {
			bodies.add(b);
		}
		bodiesToAdd.clear();
	}

	public void reset() {
		bodies.clear();
	}
	
	public ArrayList<Body> getBodies(){
		return bodies;
	}
	
	public void addBody(Body b) {
		bodies.add(b);
	}

	public void moveInBody(Body b) {
		bodiesToAdd.add(b);
	}
		public int getNumBodiesRemoved() {
		return nBodiesRemoved;
	}
	
	public int getNumBodiesAdded() {
		return bodiesToAdd.size();

	}

}
