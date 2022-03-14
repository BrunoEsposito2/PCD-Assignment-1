package pcd.lab02.lost_updates.sol_with_synchmeth;

public class Worker extends Thread {
	
	private SafeCounter counter;
	private int ntimes;
	
	public Worker(SafeCounter c, int ntimes){
		counter = c;
		this.ntimes = ntimes;
	}
	
	public void run(){
		for (int i = 0; i < ntimes; i++){
			counter.inc();
		}
	}
}
