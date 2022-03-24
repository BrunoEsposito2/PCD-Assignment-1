package pcd.ass01.conc;

public class BarrierMonitor implements Barrier {
	
	private int nConsumers;
	private int nHits;

	public BarrierMonitor(int nConsumers) {
		this.nConsumers = nConsumers;
		this.nHits = 0;
	}
	
	@Override
	public synchronized void hitAndWaitAll() throws InterruptedException {
		this.nHits++;
		while (!areAllOnBarrier()) {
			wait();
		}
		notifyAll();
	}
	
	private boolean areAllOnBarrier() {
		return this.nHits == this.nConsumers;
	}

}
