package pcd.lab02.lost_updates.sol_with_synchblock;

public class Counter {

	private int cont;
	
	public Counter(int base){
		this.cont = base;
	}
	
	public void inc(){
		cont++;
	}
	
	public int getValue(){
		return cont;
	}
}
