package pcd.lab03.liveness;

import java.util.*;

interface IObserved {
	int getState();
	void register(IObserver obj);
}

interface IObserver {
	void notifyStateChanged(IObserved obs);
}

class MyEntityA implements IObserved {

	private List<IObserver> obsList;
	private int state;

	public MyEntityA(){
		obsList = new ArrayList<IObserver>();
	}

	public void register(IObserver obs) {
		obsList.add(obs);
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized void changeState1() {
		state++;
		for (IObserver o: obsList){
			o.notifyStateChanged(this);
		}
	}

	public synchronized void changeState2() {
		state--;
		for (IObserver o: obsList){
			o.notifyStateChanged(this);
		}
	}
}

class MyEntityB implements IObserver {

	List<IObserved> obsList;

	public MyEntityB(){
		obsList = new ArrayList<IObserved>();
	}
	
	public synchronized void observe(IObserved obj){
		obsList.add(obj);
		obj.register(this);
	}
	
	public synchronized void notifyStateChanged(IObserved obs) {
		synchronized(System.out){
			System.out.println("state changed: "+obs.getState());
		}
	}

	public synchronized int getOverallState() {
		int sum = 0;
		for (IObserved o: obsList){
			sum += o.getState();
		}
		return sum;
	}
}


class MyThreadA extends Thread {
 	MyEntityA obj;
	
 	public MyThreadA(MyEntityA obj){
 		this.obj = obj;
 	}
 	
	public void run(){
		while (true){
			obj.changeState1();
			obj.changeState2();
		}
	}
}

class MyThreadB extends Thread {
 	MyEntityB obj;
	
 	public MyThreadB(MyEntityB obj){
 		this.obj = obj;
 	}
 	
	public void run(){
		while (true){
			log("overall state: "+obj.getOverallState());
		}
	}

	private void log(String msg){
		synchronized(System.out){
			System.out.println("["+this+"] "+msg);
		}
	}
}


public class TestObsPatternDeadlock {
	public static void main(String[] args) {
		
		MyEntityA objA = new MyEntityA();
		MyEntityB objB = new MyEntityB();
		objB.observe(objA);
		
		new MyThreadA(objA).start();
		new MyThreadB(objB).start();

	}
}
