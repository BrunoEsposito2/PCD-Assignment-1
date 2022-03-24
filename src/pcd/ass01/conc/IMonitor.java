package pcd.ass01.conc;

public interface IMonitor<Item> {

    void put(Item item) throws InterruptedException;
    
    Item get() throws InterruptedException;
    
}
