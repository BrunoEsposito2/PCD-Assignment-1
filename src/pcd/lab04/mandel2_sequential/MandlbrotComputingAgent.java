package pcd.lab04.mandel2_sequential;

import java.util.concurrent.*;

/**
 * Master controller agent 
 * 
 * @author aricci
 *
 */
public class MandlbrotComputingAgent extends Thread {

	private MandelbrotView view;
	private MandelbrotSet set;
	private Complex c0;
	private double diam;
	private Flag stopFlag;
	
	public MandlbrotComputingAgent(Complex c0, double diam, MandelbrotSet set, MandelbrotView view, Flag stopFlag){
		this.set = set;
		this.view = view;
		this.c0 = c0;
		this.diam = diam;
		this.stopFlag = stopFlag;
	}
	
	public void run(){
		try {
			view.changeState("Processing...");
			long t0 = System.currentTimeMillis();

			boolean stopped = false;
			int nSlices = 20;
			int w = set.getSizeX();
	       	int x0 = 0;
	       	int dx = w / nSlices;       
	    	
	       	for (int i = 0; i < nSlices-1; i++){
				set.computeSlice(x0, x0 + dx, c0, diam);				
				x0 += dx;
				if (stopFlag.isSet()){
					stopped = true;
					break;
				} 
	       	}
			
	       	if (!stopped){
				set.computeSlice(x0, w, c0, diam);
				view.setUpdated(set);
    				long t1 = System.currentTimeMillis();
    				view.changeState("completed - time elapsed: "+(t1-t0));
			} else {
	    			view.changeState("interrupted");
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void log(String msg){
		synchronized(System.out){
			System.out.println(msg);
		}
	}
	
}
