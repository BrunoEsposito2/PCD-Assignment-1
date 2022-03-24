package pcd.ass01.seq;

public class Monitor<Item> implements IMonitor<Item> {

        private Item[] buffer;
        private int in; // points to the next free position
        private int out; // points to the next full position

        public Monitor(int size) {
                in = 0;
                out = 0;
                buffer = (Item[]) new Object[size];
        }

        public synchronized void put(Item item) throws InterruptedException {
                while (isFull()) {
                        wait();
                }
                buffer[in] = item;
                in = (in + 1) % buffer.length;
                notifyAll();
        }

        public synchronized Item get() throws InterruptedException {
                while (isEmpty()) {
                        wait();
                }
                Item item = buffer[out];
                out = (out + 1) % buffer.length;
                notifyAll();
                return item;
        }

        private boolean isFull() {
                return (in + 1) % buffer.length == out;
        }

        private boolean isEmpty() {
                return in == out;
        }
}
