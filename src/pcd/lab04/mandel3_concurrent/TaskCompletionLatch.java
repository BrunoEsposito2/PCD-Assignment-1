package pcd.lab04.mandel3_concurrent;

public class TaskCompletionLatch {

	private int nWorkers;
	private boolean stopped;
	private int nCompletionsNotified;
	
	TaskCompletionLatch(int nWorkers){
		this.nWorkers = nWorkers;
		nCompletionsNotified = 0;
		stopped = false;
	}
	
	public synchronized void reset() {
		nCompletionsNotified = 0;	
	}
	
	public synchronized void waitCompletion() throws InterruptedException {
		while (nCompletionsNotified < nWorkers && !stopped) {
			wait();
		}
		if (stopped) {
			throw new InterruptedException();
		}
	}

	public synchronized void notifyCompletion() {
		nCompletionsNotified++;
		notifyAll();
	}
	
	public synchronized void stop() {
		stopped = true;
		notifyAll();
	}
	
	public synchronized boolean stopped() {
		return stopped;
	}
	
	
}
