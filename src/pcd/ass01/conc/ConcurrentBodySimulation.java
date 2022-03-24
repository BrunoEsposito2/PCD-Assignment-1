package pcd.ass01.conc;

import pcd.ass01.seq.SimulationView;

/**
 * Bodies simulation - legacy code: concurrent
 * 
 */
public class ConcurrentBodySimulation {
	public static void main(String[] args) throws InterruptedException {
        
		SimulationView viewer = new SimulationView(620,620);
		
    	Simulator sim = new Simulator(viewer);
        sim.execute(50000);
        
    }
}
