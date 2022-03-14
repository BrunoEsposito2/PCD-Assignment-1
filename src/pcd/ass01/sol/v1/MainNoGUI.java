package pcd.ass01.sol.v1;

import pcd.ass01.sol.common.Flag;

/**
 * PCD Assignment 01 - First Approach - for time measurements
 * 
 * @author aricci
 *
 */
public class MainNoGUI {

    public static void main(String[] args) {

    	int nBodies = 1000;
    	int nSteps = 5000;
    	int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
    	
    	if (args.length > 0) {
        	if (args.length == 3) {
        		nBodies = Integer.parseInt(args[0]);
        		nSteps = Integer.parseInt(args[1]);
        		nWorkers = Integer.parseInt(args[2]);
        	} else {
        		System.err.println("Params: <nbodies> <nsteps> <nworkers>");
        		System.exit(-1);
        	}
    	}
    	
    	Simulation sim = new Simulation(nBodies, nSteps, 0.01);
    	Master master = new Master(sim, nWorkers, null, new Flag());
        master.start();

    }
}
