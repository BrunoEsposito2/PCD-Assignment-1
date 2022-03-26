package pcd.ass01.conc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.*;

import pcd.ass01.utils.Body;

public class Monitor<Item> implements IMonitor<Item> {

	private Item[] bufferProdCons;
	private int in; // points to the next free position
	private int out; // points to the next full position
	
	private final int nProdWaiters;
	private int nProdHits;
	private final int nConsWaiters;
	private int nConsHits;
	private int nReturned;
	private int nTotToBeReturned;
	
	private Lock mutex;
	private Condition notEmpty, notFull, notAllInBarrier, isAllowedToWork, isAllowedToLead;

	private List<Item> readOnlyList;
	private List<Item> bufferMasterWorkers;
	private Condition isAllowedToStart;
	
	public Monitor(int size, int nConsWaiters,  List<Item> bmw, int nProdWaiters) {
		in = 0;
		out = 0;
		bufferProdCons = (Item[]) new Object[size];
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
		
		this.nProdWaiters = nProdWaiters;
		this.nProdHits = 0;
		this.nConsWaiters = nConsWaiters;
		this.nConsHits = 0;
		this.nReturned = 0;
		this.nTotToBeReturned = size;
		this.notAllInBarrier = mutex.newCondition();
		
		this.isAllowedToWork = mutex.newCondition();
		this.isAllowedToStart = mutex.newCondition();
		this.isAllowedToLead = mutex.newCondition();
		this.bufferMasterWorkers = bmw;
	}
	
	@Override
	public void synchMasterWorker() throws InterruptedException {
		try {
			mutex.lock();
			this.nProdHits++;
			if(this.areAllProdHits()) {
				this.isAllowedToStart.signalAll();
				this.nProdHits = 0;
			}  else {
				this.isAllowedToStart.await();
			}
			
			
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void startAndWaitWorkers(List<Item> rol) throws InterruptedException {
		try {
			mutex.lock();
			this.readOnlyList = rol;
			
			//this.isAllowedToWork.signalAll();
			System.out.println("master is waiting workers...");
			this.synchMasterWorker();
			
			this.isAllowedToWork.signalAll();
			this.isAllowedToLead.await();
			
			//=======================================
			System.out.println("master is awake!");
			this.nReturned = 0;
			this.nConsHits = 0;
			this.notAllInBarrier.signalAll();
			System.out.println("wake up consumers!");
			
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void getWorkerSublist(Producer p) {
		try {
			mutex.lock();
			p.setToProduce((List<Body>) this.bufferMasterWorkers.subList(p.from, p.to));
			p.setBodies((List<Body>) this.readOnlyList);
			
		} finally {
			mutex.unlock();
		}
	}

	public void put(Item item) throws InterruptedException {
		try {
			mutex.lock();
			/*if (isFull()) {
				notFull.await();
			}*/
			
			bufferProdCons[in] = item;
			in = (in + 1) % bufferProdCons.length;
			if (wasEmpty()) {
				System.out.println("i'm waking a consumer 'cause i've produced something");
				notEmpty.signalAll();
			}
		} finally {
			mutex.unlock();
		}
	}

	public void get(Consumer c) throws InterruptedException {
		try {
			mutex.lock();
			
			while (isEmpty()) {
				if(areAllGet()) {
					System.out.println("i'm a consumer and i'm going to the barrier");
					hitAndWaitAll();
				}
				else {
					System.out.println("i'm waiting elements");
					notEmpty.await();
				}
			}
			Item item = bufferProdCons[out];
			out = (out + 1) % bufferProdCons.length;
			/*if (wasFull()) {
				notFull.signal();
			}*/
			c.consume((Body)item);
			System.out.println("i consumed an element!");
			countReturnedElement();
			
		} finally {
			mutex.unlock();
		}
	}
	
	private void countReturnedElement() {
		nReturned++;
		if(areAllGet()) {
				notEmpty.signalAll();	//unlocks all getters stuck on the notEmpty wait 
										//when there are no more elements to process
		} else {
			//keep consuming
		}
		
	}

	
	private void hitAndWaitAll() throws InterruptedException {
		try {
			
			mutex.lock();
			this.nConsHits++;
			
			System.out.println(this.nConsHits + " have hit barrier");
			if(!areAllOnBarrier()) {
				System.out.println("i'm going to sleep");
				notAllInBarrier.await();
			} else {
				System.out.println("i'm waking master");
				isAllowedToLead.signal();
			}
			//if a thread reach this point the whole array of bodies is processed and all thread 
			//have been blocked the barrier, so the counter must be reset for the next iteration
			
			
		} finally {
			mutex.unlock();
		}
	}

	
	private boolean areAllProdHits() {
		return this.nProdHits == this.nProdWaiters;
	}
	
	private boolean isFull() {
		return (in + 1) % bufferProdCons.length == out;
	}

	private boolean wasFull() {
		return out == (in + 2) % bufferProdCons.length;
	}

	private boolean isEmpty() {
		return in == out;
	}

	private boolean wasEmpty() {
		return in == (out + 1) % bufferProdCons.length;
	}
	
	private boolean areAllOnBarrier() {
		return this.nConsHits == this.nConsWaiters;
	}
	
	private boolean areAllGet() {
		return this.nReturned == this.nTotToBeReturned;
	}

}
