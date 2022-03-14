package pcd.ass01.sol.v1;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import pcd.ass01.sol.common.Body;
import pcd.ass01.sol.common.Flag;

public class CollisionChecker extends Thread {
	
	private int startIndex, nCollisions;
	private ArrayList<Body> bodies;
	private ArrayList<CollisionToCheck> collisions;
	private CyclicBarrier readyToCheckCollisions;
	private CyclicBarrier readyToSolveCollisions;
	private CyclicBarrier readyToDisplay;
	private Flag stopFlag;
	
	public CollisionChecker(int startIndex, int nCollisions, 
				ArrayList<Body> bodies, ArrayList<CollisionToCheck> collisions,
				CyclicBarrier readyToCheckCollisions,
				CyclicBarrier readyToSolveCollisions, CyclicBarrier readyToDisplay, Flag stopFlag) {
		this.startIndex = startIndex;
		this.nCollisions = nCollisions;
		this.bodies = bodies;
		this.collisions = collisions;
		this.readyToCheckCollisions = readyToCheckCollisions;
		this.readyToSolveCollisions = readyToSolveCollisions;
		this.readyToDisplay = readyToDisplay;
		this.stopFlag = stopFlag;
	}
	
	public void run() {
        try {        	
        	ArrayList<CollisionToCheck> collisionsToBeSolved = new ArrayList<CollisionToCheck>();

        	while (!stopFlag.isSet()){	  

        		log("waiting to check collisions");
        		
        		readyToCheckCollisions.await();	        	

        		long found = 0;
        		for (int i = 0; i < nCollisions; i++) {
	        		CollisionToCheck col = collisions.get(startIndex + i);
			    	Body b1 = bodies.get(col.getFirst());
			        Body b2 = bodies.get(col.getSecond());
			        if (b1.collideWith(b2)) {
			        	collisionsToBeSolved.add(col);
			        	found++;
			        }
	        	}
        		log("- checked " + nCollisions + " | found: " + found );
        		log("waiting to solve collisions");
	        	
        		readyToSolveCollisions.await();	        	

	        	for (CollisionToCheck col: collisionsToBeSolved) {
			    	Body b1 = bodies.get(col.getFirst());
			        Body b2 = bodies.get(col.getSecond());			        
			        /* 
			         * Since bodies can be shared among different collision checkers
			         * we need to take locks to avoid possible races, in an order that
			         * avoid deadlocks.
			         */
			        synchronized (b1) {
			        	synchronized (b2) {
					        /* if the bodies still collide */
			        		if (b1.collideWith(b2)) {
					        	Body.solveCollision(b1, b2);
					        }
			        	}
			        }
	        	}
	        
        		log(" - collision solved: " + collisionsToBeSolved.size());
        		collisionsToBeSolved.clear();
	        		
        		readyToDisplay.await();	        	

	        
	        }
        } catch (Exception ex) {
        	// ex.printStackTrace();
        }
		
	}

	private void log(String msg) {
		// System.out.println("[COLLISION CHECKER " + Thread.currentThread().getName() +"] " + msg);
	}

}
