package pcd.ass01.seq;

import java.util.ArrayList;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.BodyGenerator;
import pcd.ass01.utils.Boundary;
/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class SequentialBodySimulationMain{

    public static void main(String[] args) {
        Boundary bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        BodyGenerator bg = new BodyGenerator();
        ArrayList<Body> bodies = bg.generateBodies(0, bounds);

    	SequentialSimulator sim = new SequentialSimulator(bodies, bounds);

        sim.execute(1000);
    }
}
