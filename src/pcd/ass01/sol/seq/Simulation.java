package pcd.ass01.sol.seq;

import java.util.*;

public class Simulation {
        
	/* bodies in the field */ 
	ArrayList<Body> bodies;
	
	/* boundary of the field */
	private Boundary bounds;
	private int nIterations;
	private SimulationView viewer;
	
    public Simulation(int nBodies, int nIterations, SimulationView viewer){
    	/* initializing boundary and bodies */
    	this.viewer = viewer;
    	
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        this.nIterations = nIterations;
        
        Random rand = new Random(System.currentTimeMillis());
        bodies = new ArrayList<Body>();
        for (int i = 0; i < nBodies; i++) {
            double x = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double y = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double dx = -1 + rand.nextDouble()*2;
            double speed = rand.nextDouble()*0.05;
            Body b = new Body(new Position(x, y), new Velocity(dx*speed,Math.sqrt(1 - dx*dx)*speed), 0.01);
            bodies.add(b);
        }

    }

    public void execute() {
    	
        /* init virtual time */
        
        double vt = 0;         
        double dt = 0.2;
        
        long iter = 0; 
        
		System.out.println("Started - " + bodies.size() + " bodies - " + nIterations + " iterations.");
        
        /* simulation loop */

		long dt21 = 0;
		long dt32 = 0;
		long dt43 = 0;
		long t0 = System.currentTimeMillis();
        while (iter < nIterations){
	        
        	/* compute bodies new pos */

        	long t1 = System.currentTimeMillis();
        	
        	for (Body b: bodies) {
        		b.updatePos(dt);
		    }
		        
        	long t2 = System.currentTimeMillis();
        	
        	dt21 += t2 - t1;
        	
        	/* check collisions */
        	
		    for (int i = 0; i < bodies.size() - 1; i++) {
		    	Body b1 = bodies.get(i);
		        for (int j = i + 1; j < bodies.size(); j++) {
		        	Body b2 = bodies.get(j);
		            if (b1.collideWith(b2)) {
		            	Body.solveCollision(b1, b2);
		            }
		        }
	        }
		    
		    /* check boundaries */
		    
		    for (Body b: bodies) {
		    	b.checkAndSolveBoundaryCollision(bounds);
		    }
		
		    long t3 = System.currentTimeMillis();
        	
		    dt32 += t3 - t2;

		    /* update virtual time */
		    
		    vt = vt + dt;
		    iter++;

		    /* display current stage */
		    if (viewer != null) {
		    	viewer.display(bodies, vt, iter);
		    }        
        }
		long t1 = System.currentTimeMillis();

		System.out.println("Time elapsed: " + (t1 - t0) + "ms");
		System.out.println("dt21 - " + dt21 + "ms - average: " + ((double)dt21)/nIterations + "ms per iteration");
		System.out.println("dt32 - " + dt32 + "ms - average: " + ((double)dt32)/nIterations + "ms per iteration");
    }

}
