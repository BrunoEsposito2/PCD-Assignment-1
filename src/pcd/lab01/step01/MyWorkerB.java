package pcd.lab01.step01;

public class MyWorkerB extends Worker {
	
	public MyWorkerB(String name){
		super(name);
	}

	public void run(){
		action1();	
		action2();
	}
	
	protected void action1(){
		sleepFor(100);
		println("b1");
	}
	
	protected void action2(){
		println("b2");
	}
}
