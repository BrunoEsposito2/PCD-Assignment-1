package pcd.lab04.monitors.latch;

import java.util.ArrayList;
import java.util.List;

public class TestLatch {

	public static void main(String[] args) {
		
		int nThreadA = 10;
		int nThreadB = 10;
		
		Latch latch = new FakeLatch(nThreadB);
		
		List<ThreadA> threadsAlist = new ArrayList<ThreadA>();
		for (int i = 0; i < nThreadA; i++) {
			threadsAlist.add(new ThreadA("ThreadA-" + i, latch));
		}

		List<ThreadB> threadsBlist = new ArrayList<ThreadB>();
		for (int i = 0; i < nThreadB; i++) {
			threadsBlist.add(new ThreadB("ThreadB-" + i, latch));
		}

		for (ThreadA t: threadsAlist) {
			t.start();
		}
		
		for (ThreadB t: threadsBlist) {
			t.start();
		}
		
	}
}
