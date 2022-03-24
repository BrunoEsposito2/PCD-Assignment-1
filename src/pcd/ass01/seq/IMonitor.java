package pcd.ass01.seq;

public interface IMonitor<Item> {

    void put(Item item) throws InterruptedException;
    
    Item get() throws InterruptedException;
    
}
