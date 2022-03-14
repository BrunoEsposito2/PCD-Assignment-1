package pcd.lab01.step04;

public class Step04_Sequential {

	public static void main(String[] args) throws Exception {
		
		/* square matrix */
		int size = 1000;
		
		int n = size; /* num rows mat A */
		int k = size; /* num columns mat A = num rows mat B */
		int m = size; /* nun columns mat B */
		
		boolean debugging = false;

		System.out.println("Testing A["+n+","+k+"]*B["+k+","+m+"]");

		System.out.println("Initialising...");

		Mat matA = new Mat(n,m);
		matA.initRandom(10);

		if (debugging) {
			System.out.println("A:");
			matA.print();
		}
		
		Mat matB = new Mat(m,k);
		matB.initRandom(10);
		
		if (debugging){
			System.out.println("B:");
			matB.print();
		}
		
		System.out.println("Initialising done.");
		System.out.println("Computing matmul...");

		Chrono cron = new Chrono();
		cron.start();
		Mat matC = Mat.mul(matA, matB);
		cron.stop();
		
		System.out.println("Computing matmul done.");

		if (debugging){
			System.out.println("C:");
			matC.print();
		}
		
		System.out.println("Time elapsed: "+cron.getTime()+" ms.");
		
	}

}
