package pcd.ass01.conc.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pcd.ass01.conc.Simulator;
import pcd.ass01.seq.SequentialSimulator;
import pcd.ass01.utils.*;

public class CalculationsTest {
	
	private Simulator concurrentSimulator;
	private SequentialSimulator sequentialSimulator;
    
	@BeforeEach
	public void setUp() {
		BodyGenerator bg = new BodyGenerator();
		Boundary bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		ArrayList<Body> bodySet = bg.generateBodies(2, bounds);
		this.concurrentSimulator = new Simulator(bodySet, bounds);
		this.sequentialSimulator = new SequentialSimulator(bodySet, bounds);
	}
	
	// N steps = 1000
	@Test
	public void testExecuteThousand() {
		this.concurrentSimulator.execute(1000);
		this.sequentialSimulator.execute(1000);
		
		this.checkCalculations(this.concurrentSimulator, this.sequentialSimulator);
	}
	
	// N steps = 10000
	@Test
	public void testExecuteTenThousand() {
		this.concurrentSimulator.execute(10000);
		this.sequentialSimulator.execute(10000);
		
		this.checkCalculations(this.concurrentSimulator, this.sequentialSimulator);
	}
	
	// N steps = 50000
	@Test
	public void testExecuteFiftyThousand() {
		this.concurrentSimulator.execute(50000);
		this.sequentialSimulator.execute(50000);
		
		this.checkCalculations(this.concurrentSimulator, this.sequentialSimulator);
	}
	
	// check if concurrent and sequential calculations are equals
	private void checkCalculations(final Simulator concSimulator, final SequentialSimulator seqSimulator) {
		if(concSimulator.getBodies().size() == seqSimulator.getBodies().size()) {
			for(int i = 0; i < concSimulator.getBodies().size(); i++) {
				// check position calculation accuracy
				assertEquals(concSimulator.getBodies().get(i).getPos().getX(), seqSimulator.getBodies().get(i).getPos().getX(), 0.0);
				assertEquals(concSimulator.getBodies().get(i).getPos().getY(), seqSimulator.getBodies().get(i).getPos().getY(), 0.0);
				
				// check velocity calculation accuracy
				assertEquals(concSimulator.getBodies().get(i).getVel().getX(), seqSimulator.getBodies().get(i).getVel().getX(), 0.0);
				assertEquals(concSimulator.getBodies().get(i).getVel().getY(), seqSimulator.getBodies().get(i).getVel().getY(), 0.0);
			}
		}
	} 
}
