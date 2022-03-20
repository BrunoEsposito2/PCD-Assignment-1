package pcd.lab04.monitors;

import java.util.concurrent.locks.*;

public class BoundedBuffer2<Item> implements IBoundedBuffer<Item> {

	private Item[] buffer;
	private int in; // points to the next free position
	private int out; // points to the next full position
	private Lock mutex;
	private Condition notEmpty, notFull;

	public BoundedBuffer2(int size) {
		in = 0;
		out = 0;
		buffer = (Item[]) new Object[size];
		mutex = new ReentrantLock();
		notEmpty = mutex.newCondition();
		notFull = mutex.newCondition();
	}

	public void put(Item item) throws InterruptedException {
		try {
			mutex.lock();
			if (isFull()) {
				notFull.await();
			}
			buffer[in] = item;
			in = (in + 1) % buffer.length;
			if (wasEmpty()) {
				notEmpty.signal();
			}
		} finally {
			mutex.unlock();
		}
	}

	public Item get() throws InterruptedException {
		try {
			mutex.lock();
			if (isEmpty()) {
				notEmpty.await();
			}
			Item item = buffer[out];
			out = (out + 1) % buffer.length;
			if (wasFull()) {
				notFull.signal();
			}
			return item;
		} finally {
			mutex.unlock();
		}
	}

	private boolean isFull() {
		return (in + 1) % buffer.length == out;
	}

	private boolean wasFull() {
		return out == (in + 2) % buffer.length;
	}

	private boolean isEmpty() {
		return in == out;
	}

	private boolean wasEmpty() {
		return in == (out + 1) % buffer.length;
	}

}
