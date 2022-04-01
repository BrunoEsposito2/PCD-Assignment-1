package pcd.ass01.view;

import java.util.ArrayList;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public class StartSynch {
	
	private boolean started;
	private Task fullJob;
	
	public StartSynch(){
		started = false;
	}
	
	public synchronized Task waitStart() {
		while (!started) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		started = false;
		return fullJob;
	}

	public synchronized void notifyStarted(ArrayList<Body> bodies) {
		started = true;
		fullJob = new Task(bodies);
		notifyAll();
	}
	
}
