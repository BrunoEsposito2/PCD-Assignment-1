package pcd.lab03.liveness;

/**
 * Deadlock example 
 * 
 * @author aricci
 *
 */
public class TestDeadlockedResource {
	public static void main(String[] args) {
		Resource res = new Resource();
		new ThreadA(res).start();
		new ThreadB(res).start();
	}

}
