package pcd.lab02.check_act;

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
		WorkerA w1a = new WorkerA(c,ntimes);
		WorkerA w1b = new WorkerA(c,ntimes);
		WorkerB w2a = new WorkerB(c,ntimes);
		WorkerB w2b = new WorkerB(c,ntimes);
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
