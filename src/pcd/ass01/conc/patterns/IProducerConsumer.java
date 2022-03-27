package pcd.ass01.conc.patterns;

public interface IProducerConsumer<Item> extends IMonitor{
	void put(Item item) throws InterruptedException;
    Item get() throws InterruptedException;
}
