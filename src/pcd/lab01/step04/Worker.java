package pcd.lab01.step04;

import pcd.lab01.step04.*;

public class Worker extends Thread {
	
	private Mat a,b,c;
	private int from, nrows;
	
	public Worker(int from, int nrows, Mat a, Mat b, Mat c){
		this.from = from;
		this.nrows = nrows;
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public void run() {
		long count = 0;
		int to = from + nrows;
		log("started: "+from+" to "+(to-1));
		for (int i = from; i < to; i++){
			for (int j = 0; j < c.getNColumns(); j++){
				double sum = 0;
				for (int k = 0; k < a.getNColumns(); k++){
					sum += a.get(i, k)*b.get(k, j);
					count++;
				}
				c.set(i,j,sum);
			}
		}
		log("done "+count);
	}
	
	private void log(String msg){
		System.out.println("[WORKER] "+msg);
	}

}
