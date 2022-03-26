package pcd.ass01.conc.test;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import pcd.ass01.conc.Consumer;
import pcd.ass01.conc.Monitor;
import pcd.ass01.conc.Producer;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;
import pcd.ass01.utils.P2d;
import pcd.ass01.utils.V2d;

public class CalculationsTest {
	/* bodies in the field */
	ArrayList<Body> bodies;
	
	/* concurrent bodies modified */
	ArrayList<Body> concBodies;
	
	private Monitor<Body> monitor;

	/* boundary of the field */
	private Boundary bounds;

	/* virtual time step */
	double dt;
	
	/* number of producers in producers-consumers pattern*/
	private int nrProd;
	
	private int nrCons;
	
	/* number of total process available*/
	private int nrProcessors;
	
	/* size of the sublists given the number of producers */
	private int deltaSplitList;
	
	/* number of elements to assign to the last producer (equals to nrProcessors % size total list)*/
	private int restSplitList;
	
	/*Lists of producers and consumers*/
	private ArrayList<Consumer> consumers;
	private ArrayList<Producer> producers;
	
	@BeforeEach
	public void setUp() {
		this.dt = 0.001;
		this.bounds = new Boundary(-4.0, -4.0, 4.0, 4.0);
		this.bodies = new ArrayList<Body>();
		this.bodies.add(new Body(0, new P2d(-0.1, 0), new V2d(0,0), 1));
		this.bodies.add(new Body(1, new P2d(0.1, 0), new V2d(0,0), 2));
		
		this.nrProcessors = Runtime.getRuntime().availableProcessors()+1;
		this.nrProd =  nrProcessors >= bodies.size() ? 
					   bodies.size() : 
					   (int)((6.0 / 10.0) * (nrProcessors)); //TODO remove magic number
		
		this.nrCons = this.nrProcessors - this.nrProd;
		
		this.deltaSplitList = (int) Math.ceil((float) (bodies.size() / nrProd));
		this.restSplitList = bodies.size() % nrProd;
		this.monitor = new Monitor<>(bodies.size(), nrCons, bodies, nrProd+1);
		this.consumers = new ArrayList<>();
		this.producers = new ArrayList<>();
		
		this.concBodies = this.bodies;
		
		//initialize consumers: they will remain alive the whole time
		this.initialize_producers();	
		this.initialize_consumers();
	}
	
	@Test
	public void testExecute() {
		final ArrayList<Body> newSequentialBodies = sequentialCalc(this.bodies); // calcoli sequenziali  
		this.concurrentCalc(); // calcoli concorrenti delle nuove posizioni e velocità dei corpi
		
		for (int i = 0; i < this.bodies.size(); i++) {
			assertEquals(this.concBodies.get(i).getPos().getX(), newSequentialBodies.get(i).getPos().getX(), 0.01); //uguaglianza dei valori delle Pos x
			assertEquals(this.concBodies.get(i).getPos().getY(), newSequentialBodies.get(i).getPos().getY(), 0.01); //uguaglianza dei valori delle Pos y
			
			assertEquals(this.concBodies.get(i).getVel().getX(), newSequentialBodies.get(i).getVel().getX(), 0.01); //uguaglianza dei valori delle Vel x
			assertEquals(this.concBodies.get(i).getVel().getY(), newSequentialBodies.get(i).getVel().getY(), 0.01); //uguaglianza dei valori delle Vel y
		}		
	}
	
	// calcoli concorrenti
	private void concurrentCalc(){
		try {
			monitor.startAndWaitWorkers(Collections.unmodifiableList(this.concBodies));
			
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	// calcoli sequenziali
	private ArrayList<Body> sequentialCalc(final ArrayList<Body> bodyList) {
		ArrayList<Body> newBodies =  bodyList;
		
		for (int i = 0; i < newBodies.size(); i++) {
			Body b = newBodies.get(i);

			/* compute total force on bodies */
			V2d totalForce = computeTotalForceOnBody(b, newBodies);

			/* compute instant acceleration */
			V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());

			/* update velocity */
			b.updateVelocity(acc, this.dt);
		}

		/* compute bodies new position */
		for (Body b : newBodies) {
			b.updatePos(this.dt);
		}

		/* check collisions with boundaries */
		for (Body b : newBodies) {
			b.checkAndSolveBoundaryCollision(this.bounds);
		}
		
		return newBodies;
	}
	
	private V2d computeTotalForceOnBody(final Body b, final ArrayList<Body> bodyList) {
		V2d totalForce = new V2d(0, 0);

		/* compute total repulsive force */
		for (int j = 0; j < bodyList.size(); j++) {
			Body otherBody = bodyList.get(j);
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
	
	// produttori inizializzati
	private void initialize_producers() {
		int fromIndex, toIndex;
        
		for(int i = 0; i < this.nrProd; i++) {
			fromIndex = i * deltaSplitList;
			toIndex = (i + 1) * deltaSplitList + (i == nrProd - 1 ? restSplitList : 0);
			
			/*Producer p = new Producer(this.monitor,
									  Collections.unmodifiableList(this.bodies),
									  this.dt,
									  fromIndex,
									  toIndex);
			p.start();
			this.producers.add(p);*/
		}
	}
	
	// consumatori inizializzati
	private void initialize_consumers() {
		System.out.println(nrProcessors + " " + nrProd + " " + nrCons);
		for(int i = 0; i < this.nrCons; i++) {
		    Consumer c = new Consumer(monitor, dt, bounds);
			c.start();
			this.consumers.add(c);
		}
	}
}
