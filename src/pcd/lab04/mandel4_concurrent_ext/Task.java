package pcd.lab04.mandel4_concurrent_ext;

public class Task {
	
	private int x0, x1;
	private Complex c0;
	private double diam;
	
	public Task(Complex c0, double diam, int x0, int x1) {
		this.c0 = c0;
		this.diam = diam;
		this.x0 = x0;
		this.x1 = x1;
	}

	public Task(Complex c0, double diam) {
		this.c0 = c0;
		this.diam = diam;
	}
	
	public int getX0() {
		return x0;
	}

	public int getX1() {
		return x1;
	}

	public Complex getC0() {
		return c0;
	}

	public double getDiam() {
		return diam;
	}
	
}
