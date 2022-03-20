package pcd.lab04.monitors.barrier;

/*
 * Barrier - to be implemented
 */
public class FakeBarrier implements Barrier {

	private int nParticipants;
	
	public FakeBarrier(int nParticipants) {
		this.nParticipants = nParticipants;
	}
	
	@Override
	public void hitAndWaitAll() throws InterruptedException {	
	}

	
}
