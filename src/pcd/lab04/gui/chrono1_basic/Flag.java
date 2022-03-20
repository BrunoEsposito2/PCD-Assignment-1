package pcd.lab04.gui.chrono1_basic;

public class Flag {

	private boolean flag;
	
	public synchronized void reset() {
		flag = false;
	}
	
	public synchronized void set() {
		flag = true;
	}
	
	public synchronized boolean isSet() {
		return flag;
	}
}
