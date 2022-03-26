package pcd.ass01.conc;

import java.util.*;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;
import pcd.ass01.utils.P2d;
import pcd.ass01.utils.SimulationView;
import pcd.ass01.utils.V2d;

public class Simulator {

        
	private SimulationView viewer;

	/* bodies in the field */
	ArrayList<Body> bodies;
	
	private Monitor<Body> monitor;

	/* boundary of the field */
	private Boundary bounds;

	/* virtual time */
	private double vt;

	/* virtual time step */
	double dt;
	
	/* number of producers in producers-consumers pattern*/
	private final int nrProd;
	
	private final int nrCons;
	
	/* number of total process available*/
	private final int nrProcessors;
	
	/* size of the sublists given the number of producers */
	private final int deltaSplitList;
	
	/* number of elements to assign to the last producer (equals to nrProcessors % size total list)*/
	private final int restSplitList;
	
	/*Lists of producers and consumers*/
	private ArrayList<Consumer> consumers;
	private ArrayList<Producer> producers;

	public Simulator(SimulationView viewer) {
		this.viewer = viewer;
		
		/* init virtual time */
		this.dt = 0.001;
		this.vt = 0;
		
		
		/* initializing boundary and bodies */

		testBodySet1_two_bodies();
		// testBodySet2_three_bodies();
		// testBodySet3_some_bodies();
		// testBodySet4_many_bodies();
		
		this.nrProcessors = Runtime.getRuntime().availableProcessors()+1;
		this.nrProd =  nrProcessors >= bodies.size() ? 
					   bodies.size() : 
					   (int)((6.0/10.0)*(nrProcessors)); //TODO remove magic number
		
		this.nrCons = this.nrProcessors - this.nrProd;
		
		this.deltaSplitList = (int) Math.ceil((float) (bodies.size() / nrProd));
		this.restSplitList = bodies.size() % nrProd;
		this.monitor = new Monitor<>(bodies.size(), nrCons, bodies, nrProd+1);
		this.consumers = new ArrayList<>();
		this.producers = new ArrayList<>();
		
		//initialize consumers: they will remain alive the whole time
		this.initialize_producers();	
		this.initialize_consumers();
	}
	
	public void execute(long nSteps) {

		long iter = 0;
		
		
		/* simulation loop */
		while (iter < nSteps) {
		    
			try {
				monitor.startAndWaitWorkers(Collections.unmodifiableList(this.bodies));
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		    System.out.println("i completed an iteration");
		    /* update virtual time */
			vt = vt + dt;
			iter++;

			/* display current stage */
			viewer.display(bodies, vt, iter, bounds);

		}
	}
	
	private void initialize_consumers() {
		
		System.out.println(nrProcessors + " " + nrProd + " " + nrCons);
		for(int i = 0; i < this.nrCons; i++) {
		    Consumer c = new Consumer(monitor, dt, bounds);
			c.start();
			this.consumers.add(c);
		}
	}
	
	private void initialize_producers() {
		int fromIndex, toIndex;
        
		for(int i = 0; i<nrProd; i++) {
			fromIndex = i * deltaSplitList;
			toIndex = (i + 1) * deltaSplitList + (i == nrProd-1 ? restSplitList : 0);
			System.out.println("generated indexes: "+fromIndex +  " "+ toIndex);
			Producer p = new Producer(this.monitor,
									  this.dt,
									  fromIndex,
									  toIndex);
			p.start();
			this.producers.add(p);
		}
	}
	
	private void testBodySet1_two_bodies() {
		bounds = new Boundary(-4.0, -4.0, 4.0, 4.0);
		bodies = new ArrayList<Body>();
		bodies.add(new Body(0, new P2d(-0.1, 0), new V2d(0,0), 1));
		bodies.add(new Body(1, new P2d(0.1, 0), new V2d(0,0), 2));		
	}

	private void testBodySet2_three_bodies() {
		bounds = new Boundary(-1.0, -1.0, 1.0, 1.0);
		bodies = new ArrayList<Body>();
		bodies.add(new Body(0, new P2d(0, 0), new V2d(0,0), 10));
		bodies.add(new Body(1, new P2d(0.2, 0), new V2d(0,0), 1));		
		bodies.add(new Body(2, new P2d(-0.2, 0), new V2d(0,0), 1));		
	}

	private void testBodySet3_some_bodies() {
		bounds = new Boundary(-4.0, -4.0, 4.0, 4.0);
		int nBodies = 100;
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<Body>();
		for (int i = 0; i < nBodies; i++) {
			double x = bounds.getX0()*0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
			double y = bounds.getY0()*0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			bodies.add(b);
		}
	}

	private void testBodySet4_many_bodies() {
		bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		int nBodies = 1000;
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<Body>();
		for (int i = 0; i < nBodies; i++) {
			double x = bounds.getX0()*0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
			double y = bounds.getY0()*0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			bodies.add(b);
		}
	}
	
	

}
