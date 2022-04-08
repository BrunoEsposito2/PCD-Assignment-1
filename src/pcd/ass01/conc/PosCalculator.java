package pcd.ass01.conc;

import pcd.ass01.conc.patterns.AbstractSCWithMaster;
import pcd.ass01.conc.patterns.SynchronizedPipelineMonitor;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public class PosCalculator extends AbstractSCWithMaster<Body, SynchronizedPipelineMonitor<Body>> {

	/* virtual time step */
    private final double dt;

    /* boundary of the field */
    private final Boundary bounds;

	public PosCalculator(SynchronizedPipelineMonitor<Body> monitor, double dt, Boundary bounds) {
		super(monitor);
		this.dt = dt;
		this.bounds = bounds;
	}

	@Override
	public void consume(Body item){
		
	    /* compute bodies new pos */
	    item.updatePos(dt);
       
        /* check collisions with boundaries */
        item.checkAndSolveBoundaryCollision(bounds);
	}

}
