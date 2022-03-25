package pcd.ass01.conc;

import java.util.concurrent.locks.*;

public class Monitor<Item> implements IMonitor<Item> {

	private Item[] buffer;
	private int in; // points to the next free position
	private int out; // points to the next full position
	
	private int nWaiters;
	private int nHits;
	private int nReturned;
	private int nTotToBeReturned;
	
	private Lock mutex;
	private Condition notEmpty, notFull, notAllInBarrier;

	public Monitor(int size, int nWaiters) {
		in = 0;
		out = 0;
		buffer = (Item[]) new Object[size];
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
		
		this.nWaiters = nWaiters;
		this.nHits = 0;
		this.nReturned = 0;
		this.nTotToBeReturned = size;
		this.notAllInBarrier = mutex.newCondition();
	}

	public void put(Item item) throws InterruptedException {
		try {
			mutex.lock();
			if (isFull()) {
				notFull.await();
			}
			buffer[in] = item;
			in = (in + 1) % buffer.length;
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
				if(areAllGet()) 
					hitAndWaitAll();
				else 
					notEmpty.await();
			}
			Item item = buffer[out];
			out = (out + 1) % buffer.length;
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
			try {
				notEmpty.signalAll();
				hitAndWaitAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
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
			while (!areAllOnBarrier()) {
				
				notAllInBarrier.await();
			}
			
			//if a thread reach this point the whole array of bodies is processed and all thread 
			//have been blocked the barrier, so the counter must be reset for the next iteration
			this.nReturned = 0;
			this.nHits = 0;
			notAllInBarrier.signalAll();
			
		} finally {
			mutex.unlock();
		}
	}

	private boolean isFull() {
		return (in + 1) % buffer.length == out;
	}

	private boolean wasFull() {
		return out == (in + 2) % buffer.length;
	}

	private boolean isEmpty() {
		return in == out;
	}

	private boolean wasEmpty() {
		return in == (out + 1) % buffer.length;
	}
	
	private boolean areAllOnBarrier() {
		return this.nHits == this.nWaiters;
	}
	
	private boolean areAllGet() {
		return this.nReturned == this.nTotToBeReturned;
	}

}
