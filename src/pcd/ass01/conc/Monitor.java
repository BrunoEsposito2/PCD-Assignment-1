package pcd.ass01.conc;

import java.util.List;
import java.util.concurrent.locks.*;

public class Monitor<Item> implements IMonitor<Item> {

	private Item[] bufferProdCons;
	private int in; // points to the next free position
	private int out; // points to the next full position
	
	private int nWaiters;
	private int nHits;
	private int nReturned;
	private int nTotToBeReturned;
	
	private Lock mutex;
	private Condition notEmpty, notFull, notAllInBarrier, isAllowedToWork, isAllowedToLead;

	private List<Item> readOnlyList;
	private List<Item> bufferMasterWorkers;
	
	public Monitor(int size, int nWaiters,  List<Item> bmw) {
		in = 0;
		out = 0;
		bufferProdCons = (Item[]) new Object[size];
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
		
		this.nWaiters = nWaiters;
		this.nHits = 0;
		this.nReturned = 0;
		this.nTotToBeReturned = size;
		this.notAllInBarrier = mutex.newCondition();
		
		this.isAllowedToWork = mutex.newCondition();
		this.isAllowedToLead = mutex.newCondition();
		this.bufferMasterWorkers = bmw;
	}
	
	@Override
	public void waitMaster() throws InterruptedException {
		try {
			mutex.lock();
			this.isAllowedToWork.await();
			
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void startAndWaitWorkers(List<Item> rol) throws InterruptedException {
		try {
			mutex.lock();
			this.readOnlyList = rol;
			this.isAllowedToWork.signalAll();
			this.isAllowedToLead.await();
			
			//=======================================
			this.nReturned = 0;
			this.nHits = 0;
			System.out.println("awake!");
			this.notAllInBarrier.signalAll();
			
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public List<Item> getWorkerSublist(int from, int to) {
		try {
			mutex.lock();
			return this.bufferMasterWorkers.subList(from, to);
			
		} finally {
			mutex.unlock();
		}
	}

	public void put(Item item) throws InterruptedException {
		try {
			mutex.lock();
			if (isFull()) {
				notFull.await();
			}
			bufferProdCons[in] = item;
			in = (in + 1) % bufferProdCons.length;
			if (wasEmpty()) {
				notEmpty.signal();
			}
		} finally {
			mutex.unlock();
		}
	}

	public Item get() throws InterruptedException {
		try {
			mutex.lock();
			
			while (isEmpty()) {
				if(areAllGet()) hitAndWaitAll();
				else  			notEmpty.await();
			}
			Item item = bufferProdCons[out];
			out = (out + 1) % bufferProdCons.length;
			if (wasFull()) {
				notFull.signal();
			}
			countReturnedElement();
			return item;
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

	@Override
	public void hitAndWaitAll() throws InterruptedException {
		try {
			mutex.lock();
			
			this.nHits++;
			
			System.out.println(this.nHits + " have hit barrier");
			if(!areAllOnBarrier()) {
				notAllInBarrier.await();
			} else {
				isAllowedToLead.notify();
			}
			//if a thread reach this point the whole array of bodies is processed and all thread 
			//have been blocked the barrier, so the counter must be reset for the next iteration
			
			
		} finally {
			mutex.unlock();
		}
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
		return this.nHits == this.nWaiters;
	}
	
	private boolean areAllGet() {
		return this.nReturned == this.nTotToBeReturned;
	}

}
