package pcd.ass01.utils;

import java.util.ArrayList;
import java.util.Random;

public class BodyGenerator {

	public ArrayList<Body> generateBodies(final int nBodies, final Boundary bounds) {
		ArrayList<Body> bodies = new ArrayList<>();
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<Body>();
		for (int i = 0; i < nBodies; i++) {
			double x = bounds.getX0() * 0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
			double y = bounds.getY0() * 0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
			Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
			bodies.add(b);
		}
		return bodies;
	}
}
