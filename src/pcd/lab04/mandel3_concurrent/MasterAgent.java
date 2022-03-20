package pcd.lab04.mandel3_concurrent;

/**
 * Master controller agent 
 * 
 * @author aricci
 *
 */
public class MasterAgent extends Thread {

	private MandelbrotView view;
	private MandelbrotSet set;
	private Complex c0;
	private double diam;	
	private TaskCompletionLatch synch;
	private int nWorkers;
	
	public MasterAgent(MandelbrotSet set, MandelbrotView view, TaskCompletionLatch synch, int nWorkers, Complex c0, double diam){
		this.set = set;
		this.view = view;
		this.c0 = c0;
		this.diam = diam;
		this.synch = synch;
		this.nWorkers = nWorkers;
	}
	
	public void run(){
			view.changeState("Processing...");
   			long t0 = System.currentTimeMillis();

			int w = set.getSizeX();
	       	int x0 = 0;
	       	int dx = w / nWorkers;       
	    	
	       	for (int i = 0; i < nWorkers-1; i++){	
	       		WorkerAgent worker = new WorkerAgent(c0, diam, x0, x0 + dx, set, synch);
	       		worker.start();	
	       		x0 += dx;
	       	}
	
	       	WorkerAgent worker = new WorkerAgent(c0, diam, x0, w, set, synch);
       		worker.start();	
       		
       		log("wait completion");
       		try {
       			synch.waitCompletion();
	       		log("completion arrived");
	    		view.update(set);
	    		
	    		long t1 = System.currentTimeMillis();
	    		view.changeState("Completed - time elapsed: "+(t1-t0));
	    		
      		} catch (InterruptedException ex) {
      			log("interrupted");
	    		view.changeState("Interrupted");
      			
      		}
			
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println("[ master ] " + msg);
		}
	}
	
}
