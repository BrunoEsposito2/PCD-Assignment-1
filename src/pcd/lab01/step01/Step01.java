package pcd.lab01.step01;

/*
 * Thread organization as simple agents.
 * 
 * Non determinism.
 * 
 */
public class Step01 {
	public static void main(String[] args) {
		new MyWorkerA("worker-A").start();		
		new MyWorkerB("worker-B").start();
	}
}
