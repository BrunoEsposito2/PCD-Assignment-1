package pcd.lab04.gui.chrono2_strict;

/**
 * Active component doing counting. 
 * 
 * @author aricci
 *
 */
public class CounterAgent extends Thread {

	private Counter counter;
	private Flag stopFlag;
	private long delta;
	private CounterView view;
	
	public CounterAgent(Counter c, Flag stopFlag, CounterView view, long delta){
		counter = c;
		this.stopFlag = stopFlag;
		this.delta = delta;
		this.view = view;
	}
	public void run(){
		stopFlag.reset();
		view.setCountingState();
		while (!stopFlag.isSet()){
			counter.inc();
			view.updateCountValue(counter.getValue());
			System.out.println(counter.getValue());
			try {
				Thread.sleep(delta);
			} catch(Exception ex){
			}
		}
	}
}
