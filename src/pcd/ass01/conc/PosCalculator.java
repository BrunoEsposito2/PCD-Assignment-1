package pcd.ass01.conc;

import pcd.ass01.conc.patterns.AbstractSCWithMaster;
import pcd.ass01.conc.patterns.MonitorImpl;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public class PosCalculator extends AbstractSCWithMaster<Body, MonitorImpl<Body>> {

	 /* virtual time step */
    private final double dt;

    /* boundary of the field */
    private final Boundary bounds;

	public PosCalculator(MonitorImpl<Body> monitor, double dt, Boundary bounds) {
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
