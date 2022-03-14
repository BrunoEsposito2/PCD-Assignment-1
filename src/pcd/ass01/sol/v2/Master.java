package pcd.ass01.sol.v2;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

import pcd.ass01.sol.common.Flag;
import pcd.ass01.sol.common.Statistics;
import pcd.ass01.sol.common.View;

public class Master extends Thread {

	private Simulation sim;
	private Flag stopFlag;
	private View view;
	private int nWorkers;

	public Master(Simulation state, int nWorkers, Flag stopFlag, View view) {
		this.sim = state;
		this.stopFlag = stopFlag;
		this.view = view;
		this.nWorkers = nWorkers;
	}

	public void run() {
		sim.init();

		CyclicBarrier readyToUpdateBodies = new CyclicBarrier(nWorkers + 1);
		CyclicBarrier readyToCheckCollisions = new CyclicBarrier(nWorkers + 1);
		CyclicBarrier readyToRemapBodies = new CyclicBarrier(nWorkers + 1);
		CyclicBarrier readyToDisplay = new CyclicBarrier(nWorkers + 1);

		ArrayList<AreaWorker> workers = new ArrayList<AreaWorker>();
		
		int nAreasPerWorker = sim.getAreas().length / nWorkers;
		int startAreaIndex = 0;

		for (int i = 0; i < nWorkers - 1; i++) {
			AreaWorker w = new AreaWorker(sim, startAreaIndex, nAreasPerWorker, readyToUpdateBodies, readyToCheckCollisions, readyToRemapBodies, readyToDisplay, stopFlag);
			workers.add(w);
			w.start();
			startAreaIndex += nAreasPerWorker;
		}
		
		AreaWorker w = new AreaWorker(sim, startAreaIndex, sim.getAreas().length - startAreaIndex, readyToUpdateBodies, readyToCheckCollisions, readyToRemapBodies, readyToDisplay, stopFlag);
		workers.add(w);
		w.start();

		long nIterations = sim.getNumMaxIterations();

		Statistics stat = Statistics.getInstance();
		stat.notifyStartedNewSimulation(sim.getNBodies(), nIterations, workers.size());

		try {
			while (!sim.isCompleted()){

				if (stopFlag.isSet()) {
					break;
				}

				readyToUpdateBodies.await();
				stat.notifyStartNextStage();
				
				readyToCheckCollisions.await();
				
				stat.notifyUpdatedBodyPosCompleted();
				
				readyToRemapBodies.await();
				
				readyToDisplay.await();
				stat.notifyCheckCollisionsCompleted();

				/* display current stage */
				if (view != null) {
					view.update();
					stat.notifyDisplayCompleted();
				}        
				sim.nextFrame();
			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}

		stat.notifyEndSimulation();
		stat.dump();
		
		/* to stop area workers */
		
		stopFlag.set();
		for (AreaWorker wo: workers) {
			wo.interrupt();
		}
	}
}
