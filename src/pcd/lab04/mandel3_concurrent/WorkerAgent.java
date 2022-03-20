package pcd.lab04.mandel3_concurrent;

/**
 * Worker  agent 
 * 
 * @author aricci
 *
 */
public class WorkerAgent extends Thread {

	private static final int SUBSLICE_SIZE = 5;
	private MandelbrotSet set;
	private Complex c0;
	private double diam;

	private int x0, x1;
	private TaskCompletionLatch synch;
	
	public WorkerAgent(Complex c0, double diam, int x0, int x1, MandelbrotSet set, TaskCompletionLatch synch){
		this.set = set;
		this.c0 = c0;
		this.diam = diam;
		this.x0 = x0;
		this.x1 = x1;
		this.synch = synch;
	}
	
	public void run(){
		log("started");

		boolean stopped = false;
		int nSubSlices = (x1 - x0) / SUBSLICE_SIZE;
		int x = x0;
		for (int i = 0; i < nSubSlices; i++) {
			if (!synch.stopped()) {
				set.computeSlice(x, x + SUBSLICE_SIZE, c0, diam);			
				x += SUBSLICE_SIZE;
				stopped = true;
			} else {
				log("interrupted");
			}
		}
		if (x < x1 && !stopped) {
			if (!synch.stopped()) {
				set.computeSlice(x, x1, c0, diam);			
			} else {
				log("interrupted");
			}
		}
		synch.notifyCompletion();
		log("completed");		
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println("[ worker " + getName() + "] " + msg);
		}
	}
	
}
