package pcd.lab01.step04;

import pcd.lab01.step04.*;

public class MatMulConcurLib {

	private Worker[] workers;
	private static MatMulConcurLib instance;
	
	public static void init(int n){
		instance = new MatMulConcurLib(n);
	}

	public static void init(){
		instance = new MatMulConcurLib(Runtime.getRuntime().availableProcessors()+1);
	}
	
	public static Mat matmul(Mat matA, Mat matB) throws LibNotInitialisedException, MatMulException {
		if (instance == null){
			throw new LibNotInitialisedException();
		}
		return instance.doMulTask(matA, matB);
	}

	private Mat doMulTask(Mat matA, Mat matB) throws MatMulException {
		Mat matC = new Mat(matA.getNRows(), matB.getNColumns());
		int nrows = matA.getNRows()/workers.length;
		int irow = 0;
		try {
			for (int i = 0; i < workers.length - 1; i++){
				workers[i] = new Worker(irow,nrows,matA,matB,matC);
				workers[i].start();
				irow += nrows;
			}
			workers[workers.length - 1] = new Worker(irow,matA.getNRows()-irow,matA,matB,matC);
			workers[workers.length - 1].start();
		
			for (Worker w: workers){
				w.join();
			}				
			return matC;
		} catch (Exception ex){
			throw new MatMulException();
		}
	}
	
	private MatMulConcurLib(int n){
		workers = new Worker[n];
	}
	

}
