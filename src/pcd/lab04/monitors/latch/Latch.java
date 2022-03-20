package pcd.lab04.monitors.latch;

public interface Latch {

	void countDown();

	void await() throws InterruptedException;
}
