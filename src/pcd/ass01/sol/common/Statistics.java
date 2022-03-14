package pcd.ass01.sol.common;

/**
 * Class used to keep statistics about a simulation run.
 *
 * @author aricci
 *
 */
public class Statistics {

	private static Statistics instance;

	private long updatePosDuration;
	private long collisionCheckDuration;
	private long displayDuration;
	private long startTime;
	private long endTime;
	
	private long t0, t1, t2, t3;
	private long iter;

	public static Statistics getInstance() {
		synchronized (Statistics.class) {
			if (instance == null) {
				instance = new Statistics();
			}
		}
		return instance;
	}
	
	private Statistics() {}
	
	public void notifyStartedNewSimulation(int nBodies, long nIterations, int nWorkers) {
		System.out.println("Started | " + nBodies + " bodies | " + nIterations + " iterations | " + nWorkers + " workers.");
		updatePosDuration = 0;
		collisionCheckDuration = 0;
		displayDuration = 0;
		iter = 0;
		startTime = System.currentTimeMillis();
	}

	public void notifyEndSimulation() {
		endTime = System.currentTimeMillis();
	}
	
	public void notifyStartNextStage() {
		iter++;
		t0 = System.currentTimeMillis();
	}
	
	public void notifyUpdatedBodyPosCompleted() {
		t1 = System.currentTimeMillis();
		updatePosDuration += (t1 - t0);
	}
	
	public void notifyCheckCollisionsCompleted() {
		t2 = System.currentTimeMillis();
		collisionCheckDuration += (t2 - t1);
	}

	public void notifyDisplayCompleted() {
		t3 = System.currentTimeMillis();
		displayDuration += (t3 - t2);
	}

	public void dump() {
		System.out.println("Time elapsed: " + (endTime - startTime) + " ms - " + iter + " iterations completed.");
		System.out.println("Update position time: " + updatePosDuration + " ms - average: " + ((double)updatePosDuration)/iter + " ms per iteration");
		System.out.println("Collision manag time: " + collisionCheckDuration + " ms - average: " + ((double)collisionCheckDuration)/iter + " ms per iteration");
		System.out.println("Display         time: " + displayDuration + " ms - average: " + ((double)displayDuration)/iter + " ms per iteration");
		
	}
}
