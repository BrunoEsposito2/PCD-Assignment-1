package pcd.ass01.conc;

import java.util.Optional;

import pcd.ass01.utils.SimulationView;
import pcd.ass01.view.Controller;
import pcd.ass01.view.StartSynch;

/**
 * Bodies simulation - legacy code: concurrent
 * 
 */
public class ConcurrentBodySimulation {
	public static void main(String[] args) throws InterruptedException {
        
		SimulationView viewer = new SimulationView(620,620);
		
		StartSynch synch = new StartSynch();
		Controller controller = new Controller();
    	Simulator sim = new Simulator(Optional.of(viewer), Optional.of(synch), Optional.of(controller));
                
        viewer.addListener(controller);
        viewer.display();
        sim.execute(50000);
    }
}
