package pcd.lab02.lost_updates.sol_with_lock;

import java.util.concurrent.locks.*;

public class TestCounter {

	public static void main(String[] args) throws Exception {
		int ntimes = Integer.parseInt(args[0]);
		UnsafeCounter c = new UnsafeCounter(0);
		Lock lock = new ReentrantLock();
		Worker w1 = new Worker(c,ntimes, lock);
		Worker w2 = new Worker(c,ntimes, lock);

		Cron cron = new Cron();
		cron.start();
		w1.start();
		w2.start();
		w1.join();
		w2.join();
		cron.stop();
		System.out.println("Counter final value: " + c.getValue() + " in " + cron.getTime() + "ms.");
	}
}
