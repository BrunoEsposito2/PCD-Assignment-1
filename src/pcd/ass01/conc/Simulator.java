package pcd.ass01.conc;

import java.util.*;

import pcd.ass01.seq.Body;
import pcd.ass01.seq.Boundary;
import pcd.ass01.seq.P2d;
import pcd.ass01.seq.SimulationView;
import pcd.ass01.seq.V2d;

public class Simulator {


	private SimulationView viewer;

	/* bodies in the field */
	ArrayList<Body> bodies;

	private Monitor<Body> monitor;
	private BarrierMonitor bar;

	/* boundary of the field */
	private Boundary bounds;

	/* virtual time */
	private double vt;

	/* virtual time step */
	double dt;

	private Producer p1, p2;
	private Consumer c;

	public Simulator(SimulationView viewer) {
		this.viewer = viewer;

		/* initializing boundary and bodies */

		testBodySet1_two_bodies();
		// testBodySet2_three_bodies();
		// testBodySet3_some_bodies();
		// testBodySet4_many_bodies();
		monitor = new Monitor<>(bodies.size());
		bar = new BarrierMonitor(bodies.size());
	}

	public void execute(long nSteps) throws InterruptedException {

		/* init virtual time */

		vt = 0;
		dt = 0.001;

		long iter = 0;

		//initialize consumer out of the loop: it will remain alive the whole time
		c = new Consumer(monitor, dt, bounds);
		c.start();

		/* simulation loop */
		while (iter < nSteps) {


			//initialize Producers inside loop
			p1 = new Producer(monitor, bodies.subList(0, 1), Collections.unmodifiableList(bodies), dt, bar);
			p2 = new Producer(monitor, bodies.subList(1, 2), Collections.unmodifiableList(bodies), dt, bar);

			//run producers
			p1.start();
			p2.start();

			/* update virtual time */
			vt = vt + dt;
			iter++;

			/* display current stage */
			viewer.display(bodies, vt, iter, bounds);
		}
	}

	private V2d computeTotalForceOnBody(Body b) {

		V2d totalForce = new V2d(0, 0);

		/* compute total repulsive force */

		for (int j = 0; j < bodies.size(); j++) {
			Body otherBody = bodies.get(j);
			if (!b.equals(otherBody)) {
				try {
					V2d forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
					totalForce.sum(forceByOtherBody);
				} catch (Exception ex) {
				}
			}
		}

		/* add friction force */
		totalForce.sum(b.getCurrentFrictionForce());

		return totalForce;
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