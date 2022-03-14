package pcd.lab02.check_act.sol;

public class Counter {

	private int cont;
	private int min, max;
	
	public Counter(int min, int max){
		this.cont = this.min = min;
		this.max = max;
	}
	
	public void inc() throws OverflowException {
		if (cont + 1 > max){
			throw new OverflowException();
		}
		cont++;
	}

	public void dec() throws UnderflowException {
		if (cont - 1 < min){
			throw new UnderflowException();
		}
		cont--;
	}
	
	public int getValue(){
		return cont;
	}
}
