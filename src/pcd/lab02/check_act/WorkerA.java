package pcd.lab02.check_act;

public class WorkerA extends Thread{
	
	private Counter counter;
	private int ntimes;
	
	public WorkerA(Counter c, int ntimes){
		counter = c;
		this.ntimes = ntimes;
	}
	
	public void run(){
		try {
			for (int i = 0; i < ntimes; i++){
				if (counter.getValue() > 0){
					counter.dec();
				}
			}
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
