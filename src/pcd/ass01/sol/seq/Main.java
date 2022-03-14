package pcd.ass01.sol.seq;

/**
 * Sequential simulation - measuring times.
 * 
 * @author aricci
 */
public class Main {

    public static void main(String[] args) {

    	int nBodies = 1000;
    	int nSteps = 5000;

    	/* if cmd line params */
    	
    	if (args.length > 0) {
    		nBodies = Integer.parseInt(args[0]);
    		nSteps = Integer.parseInt(args[1]);
    	}     	

    	SimulationView viewer = null;
    	// SeqSimulationViewer viewer = new SeqSimulationViewer(620,620);

        Simulation sim4 = new Simulation(nBodies, nSteps, viewer);
        sim4.execute();
    }
}
