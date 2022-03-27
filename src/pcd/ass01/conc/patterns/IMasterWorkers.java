package pcd.ass01.conc.patterns;

import java.util.List;
import java.util.Map;

public interface IMasterWorkers<Item> extends IMonitor{
	void synchMasterWorker() throws InterruptedException;
	void startAndWaitWorkers(List<Item> rol) throws InterruptedException;
	Map<String, ? extends Object> initializeWorkerResources(Object[] args);
}
