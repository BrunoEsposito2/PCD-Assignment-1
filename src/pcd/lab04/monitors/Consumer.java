package pcd.lab04.monitors;

import java.util.*;

class Consumer extends Thread {

	private IBoundedBuffer<Integer> buffer;
	
	public Consumer(IBoundedBuffer<Integer> buffer){
		this.buffer = buffer;
	}

	public void run(){
		while (true){
			try {
				Integer item = buffer.get();
				consume(item);
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
	
	private void consume(Integer item){
		log("consumed "+item);
	}
	
	private void log(String st){
		synchronized(System.out){
			System.out.println("["+this.getName()+"] "+st);
		}
	}
}
