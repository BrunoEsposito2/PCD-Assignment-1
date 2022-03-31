package pcd.ass01.seq;

import java.util.ArrayList;

import pcd.ass01.conc.view.Controller;
import pcd.ass01.conc.view.VisualizerFrame;

/**
 * Simulation view
 *
 * @author aricci
 *
 */
public class SimulationView {
        
	private VisualizerFrame frame;
	
    /**
     * Creates a view of the specified size (in pixels)
     * 
     * @param w
     * @param h
     */
    public SimulationView(Controller controller, int w, int h){
    	frame = new VisualizerFrame(controller, w, h);
    }
    
    public synchronized void setSimulationState() {
    	frame.setSimulationState();
	}

	public synchronized void setIdleState() {
		frame.setIdleState();
	}
	
	public synchronized void updateSimulationScene(ArrayList<Body> bodies, double vt, long iter, Boundary bounds) {
		frame.updateSimulationScene(bodies, vt, iter, bounds);
	}
        
    public synchronized void display(){
 	   frame.display(); 
    }
}
