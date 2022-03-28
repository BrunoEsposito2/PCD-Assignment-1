package pcd.ass01.conc;

import java.util.Optional;

import pcd.ass01.utils.SimulationView;

/**
 * Bodies simulation - legacy code: concurrent
 * 
 */
public class ConcurrentBodySimulation {
	public static void main(String[] args) throws InterruptedException {
        
		SimulationView viewer = new SimulationView(620,620);
		
    	Simulator sim = new Simulator(Optional.of(viewer));
        sim.execute(50000);
    }
}
