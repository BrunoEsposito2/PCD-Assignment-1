package pcd.lab03.sem.ex;

public class Ponger extends Thread {
	
	public Ponger() {
	}	
	
	public void run() {
		while (true) {
			try {
				System.out.println("pong!");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}