package pcd.lab04.monitors;

import java.util.*;

class Producer extends Thread {

	private IBoundedBuffer<Integer> buffer;
	private Random gen;
	
	public Producer(IBoundedBuffer<Integer> buffer){
		gen = new Random();
		this.buffer = buffer;
	}

	public void run(){
		while (true){
			Integer item = produce();
			try {
				buffer.put(item);
				log("produced "+item);
			} catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
	
	private Integer produce(){
		int v = gen.nextInt(100);
		return v;
	}
	
	private void log(String st){
		synchronized(System.out){
			System.out.println("["+this.getName()+"] "+st);
		}
	}
}
