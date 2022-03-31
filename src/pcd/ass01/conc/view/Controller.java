package pcd.ass01.conc.view;

import java.util.ArrayList;
import java.util.Optional;

import pcd.ass01.conc.Simulator;
import pcd.ass01.conc.patterns.MonitorImpl;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.SimulationView;

public class Controller {
	
	private Flag stopFlag;
	private SimulationView view;
	private MonitorImpl<Body> monitor;
	private Simulator sim;
	
	public Controller() {
		this.stopFlag = new Flag();
	}
	
	public synchronized void setView(SimulationView view) {
		this.view = view;
		this.sim = new Simulator(Optional.of(this), Optional.of(view), Optional.of(stopFlag));
	}
	
	public synchronized void notifyStarted() {
		// faccio partire il main worker
		this.monitor = sim.getMonitor();
		this.stopFlag.set();
		sim.execute(500);
	}
	
	public synchronized void notifyReset() {
		this.stopFlag.reset();
		//resetto le strutture dati su cui si effettuano i calcoli
		this.monitor.resetData();
		//aggiorno la view
		this.view.updateSimulationScene(new ArrayList<Body>(), 0, 0, this.sim.getBounds());
	}
	
}
