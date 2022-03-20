package pcd.lab04.mandel3_concurrent;

/**
 * Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private MandelbrotSet set;
	private MandelbrotView view;
	private TaskCompletionLatch synch;
	
	public Controller(MandelbrotSet set, MandelbrotView view){
		this.set = set;
		this.view = view;
	}
	
	public synchronized void started(Complex c0, double diam){
		int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
		synch = new TaskCompletionLatch(nWorkers);
		new MasterAgent(set, view, synch, nWorkers, c0, diam).start();
	}

	public synchronized void stopped() {
		synch.stop();
	}

}
