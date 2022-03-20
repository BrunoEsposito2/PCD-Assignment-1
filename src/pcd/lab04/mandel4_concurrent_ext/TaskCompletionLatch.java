package pcd.lab04.mandel4_concurrent_ext;

public class TaskCompletionLatch {

	private int nWorkers;
	private int nCompletionsNotified;
	
	TaskCompletionLatch(int nWorkers){
		this.nWorkers = nWorkers;
		nCompletionsNotified = 0;
	}
	
	public synchronized void reset() {
		nCompletionsNotified = 0;	
	}
	
	public synchronized void waitCompletion() throws InterruptedException {
		while (nCompletionsNotified < nWorkers) {
			wait();
		}
	}

	public synchronized void notifyCompletion() {
		nCompletionsNotified++;
		notifyAll();
	}
	
}
