package pcd.lab04.monitors.resman;

public class FakeResManager implements ResManager {

	public FakeResManager(int nResourcesAvailable) {
	}
	
	@Override
	public int get() throws InterruptedException {
		throw new NotImplementedException();
	}

	@Override
	public void release(int id) {
		throw new NotImplementedException();
	}

}
