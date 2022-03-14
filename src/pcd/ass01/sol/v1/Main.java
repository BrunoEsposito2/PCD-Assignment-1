package pcd.ass01.sol.v1;

/**
 * PCD Assignment 01 - First Approach 
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

    	/* if cmd line params */
    	
    	if (args.length > 0) {
    		nBodies = Integer.parseInt(args[0]);
    		nSteps = Integer.parseInt(args[1]);
    		nWorkers = Integer.parseInt(args[2]);
    	}
    	
    	Simulation sim = new Simulation(nBodies, nSteps, 0.1);
    	Controller controller = new Controller(sim, nWorkers);
    	SimulationView view = new SimulationView(620,620, controller, sim);
    	view.showUp();

    }
}
