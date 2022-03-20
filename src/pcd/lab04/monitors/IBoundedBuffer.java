package pcd.lab04.monitors;

public interface IBoundedBuffer<Item> {

    void put(Item item) throws InterruptedException;
    
    Item get() throws InterruptedException;
    
}
