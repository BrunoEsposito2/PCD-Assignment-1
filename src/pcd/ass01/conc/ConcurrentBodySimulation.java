package pcd.ass01.conc;

import pcd.ass01.conc.view.Controller;
import pcd.ass01.utils.SimulationView;

/**
 * Bodies simulation - legacy code: concurrent
 * 
 */
public class ConcurrentBodySimulation {
	public static void main(String[] args) throws InterruptedException {
        
		Controller controller = new Controller();
		SimulationView viewer = new SimulationView(controller, 620,620);
		
		controller.setView(viewer);
		viewer.display();
    	//Simulator sim = new Simulator(Optional.of(controller), Optional.of(viewer));
        //sim.execute(50000);
    }
}
