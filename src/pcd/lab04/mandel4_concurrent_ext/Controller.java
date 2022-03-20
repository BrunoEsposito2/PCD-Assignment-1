package pcd.lab04.mandel4_concurrent_ext;

/**
 * Controller part of the application - passive part.
 * 
 * @author aricci
 *
 */
public class Controller implements InputListener {

	private StartSynch synch;
	private Flag stopFlag;
	
	public Controller(StartSynch synch, Flag stopFlag){
		this.synch = synch;
		this.stopFlag = stopFlag;
	}
	
	public synchronized void started(Complex c0, double diam){
		stopFlag.reset();
		synch.notifyStarted(c0, diam);
	}

	public synchronized void stopped() {
		stopFlag.set();
	}

}
