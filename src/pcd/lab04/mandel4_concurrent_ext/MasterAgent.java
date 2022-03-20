package pcd.lab04.mandel4_concurrent_ext;

/**
 * 
 * Master agent 
 * 
 * @author aricci
 *
 */
public class MasterAgent extends Thread {

	private MandelbrotView view;
	private MandelbrotSet set;
	private Flag stopFlag;
	private StartSynch synch;
	
	public MasterAgent(MandelbrotSet set, MandelbrotView view, StartSynch synch, Flag stopFlag){
		this.set = set;
		this.view = view;
		this.synch = synch;
		this.stopFlag = stopFlag;
	}
	
	public void run(){

		/* creating workers */
		
		int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
		
		TaskBag bag = new TaskBag();
		TaskCompletionLatch taskLatch = new TaskCompletionLatch(nWorkers);
		
       	for (int i = 0; i < nWorkers - 1; i++){	
       		WorkerAgent worker = new WorkerAgent(set, bag, taskLatch, stopFlag);
       		worker.start();
       	}
       	
   		WorkerAgent worker = new WorkerAgent(set, bag, taskLatch, stopFlag);
   		worker.start();
		
   		/* main loop */
   		
		while (true) {
			try {
				Task job = synch.waitStart();
				
	   			long t0 = System.currentTimeMillis();	
				view.changeState("Processing...");	
				
				log("allocating tasks...");
				
				/* reset the latch */
				
	       		taskLatch.reset();

	       		/* allocating tasks through the bag */

		       	bag.clear();
				
				int nTasks = nWorkers;
				int w = set.getSizeX();
		       	int x0 = 0;
		       	int dx = w / nTasks;       
		    			       	
		       	for (int i = 0; i < nTasks - 1; i++){	
		       		Task t = new Task(job.getC0(), job.getDiam(), x0, x0 + dx);
		       		bag.addNewTask(t);
		       		x0 += dx;
		       	}
		
	       		Task t = new Task(job.getC0(), job.getDiam(), x0, w);
	       		bag.addNewTask(t);

				/* waiting tasks to be completed through the latch */
	       		
	       		log("wait completion");
	       		taskLatch.waitCompletion();
		       	
	       		/* update the view */

	       		if (!stopFlag.isSet()) {
	       			view.update(set);
		    		long t1 = System.currentTimeMillis();
		    		view.changeState("Completed - time elapsed: "+(t1-t0));
	      		} else {
	      			log("interrupted");
		    		view.changeState("Interrupted");	      			
	      		}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
			
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println("[ master ] " + msg);
		}
	}
	
}
