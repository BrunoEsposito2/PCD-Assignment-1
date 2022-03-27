package pcd.ass01.conc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pcd.ass01.conc.patterns.IBarrier;
import pcd.ass01.conc.patterns.IMasterWorkers;
import pcd.ass01.conc.patterns.IProducerConsumer;
import pcd.ass01.utils.Body;

public class MonitorImpl<Body> implements IProducerConsumer<Body>, IMasterWorkers<Body>, IBarrier{

	private Lock mutex;
	
	private Body[] bufferProdCons;
	private int in; // points to the next free position
	private int out; // points to the next full position
	private Condition notEmpty, notFull;
	
	private List<Body> readOnlyList;
	private List<Body> bufferMasterWorkers;
	private final int nWorkers;
	private int nWorkersHits;
	private Condition isAllowedToWork, isAllowedToStart;
	
	private final int nConsWaiters;
	private int nConsHits;
	private int nReturned;
	private int nTotToBeReturned;
	private Condition notAllInBarrier, isAllowedToContinue;
	
	public MonitorImpl(int nWorkers, int nConsWaiters, List<Body> bodies) {
		mutex = new ReentrantLock();
		
		this.bufferProdCons = (Body[]) new Object[bodies.size()];
		in = 0;
		out = 0;
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
		
		this.nWorkers = nWorkers;
		this.nWorkersHits = 0;
		this.bufferMasterWorkers = bodies;
		this.readOnlyList = Collections.unmodifiableList(bodies);
		this.isAllowedToWork = mutex.newCondition();
		this.isAllowedToStart = mutex.newCondition();
		
		this.nConsWaiters = nConsWaiters;
		this.nConsHits = 0;
		this.nReturned = 0;
		this.nTotToBeReturned = bodies.size();
		this.notAllInBarrier = mutex.newCondition();
		this.isAllowedToContinue = mutex.newCondition();
	}

	@Override
	public void put(Body item) throws InterruptedException {
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

	@Override
	public Optional<Body> get() throws InterruptedException {
		try {
			mutex.lock();
			
			while (isEmpty()) {
				if(areAllGet()) {
					return Optional.empty();
				}
				else {
					System.out.println("i'm waiting elements");
					notEmpty.await();
				}
			}
			Optional<Body> ret;
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
	public void startAndWaitWorkers(List<Body> rol) throws InterruptedException {
		try {
			mutex.lock();
			this.readOnlyList = rol;
			
			//this.isAllowedToWork.signalAll();
			System.out.println("master is waiting workers...");
			this.synchMasterWorker();
			
			this.isAllowedToWork.signalAll();
			this.isAllowedToContinue.await();
			
			//=======================================
			//if master reach this point the whole array of bodies is processed and all thread 
			//have been blocked the barrier, so the counter must be reset for the next iteration
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
	public Map<String, ? extends Object> initializeWorkerResources(Object[] args) {
		try {
			mutex.lock();
			Map<String, List<Body>> ret = new HashMap<>();
			ret.put("bodiesView", Collections.unmodifiableList(this.readOnlyList));
			ret.put("toProduce", bufferMasterWorkers.subList((int)args[0],(int) args[1]));
			return ret;
		} finally {
			mutex.unlock();
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
			
			System.out.println(this.nConsHits + " have hit barrier");
			if(!areAllOnBarrier()) {
				System.out.println("i'm going to sleep");
				notAllInBarrier.await();
			} else {
				System.out.println("i'm waking master");
				isAllowedToContinue.signal();
			}
			
		} finally {
			mutex.unlock();
		}
		
	}
	
	private boolean areAllWorkersHits() {
		return this.nWorkersHits == this.nWorkers;
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
