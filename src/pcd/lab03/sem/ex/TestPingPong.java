package pcd.lab03.sem.ex;

/**
 * Unsynchronized version
 * 
 * @TODO make it sync 
 * @author aricci
 *
 */
public class TestPingPong {
	public static void main(String[] args) {
		new Pinger().start();
		new Ponger().start();	
	}

}
