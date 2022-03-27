package pcd.ass01.conc.legacy;

import java.util.List;

public interface IMonitor<Item> {

	
	void synchMasterWorker() throws InterruptedException;
	
	void startAndWaitWorkers(List<Item> rol) throws InterruptedException;
	
	public void getWorkerSublist(Producer p);
	
    void put(Item item) throws InterruptedException;
    
    void get(Consumer c) throws InterruptedException;
    
    //void hitAndWaitAll() throws InterruptedException;
    
}
