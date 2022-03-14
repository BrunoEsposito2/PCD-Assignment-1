package pcd.lab02.check_act.sol;

import java.util.concurrent.Semaphore;

/**
 * To enable assertions: run with -ea option
 * 
 * @author aricci
 *
 */
public class TestCounter {

	public static void main(String[] args) throws Exception {
		int ntimes = 10000;
		Counter c = new Counter(0,1);
		Semaphore mutex = new Semaphore(1,true);
		WorkerA w1a = new WorkerA(c,ntimes, mutex);
		WorkerA w1b = new WorkerA(c,ntimes, mutex);
		WorkerB w2a = new WorkerB(c,ntimes, mutex);
		WorkerB w2b = new WorkerB(c,ntimes, mutex);
		w1a.start();
		w1b.start();
		w2a.start();
		w2b.start();
		w1a.join();
		w1b.join();
		w2a.join();
		w2b.join();
		System.out.println("Counter final value: "+c.getValue());
	}
}
