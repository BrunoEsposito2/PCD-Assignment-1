package pcd.ass01.conc.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pcd.ass01.conc.ConcurrentSimulator;
import pcd.ass01.seq.SequentialSimulator;
import pcd.ass01.utils.*;

public class CalculationsTest {
	
	private ConcurrentSimulator concurrentSimulator;
	private SequentialSimulator sequentialSimulator;
	private int nBodies;
    
	@BeforeEach
	public void setUp() {
		BodyGenerator bg = new BodyGenerator();
		Boundary bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		nBodies = 100;
		ArrayList<Body> bodySet = bg.generateBodies(nBodies, bounds);
		this.concurrentSimulator = new ConcurrentSimulator(bodySet, bounds);
		this.sequentialSimulator = new SequentialSimulator(bodySet, bounds);
	}
	
	// N steps = 50000
	@Test
	public void testExecuteFiftyThousand() {
		this.sequentialSimulator.execute(50000);
		this.concurrentSimulator.execute(50000);
		this.checkCalculations(this.concurrentSimulator, this.sequentialSimulator);
	}
	
	// check if concurrent and sequential calculations are equals
	private void checkCalculations(final ConcurrentSimulator concSimulator, final SequentialSimulator seqSimulator) {
		if(concSimulator.getBodies().size() == seqSimulator.getBodies().size()) {
			concSimulator.getBodies().stream().sorted((b1,b2) -> Integer.compare(b1.getId(), b2.getId())).collect(Collectors.toList());
			seqSimulator.getBodies().stream().sorted((b1,b2) -> Integer.compare(b1.getId(), b2.getId())).collect(Collectors.toList());

			for(int i = 0; i < nBodies; i++) {
				// check position calculation accuracy
				assertEquals(seqSimulator.getBodies().get(i).getPos().getX(), concSimulator.getBodies().get(i).getPos().getX(), 0);
				assertEquals(seqSimulator.getBodies().get(i).getPos().getY(), concSimulator.getBodies().get(i).getPos().getY(), 0);
				
				// check velocity calculation accuracy
				assertEquals(seqSimulator.getBodies().get(i).getVel().getX(), concSimulator.getBodies().get(i).getVel().getX(), 0);
				assertEquals(seqSimulator.getBodies().get(i).getVel().getY(), concSimulator.getBodies().get(i).getVel().getY(), 0);
			}
		}
	} 
}
