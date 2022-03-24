package pcd.ass01.conc;

import pcd.ass01.seq.Body;
import pcd.ass01.seq.Boundary;

class Consumer extends Thread {

    /* virtual time step */
    double dt;
        
    /* boundary of the field */
    private Boundary bounds;
        
	private Monitor<Body> buffer;
	
	
	public Consumer(Monitor<Body> monitor, double dt, Boundary bounds){
		this.buffer = monitor;
		this.dt = dt;
		this.bounds = bounds;
	}

	public void run(){
	    //loop forever in search to produced items in the monitor's buffer to "consume" (AKA to update pos and check collision)
		while (true){
			try {
				Body item = buffer.get();
				consume(item);
				//bar.await();
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
