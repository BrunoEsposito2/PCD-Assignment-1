package pcd.ass01.conc;

import java.util.*;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.V2d;

public class Producer extends Thread {

    //the total list of bodies of the simulation
    private final List<Body> bodies;
    
    //a sublist of bodies to produce
    private List<Body> toProduce;
    
    //the buffer of the monitor (initialized at bodies.size)
    private IMonitor<Body> monitor;
    
    private double dt;
    
    private final int from, to;
    
    public Producer(IMonitor<Body> monitor,  List<Body> bodies, double dt, int f, int t){
            this.monitor = monitor;
            this.bodies = bodies;
            this.dt = dt;
            this.from = f;
            this.to = t;
    }

    public void run(){
	    while(true) {
	    	try {
				this.monitor.waitMaster();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	this.toProduce = monitor.getWorkerSublist(this.from, this.to);
	    	
    		//for each body in toProduce put in the monitor's buffer the updated body
	        for(Body b:toProduce){
	        	b = produce(b);
	            try {
	            	monitor.put(b);
	            } catch(InterruptedException ex){
	            	ex.printStackTrace();
	            }
	        }
	    }
    }
    
    private Body produce(Body b){
        /* compute total force on bodies */
        V2d totalForce = computeTotalForceOnBody(b);

        /* compute instant acceleration */
        V2d acc = new V2d(totalForce).scalarMul(1.0 / b.getMass());

        /* update velocity */
        b.updateVelocity(acc, dt);
        return b;
    }
    
    private V2d computeTotalForceOnBody(Body b) {
        V2d totalForce = new V2d(0, 0);

        /* compute total repulsive force */
        for (int j = 0; j < this.bodies.size(); j++) {
        	Body otherBody = this.bodies.get(j);
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
    
    private void log(String st){
    	synchronized(System.out){
    		System.out.println("["+this.getName()+"] "+st);
    	}
    }
}
