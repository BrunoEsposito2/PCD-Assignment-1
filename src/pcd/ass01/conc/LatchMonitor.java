package pcd.ass01.conc;

public class LatchMonitor {
	
	private int count;
	
	public LatchMonitor(int nConsumers) {
		this.count = nConsumers;
	}
	
	public synchronized void await() throws InterruptedException {	
		while (count > 0)
			wait();
	}

	public synchronized void countDown() {	
		count--;
		if (count == 0) 
			notifyAll();
	}
}
