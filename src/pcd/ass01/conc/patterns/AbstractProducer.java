package pcd.ass01.conc.patterns;

public abstract class AbstractProducer<Item, M extends IProducerConsumer<Item>> extends Thread {

	protected final M monitor;
	public AbstractProducer(M m){
		this.monitor = m;
	}

	public abstract Item produce();
	public abstract Item produce(Item item);
	
}
