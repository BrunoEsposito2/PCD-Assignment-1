package pcd.lab03.liveness.jpf;

import pcd.lab03.liveness.Resource;

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
