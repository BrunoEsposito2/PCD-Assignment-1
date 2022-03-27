package pcd.ass01.conc.patterns;

import java.util.List;
import java.util.Map;

public abstract class AbstractWorkerProducer<Item,  
											 M extends IProducerConsumer<Item> & 
											 		   IMasterWorkers<Item>> 
					  extends AbstractProducer<Item,  M>{

	private List<Item> toProduce;
	private Map<String, ? extends Object> resources;
	
	public AbstractWorkerProducer(M monitorMW) {
		super(monitorMW);
	}
	
	public void run(){
	    while(true) {
	    	try {
	    		System.out.println("i'm going to wait master");
				this.monitor.synchMasterWorker();
	    		
				this.resources = monitor.initializeWorkerResources();
		    	
	    		//for each body in toProduce put in the monitor's buffer the updated body
		        for(Item i:toProduce){
		        	i = produce(i);
	            	monitor.put(i);
		        }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    }
    }
}
