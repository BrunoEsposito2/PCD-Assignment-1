package pcd.lab01.step02;

public class MyWorkerB extends Worker {
	
	public MyWorkerB(String name){
		super(name);
	}

	public void run(){
		while (true){
		  action1();	
		  action2();
		}
	}
	
	protected void action1(){
		println("b1");
		sleepForRandomTime(0,10);	
	}
	
	protected void action2(){
		println("b2");
		sleepForRandomTime(100,200);	
	}
}
