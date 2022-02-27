package pcd.lab01.step05;

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
		// println("b1");
		// sleepFor(1);
	}
	
	protected void action2(){
		// println("b2");
		// sleepFor(1);
	}
}
