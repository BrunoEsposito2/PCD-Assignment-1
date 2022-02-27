package pcd.lab01.step03;

public class MyWorkerB extends Worker {
	
	private MyWorkerA friend;
	
	public MyWorkerB(String name, MyWorkerA t){
		super(name);
		friend = t;
	}

	public void run(){
		println("b1");
		println("b2");
		try {
			friend.join();
		} catch (InterruptedException ex){
			ex.printStackTrace();
		}
		println("b3");
	}
}
