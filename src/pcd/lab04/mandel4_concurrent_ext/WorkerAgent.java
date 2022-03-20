package pcd.lab04.mandel4_concurrent_ext;

/**
 * 
 * Worker agents 
 * 
 * @author aricci
 *
 */
public class WorkerAgent extends Thread {

	private static final int SUBSLICE_SIZE = 5;

	private MandelbrotSet set;	
	private TaskBag bag;
	private Flag stopFlag;
	private TaskCompletionLatch latch;
	
	
	public WorkerAgent(MandelbrotSet set, TaskBag bag, TaskCompletionLatch latch, Flag stopFlag){
		this.set = set;
		this.bag = bag;
		this.latch = latch;
		this.stopFlag = stopFlag;
	}
	
	public void run(){
		log("started");

		while (true) {
			
			/* wait for a task to do */
			
			Task t = bag.getATask();

			/* got the task, do it */
			
			log("task allocated - " + t.getX0() + " - " + t.getX1());
			
			boolean stopped = false;
			int nSubSlices = (t.getX1() - t.getX0()) / SUBSLICE_SIZE;
			int x = t.getX0();
			for (int i = 0; i < nSubSlices; i++) {
				if (!stopFlag.isSet()) {
					set.computeSlice(x, x + SUBSLICE_SIZE, t.getC0(), t.getDiam());			
					x += SUBSLICE_SIZE;
					stopped = true;
				} else {
					log("interrupted");
				}
			}
			if (x < t.getX1() && !stopped) {
				if (!stopFlag.isSet()) {
					set.computeSlice(x, t.getX1(), t.getC0(), t.getDiam());			
				} else {
					log("interrupted");
				}
			}
			
			/* notify the task completion through the latch */
			
			latch.notifyCompletion();
			log("completed");		
		}
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println("[ worker " + getName() + "] " + msg);
		}
	}
	
}
