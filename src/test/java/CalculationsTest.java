import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import ass01.conc.ConcurrentSimulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ass01.seq.SequentialSimulator;
import ass01.utils.Body;
import ass01.utils.BodyGenerator;
import ass01.utils.Boundary;

import static org.junit.jupiter.api.Assertions.*;

public class CalculationsTest {
	
	private ConcurrentSimulator concurrentSimulator;
	private SequentialSimulator sequentialSimulator;
	private int nBodies;
	private BodyGenerator bg;
	private Boundary bounds;

	@BeforeEach
	public void setUp() {
		bg = new BodyGenerator();
		bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
	}

	@Test
	public void testSequentialConcurrentComputations() {
		nBodies = 1000;
		ArrayList<Body> bodySet = bg.generateBodies(nBodies, bounds);
		this.concurrentSimulator = new ConcurrentSimulator(bodySet, bounds);
		this.sequentialSimulator = new SequentialSimulator(bodySet, bounds);

		this.sequentialSimulator.execute(50000);
		this.concurrentSimulator.execute(50000);

		this.checkCalculations(this.concurrentSimulator, this.sequentialSimulator);
	}

	@Test
	public void testConcurrentSimulator100Bodies1000Steps() {
		nBodies = 100;
		ArrayList<Body> bodySet = bg.generateBodies(nBodies, bounds);
		this.concurrentSimulator = new ConcurrentSimulator(bodySet, bounds);
		assertTimeout(Duration.ofMillis(1000), () -> this.concurrentSimulator.execute(1000));
	}

	@Test
	public void testConcurrentSimulator1000Bodies1000Steps() {

	}

	@Test
	public void testConcurrentSimulator1000Bodies5000Steps() {

	}

	@Test
	public void testConcurrentSimulator1000Bodies50000Steps() {
		nBodies = 1000;
		ArrayList<Body> bodySet = bg.generateBodies(nBodies, bounds);
		this.concurrentSimulator = new ConcurrentSimulator(bodySet, bounds);
		assertTimeout(Duration.ofMinutes(6), () -> this.concurrentSimulator.execute(50000));
	}

	// check if concurrent and sequential calculations are equals
	private void checkCalculations(final ConcurrentSimulator concSimulator, final SequentialSimulator seqSimulator) {
		if(concSimulator.getBodies().size() == seqSimulator.getBodies().size()) {
			concSimulator.getBodies().stream().sorted(Comparator.comparingInt(Body::getId)).collect(Collectors.toList());
			seqSimulator.getBodies().stream().sorted(Comparator.comparingInt(Body::getId)).collect(Collectors.toList());

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
