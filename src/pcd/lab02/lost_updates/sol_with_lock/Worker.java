package pcd.lab02.lost_updates.sol_with_lock;

import java.util.concurrent.locks.Lock;

public class Worker extends Thread{
	
	private UnsafeCounter counter;
	private int ntimes;
	private Lock lock;
	
	public Worker(UnsafeCounter c, int ntimes, Lock lock){
		counter = c;
		this.ntimes = ntimes;
		this.lock = lock;
	}
	
	public void run(){
		for (int i = 0; i < ntimes; i++){
			try {
				lock.lockInterruptibly();
				counter.inc();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
}
