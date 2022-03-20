package pcd.lab04.monitors.latch;

/*
 * Latch - to be implemented
 */
public class FakeLatch implements Latch {

	private int count;
	
	public FakeLatch(int count) {
		this.count = count;
	}
	
	@Override
	public void await() throws InterruptedException {		
	}

	@Override
	public void countDown() {		
	}

	
}
