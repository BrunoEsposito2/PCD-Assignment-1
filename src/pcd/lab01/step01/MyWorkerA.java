package pcd.lab01.step01;

public class MyWorkerA extends Worker {
	
	public MyWorkerA(String name){
		super(name);
	}
	
	public void run(){
		action1();	
		action2();	
		action3();	
	}
	
	protected void action1(){
		sleepFor(100);
		println("a1");
	}
	
	protected void action2(){
		println("a2");
	}

	protected void action3(){
		println("a3");
	}
	
}

