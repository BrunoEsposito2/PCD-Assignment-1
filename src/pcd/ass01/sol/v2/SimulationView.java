package pcd.ass01.sol.v2;

import java.util.concurrent.Semaphore;
import javax.swing.*;

import pcd.ass01.sol.common.View;

/**
 * Simulation view
 * 
 * @author aricci
 *
 */
public class SimulationView  implements View {
    
    private SimulationFrame simuFrame;
    private Simulation sim;
    
    public SimulationView(int w, int h, Controller controller, Simulation sim){
        this.sim = sim;
        simuFrame = new SimulationFrame(w, h, controller, this);
    }
   
    public void showUp() {
    	SwingUtilities.invokeLater(() -> {
            simuFrame.setVisible(true);
    	});
    }
     
    public void update(){
    	Semaphore done = new Semaphore(0);
    	simuFrame.display(sim,done);
    	try {
    		done.acquire();
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
}
