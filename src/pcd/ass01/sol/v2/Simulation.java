package pcd.ass01.sol.v2;

import java.util.Random;
import pcd.ass01.sol.common.Body;
import pcd.ass01.sol.common.Boundary;
import pcd.ass01.sol.common.Position;
import pcd.ass01.sol.common.Velocity;

public class Simulation {
	
	private Area[] areas;
	private double time;
	private long nIter;
	private Boundary bounds; 
	private int nBodies;
	private int nMaxIterations;
	private double dt;
	
	public Simulation(int nBodies, int nIterations, int nAreas, double dt) {
		this.nBodies = nBodies;
		this.nMaxIterations = nIterations;
		this.dt = dt;
		
		areas = new Area[nAreas];

        bounds = new Boundary(-1.0,-1.0,1.0,1.0);

        double from = bounds.getX0();
		double delta = (bounds.getX1() - bounds.getX0()) / nAreas;
		
		for (int i = 0; i < nAreas; i++) {
			areas[i] = new Area(this, from, from + delta);
			from += delta;
		}

		if (nAreas > 1) {
			areas[0].setNeighbours(null, areas[1]);
			areas[nAreas - 1].setNeighbours(areas[nAreas - 2], null);
			for (int i = 1; i < nAreas - 1; i++) {
				areas[i].setNeighbours(areas[i - 1], areas[i + 1]);
			}
		} else {
			areas[0].setNeighbours(null, null);
		}
	}
	
	public void init() {
		double dxArea = (bounds.getX1() - bounds.getX0())/areas.length;
        Random rand = new Random(System.currentTimeMillis());
        
        for (Area a: areas) {
        	a.reset();
        }
        for (int i = 0; i < nBodies; i++) {
            double x = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double y = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double dx = -1 + rand.nextDouble()*2;
            double dy = rand.nextDouble() > 0.5 ? Math.sqrt(1 - dx*dx) : -Math.sqrt(1 - dx*dx);
            double speed = rand.nextDouble()*0.01;
            Body b = new Body(new Position(x, y), new Velocity(dx*speed,dy*speed), 0.01);
            
            int areaIndex = (int)((x - bounds.getX0())/dxArea);
            areas[areaIndex].addBody(b);
        }	
        time = 0;
		nIter = 0;
		
	}
		
	public void nextFrame() {
		time += dt;
		nIter++;
	}
	
	public boolean isCompleted() {
		return nIter >= nMaxIterations;
	}
	
	public void checkRemapping() {
		System.out.println("Remappings");
		int totBodiesAdded = 0;
		int totBodiesRemoved = 0;
		for (int i = 0; i < areas.length; i++) {
			totBodiesRemoved +=  areas[i].getNumBodiesRemoved();
			totBodiesAdded += areas[i].getNumBodiesAdded();
			System.out.println("" + i + " - removed: " + areas[i].getNumBodiesRemoved() + " - added: " + areas[i].getNumBodiesAdded());
		}
		if (totBodiesAdded != totBodiesRemoved) {
			System.out.println("ERROR.");
		}
	}
	
	public double getTime() {
		return time;
	}
	
	public long getNIter() {
		return nIter;
	}

	public long getNumMaxIterations() {
		return nMaxIterations;
	}

	public Area getArea(int i) {
		return areas[i];
	}
	
	public Area[] getAreas() {
		return areas;
	}
	
	public int getNBodies() {
		return nBodies;
	}

	public double getDT() {
		return dt;
	}
	
	public Boundary getBounds() {
		return bounds;
	}	
}
