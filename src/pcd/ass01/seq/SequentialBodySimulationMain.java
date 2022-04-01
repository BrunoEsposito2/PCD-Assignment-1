package pcd.ass01.seq;

import java.util.Optional;

import pcd.ass01.seq.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class SequentialBodySimulationMain {

    public static void main(String[] args) {
                
    	SimulationView viewer = new SimulationView(620,620);

    	SequentialSimulator sim = new SequentialSimulator(Optional.of(viewer));
        sim.execute(50000);
    }
}
