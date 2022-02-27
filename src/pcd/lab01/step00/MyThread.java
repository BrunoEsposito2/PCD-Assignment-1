package pcd.lab01.step00;

public class MyThread extends Thread {

	public MyThread(String myName){
		super(myName);
	}
	
	public void run(){
		System.out.println("Hello concurrent world! by " + getName());
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		System.out.println("done.");
	}
}
