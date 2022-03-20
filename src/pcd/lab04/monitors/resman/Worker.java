package pcd.lab04.monitors.resman;

import java.util.Random;

public class Worker extends Thread {

	private ResManager resManager;
	
	public Worker(String name, ResManager resManager) {
		super(name);
		this.resManager = resManager;
	}
	
	public void run() {
		Random gen = new Random(System.nanoTime());
		try {
			while (true) {
				log("acquiring a res...");
				int idRes = resManager.get();
				log("using res: "+idRes);
				waitFor(gen.nextInt(2000));
				log("releasing res: "+idRes);
				resManager.release(idRes);
			}
		} catch (InterruptedException ex) {
			log("Interrupted!");
		}
	}
	
	private void log(String msg) {
		synchronized(System.out) {
			System.out.println("[ "+getName()+" ] "+msg);
		}
	}
	
	private void waitFor(long ms) throws InterruptedException{
		Thread.sleep(ms);
	}
}
