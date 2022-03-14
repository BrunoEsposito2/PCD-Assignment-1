package pcd.ass01.sol.v2;

/**
 * PCD Assignment 01 - Second Approach 
 * 
 * @author aricci
 *
 */
public class Main {

    public static void main(String[] args) {
        
    	/* default values */   
    	
    	int nBodies = 1000;
    	int nSteps = 5000;
    	int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
    	int nAreas = nWorkers;
    	
    	Simulation sim = new Simulation(nBodies, nSteps, nAreas, 0.1);
    	Controller controller = new Controller(sim, nWorkers);    	
    	SimulationView view = new SimulationView(620,620, controller, sim);
    	view.showUp();
 
    	
    }
}
