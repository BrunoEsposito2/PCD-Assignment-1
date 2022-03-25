package pcd.ass01.conc;

import java.util.List;

public interface IMonitor<Item> {

	
	void waitMaster() throws InterruptedException;
	
	void startAndWaitWorkers(List<Item> rol) throws InterruptedException;
	
	public List<Item> getWorkerSublist(int from, int to);
	
    void put(Item item) throws InterruptedException;
    
    Item get() throws InterruptedException;
    
    void hitAndWaitAll() throws InterruptedException;
    
}
