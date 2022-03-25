package pcd.ass01.conc;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

class Consumer extends Thread {

    /* virtual time step */
    double dt;
        
    /* boundary of the field */
    private Boundary bounds;
        
	private IMonitor<Body> monitor;
	
	
	public Consumer(IMonitor<Body> monitor, double dt, Boundary bounds){
		this.monitor = monitor;
		this.dt = dt;
		this.bounds = bounds;
	}

	public void run(){
	    //loop forever in search to produced items in the monitor's buffer to "consume" (AKA to update pos and check collision)
		while (true){
			try {
				Body item = monitor.get();
				consume(item);
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
			
		}
	}
	
	private void consume(Body item){
	    /* compute bodies new pos */
	    item.updatePos(dt);
            

        /* check collisions with boundaries */
        item.checkAndSolveBoundaryCollision(bounds);
            
	}
	
}
