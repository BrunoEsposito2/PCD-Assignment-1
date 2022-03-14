package pcd.lab03.liveness;

public class ThreadA extends BaseAgent {
 
	private Resource res;
	
	public ThreadA(Resource res){
		this.res = res;
	}
	
	public void run(){
		while (true){
			waitAbit();
			res.rightLeft();
		}
	}	
}
