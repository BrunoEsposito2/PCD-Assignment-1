package pcd.lab02.lost_updates.sol_with_synchmeth;

public class SafeCounter {

	private int cont;
	
	public SafeCounter(int base){
		this.cont = base;
	}
	
	public synchronized void inc(){
		cont++;
	}
	
	public synchronized int getValue(){
		return cont;
	}
}
