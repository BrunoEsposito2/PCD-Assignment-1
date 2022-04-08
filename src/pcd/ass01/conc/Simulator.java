package pcd.ass01.conc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import pcd.ass01.conc.patterns.SynchronizedPipelineMonitor;
import pcd.ass01.utils.AbstractSimulator;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;
import pcd.ass01.utils.SimulationView;
import pcd.ass01.view.Controller;

public class Simulator extends AbstractSimulator{
	
	private final static int UPDATE_FREQUENCY = 2;
	
	private Optional<Controller> controller;
	
	private Optional<SimulationView> viewer;

	private SynchronizedPipelineMonitor<Body> monitor;
	
	private final MultithreadingManager mtManager;

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
				ArrayList<Body> readOnlyList = new ArrayList<>();
				super.copyAndReplace(super.bodies, readOnlyList);
				monitor.startAndWaitWorkers(readOnlyList);
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
			super.reset();
			this.execute(nSteps);
		}
	}
	
	public ArrayList<Body> getBodies() {
		
		return super.bodies;
	}
}
