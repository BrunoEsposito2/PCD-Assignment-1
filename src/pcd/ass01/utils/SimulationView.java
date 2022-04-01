package pcd.ass01.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import pcd.ass01.view.ActionListener;
import pcd.ass01.view.VisualizerFrame;

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
    public SimulationView(int w, int h){
    	frame = new VisualizerFrame(w, h);
    }
        
    public void update(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
    	frame.display(bodies, vt, iter, bounds); 
    }
    
    public void updateState(final String state) {
    	frame.updateState(state);
    }
    
    public void display() {
        try {
			SwingUtilities.invokeAndWait(() -> {
				frame.setVisible(true);
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void addListener(ActionListener l) {
    	frame.addListener(l);
    }

}