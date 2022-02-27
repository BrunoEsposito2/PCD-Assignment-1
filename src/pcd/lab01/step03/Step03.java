package pcd.lab01.step03;

/**
 * Test join.
 * 
 * @author aricci
 *
 */
public class Step03 {

	public static void main(String[] args) throws Exception {
		MyWorkerA t = new MyWorkerA("worker-A");
		t.start();
		new MyWorkerB("worker-B", t).start();		
	}

}
