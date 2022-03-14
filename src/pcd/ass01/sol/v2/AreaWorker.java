package pcd.ass01.sol.v2;

import java.util.concurrent.CyclicBarrier;
import pcd.ass01.sol.common.Flag;

public class AreaWorker extends Thread {
	
	private Simulation sim;
	private CyclicBarrier readyToUpdateBodies, readyToCheckCollisions, readyToDisplay, readyToRemapBodies;
    private int startIndex, nAssignedAreas;
    
    private Flag stopFlag;
    
	public AreaWorker(Simulation sim, int startIndex, int nAreasAssigned,
			CyclicBarrier readyToUpdateBodies, CyclicBarrier readyToCheckCollisions, 
			CyclicBarrier readyToRemapBodies, CyclicBarrier readyToDisplay, Flag stopFlag) {
		
		super("area-" + startIndex);
		this.startIndex = startIndex;
		this.nAssignedAreas = nAreasAssigned;
		this.sim = sim;
		this.readyToUpdateBodies = readyToUpdateBodies;
		this.readyToCheckCollisions = readyToCheckCollisions;
		this.readyToDisplay = readyToDisplay;
		this.readyToRemapBodies = readyToRemapBodies;
		this.stopFlag = stopFlag;
	}
	
	public void run() {        
        try {        	
	        while (true){
	        	if (stopFlag.isSet()) {
	        		break;
	        	}
	        	
	        	log("waiting for updating body pos.");
	        	
	        	readyToUpdateBodies.await();
	
	    		log("updating bodies started");

	        	for (int i = 0; i < nAssignedAreas; i++) {
	        		sim.getArea(startIndex + i).updateBodies();
	        	}
	        
	        	readyToCheckCollisions.await();

	        	log("collision check started.");
	        	
	        	/* solve collisions for internal bodies */
	        	
	        	for (int i = 0; i < nAssignedAreas; i++) {
	        		sim.getArea(startIndex + i).checkCollisions();
	        	}	        	

	        	/* check for bodies changing area or on boundaries */
	        	
	        	for (int i = 0; i < nAssignedAreas; i++) {
	        		sim.getArea(startIndex + i).checkBodiesToMoveOut();
	        	}	        	

	        	readyToRemapBodies.await();
	        	
	        	for (int i = 0; i < nAssignedAreas; i++) {
	        		sim.getArea(startIndex + i).checkBodiesMovingIn();
	        	}	        	
	        	
	        	readyToDisplay.await();	        	        		        	
	        }
        } catch (Exception ex) {
        	// ex.printStackTrace();
        }		
	}
	
	private void log(String msg) {
		// System.out.println("[AREA UPDATER " + Thread.currentThread().getName() +"] " + msg);
	}
}
