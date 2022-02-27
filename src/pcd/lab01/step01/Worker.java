package pcd.lab01.step01;

/**
 * Base class for very simple agent structure
 * 
 * @author aricci
 *
 */
public abstract class Worker extends Thread {
	
	public Worker(String name){
		super(name);
	}

	protected void sleepFor(long ms){
		try {
			sleep(ms);
		} catch (InterruptedException ex){
			ex.printStackTrace();
		}
	}

	protected void print(String msg){
		synchronized (System.out){
			System.out.print(msg);
		}
	}

	protected void println(String msg){
		synchronized (System.out){
			System.out.println(msg);
		}
	}
}
