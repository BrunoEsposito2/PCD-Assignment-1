package pcd.ass01.sol.v2;

import pcd.ass01.sol.common.Flag;

/**
 * PCD Assignment 01 - First Approach - for time measurements
 * 
 * @author aricci
 *
 */
public class MainNoGUI {

    public static void main(String[] args) {

    	/* default values */
    	
    	int nBodies = 5000;
    	int nSteps = 5000;
    	int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
    			
    	if (args.length > 0) {
    		nBodies = Integer.parseInt(args[0]);
    		nSteps = Integer.parseInt(args[1]);
    		nWorkers = Integer.parseInt(args[2]);
    	}

    	int nAreas = nWorkers;
    	
    	Simulation sim = new Simulation(nBodies, nSteps, nAreas, 0.01);
		Master master = new Master(sim, nWorkers, new Flag(), null);
        master.start();

    }
}
