package pcd.ass01.conc.patterns;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MonitorImpl<Item> implements IMasterWorkers<Item>, IProducerConsumer<Item>, IBarrier{

	private Lock mutex;
	
	private List<Item> readOnlyList;
	private List<Item> bufferMasterWorkers;
	private final int nWorkers;
	private int nWorkersHits;
	private Condition isAllowedToWork, isAllowedToStart;
	
	private Item[] bufferProdCons;
	private int in; // points to the next free position
	private int out; // points to the next full position
	private Condition notEmpty, notFull;
	
	private final int nConsWaiters;
	private int nConsHits;
	private int nReturned;
	private int nTotToBeReturned;
	private Condition notAllInBarrier, isAllowedToContinue;
	
	@SuppressWarnings("unchecked")
	public MonitorImpl(int nWorkers, int nConsWaiters, List<Item> items) {
		mutex = new ReentrantLock();
		
		this.nWorkers = nWorkers;
		this.nWorkersHits = 0;
		this.bufferMasterWorkers = items;
		this.readOnlyList = Collections.unmodifiableList(items);
		this.isAllowedToWork = mutex.newCondition();
		this.isAllowedToStart = mutex.newCondition();
		
		this.bufferProdCons = (Item[]) new Object[items.size()+1];
		in = 0;
		out = 0;
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
		
		this.nConsWaiters = nConsWaiters;
		this.nConsHits = 0;
		this.nReturned = 0;
		this.nTotToBeReturned = items.size();
		this.notAllInBarrier = mutex.newCondition();
		this.isAllowedToContinue = mutex.newCondition();
	}

	

	@Override
	public void synchMasterWorker() throws InterruptedException {
		try {
			mutex.lock();
			this.nWorkersHits++;
			if(this.areAllWorkersHits()) {
				this.isAllowedToStart.signalAll();
				this.nWorkersHits = 0;
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
			
			this.synchMasterWorker();
			
			this.isAllowedToWork.signalAll();
			this.isAllowedToContinue.await();
			
			//=======================================
			//if master reach this point the whole array of bodies is processed and all thread 
			//have been blocked the barrier, so the counter must be reset for the next iteration
			this.nReturned = 0;
			this.nConsHits = 0;
			this.notAllInBarrier.signalAll();
			
		} finally {
			mutex.unlock();
		}
		
	}

	@Override
	public Map<String, ? extends Object> initializeWorkerResources(Object[] args) {
		try {
			mutex.lock();
			Map<String, List<Item>> ret = new HashMap<>();
			ret.put("bodiesView", Collections.unmodifiableList(this.readOnlyList));
			ret.put("toProduce", bufferMasterWorkers.subList((int)args[0],(int) args[1]));
			return ret;
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void put(Item item) throws InterruptedException {
		try {
			mutex.lock();
			
			bufferProdCons[in] = item;
			in = (in + 1) % bufferProdCons.length;
			if (wasEmpty()) {
				notEmpty.signalAll();
			}
		} finally {
			mutex.unlock();
		}
	}

	@Override
	public Optional<Item> get() throws InterruptedException {
		try {
			mutex.lock();
			
			while (isEmpty()) {
				if(areAllGet()) {
					return Optional.empty();
				}
				else {
					notEmpty.await();
				}
			}
			Optional<Item> ret;
			ret = Optional.of(bufferProdCons[out]);
			out = (out + 1) % bufferProdCons.length;
			if (wasFull()) {
				notFull.signal();
			}
			countReturnedElement();
			return ret;
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
	public void evaluateSynchronize() throws InterruptedException {
		try {
			mutex.lock();
			if(areAllGet()) hitAndWaitAll();
		} finally {
			mutex.unlock();
		}
	}
	
	@Override
	public void hitAndWaitAll() throws InterruptedException {
		try {	
			mutex.lock();
			
			this.nConsHits++;
			
			if(!areAllOnBarrier()) {
				notAllInBarrier.await();
			} else {
				isAllowedToContinue.signal();
			}
			
		} finally {
			mutex.unlock();
		}
		
	}
	
	private boolean areAllWorkersHits() {
		return this.nWorkersHits == this.nWorkers;
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
