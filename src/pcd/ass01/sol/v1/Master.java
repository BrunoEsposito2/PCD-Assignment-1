package pcd.ass01.sol.v1;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import pcd.ass01.sol.common.Flag;
import pcd.ass01.sol.common.Statistics;
import pcd.ass01.sol.common.View;

public class Master extends Thread {

	private Simulation sim;
	private int nWorkers;
	private View view;
	private Flag stopFlag;
	
	ArrayList<CollisionChecker> collisionCheckers;	
	ArrayList<CollisionToCheck> collisionsToCheck;

	public Master(Simulation sim, int nWorkers, View view, Flag stopFlag) {
		this.sim = sim;
		this.view = view;
		this.nWorkers = nWorkers;
		this.stopFlag = stopFlag;
    }
	
	public void run() {
		sim.init();

		int nBodies = sim.getNumBodies();
        int nCollisionCheckers = nWorkers; 

    	/* generate all the collision couples to be checked */
    	
    	int nCollisions = (nBodies*(nBodies - 1)/2) / nCollisionCheckers;
        collisionsToCheck = new ArrayList<CollisionToCheck>();
        for (int i = 0; i < nBodies - 1; i++) {
        	for (int j = i + 1; j < nBodies; j++) {
        		collisionsToCheck.add(new CollisionToCheck(i,j));
        	}
        }

        /* create and spawn collision workers */
        
    	CyclicBarrier readyToCheckCollisions = new CyclicBarrier(nCollisionCheckers + 1);
    	CyclicBarrier readyToSolveCollisions = new CyclicBarrier(nCollisionCheckers + 1);
    	CyclicBarrier readyToDisplay = new CyclicBarrier(nCollisionCheckers + 1);

    	collisionCheckers = new ArrayList<CollisionChecker>();	
        int startIndex = 0;
        for (int i = 0; i < nCollisionCheckers; i++) {
        	CollisionChecker w = new CollisionChecker(startIndex, nCollisions, 
        			sim.getBodies(), collisionsToCheck, 
        			readyToCheckCollisions, readyToSolveCollisions, readyToDisplay, stopFlag);
        	collisionCheckers.add(w);
        	w.start();
        	startIndex += nCollisions;
        }
 
		Statistics stat = Statistics.getInstance();
		stat.notifyStartedNewSimulation(sim.getBodies().size(), sim.getNumIterations(), nWorkers);
		
		try {        	
	        while (!sim.isCompleted() && !stopFlag.isSet()){
	            
				stat.notifyStartNextStage();

				sim.updateBodies();
	        	
				stat.notifyUpdatedBodyPosCompleted();

				readyToCheckCollisions.await();
	        
	        	readyToSolveCollisions.await();

				readyToDisplay.await();	        	

				stat.notifyCheckCollisionsCompleted();
	        	
			    /* display current stage */
			    if (view != null) {
			    	view.update();
					stat.notifyDisplayCompleted();
			    }        
	        	sim.nextFrame();
	        }
        } catch (Exception ex) {
        	// ex.printStackTrace();
        }

		stat.notifyEndSimulation();
		stat.dump();
		
		/* to stop collision checkers */
		stopFlag.set();
		for (CollisionChecker c: collisionCheckers) {
			c.interrupt();
		}
	}
}
