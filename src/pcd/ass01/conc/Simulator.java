package pcd.ass01.conc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import pcd.ass01.conc.patterns.SynchronizedPipelineMonitor;
import pcd.ass01.utils.AbstractSimulator;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;
import pcd.ass01.utils.P2d;
import pcd.ass01.utils.SimulationView;
import pcd.ass01.utils.V2d;
import pcd.ass01.view.Controller;

public class Simulator extends AbstractSimulator{
	private final static int UPDATE_FREQUENCY = 2;
	
	private Optional<Controller> controller;
	
	private Optional<SimulationView> viewer;

	private SynchronizedPipelineMonitor<Body> monitor;
	
	private final MultithreadingManager mtManager;

	
	//private ArrayList<Body> bodies;

	public Simulator(final SimulationView viewer, final Controller controller, ArrayList<Body> bodies,  Boundary bounds) {
		super(bodies, bounds);
		this.controller = Optional.of(controller);
		this.viewer = Optional.of(viewer);
		
		this.initialBodies = new ArrayList<>();
		super.copyAndReplace(super.bodies, this.initialBodies);
		
        this.mtManager = new MultithreadingManager(super.bodies, super.bounds, super.dt);
        this.monitor = mtManager.getMonitor();
	}
	
	public Simulator(final ArrayList<Body> bodies, Boundary bounds) {
		super(bodies, bounds);
		this.controller = Optional.empty();
		this.viewer = Optional.empty();
		 
		super.copyAndReplace(super.bodies, this.initialBodies);
		
        this.mtManager = new MultithreadingManager(super.bodies, super.bounds, super.dt);
        this.monitor = mtManager.getMonitor(); 
	}
	
	@Override
	public void execute(final long nSteps) {

		if(this.controller.isPresent()) {
			try {
				this.controller.get().m.waitStart();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/* simulation loop */
		while (super.iter < nSteps) {
			if(this.controller.isPresent()) {
			    if(this.controller.get().m.evaluateReset()) {
			    	super.reset();
			    	try {
						this.controller.get().m.waitStart();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			}

			try {
				monitor.startAndWaitWorkers(Collections.unmodifiableList(this.bodies));
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		    /* update virtual time */
			vt = vt + dt;
			super.iter++;
						
			/* display current stage */
			if(viewer.isPresent() && (iter % UPDATE_FREQUENCY == 0)) viewer.get().updateView(bodies, vt, iter, bounds);
		}
		/* change of GUI and button states when simulation ends without user interaction on the GUI */
		if(viewer.isPresent()) {
			viewer.get().updateState("Terminated");
		}
		super.reset();
		this.execute(nSteps);
	}
	
	public ArrayList<Body> getBodies() {
		return super.bodies;
	}
	/*
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

*/}
