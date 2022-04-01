package pcd.ass01.conc.patterns;

import java.util.List;
import java.util.Map;

import pcd.ass01.view.Flag;
import pcd.ass01.view.TaskBag;
import pcd.ass01.view.TaskCompletionLatch;

public interface IMasterWorkers<Item> extends IMonitor{
	void synchMasterWorker() throws InterruptedException;
	void startAndWaitWorkers(List<Item> rol) throws InterruptedException;
	Map<String, ? extends Object> initializeWorkerResources(Object[] args);
}
