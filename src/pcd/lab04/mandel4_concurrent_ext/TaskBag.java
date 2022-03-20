package pcd.lab04.mandel4_concurrent_ext;

import java.util.LinkedList;

public class TaskBag {

	private LinkedList<Task> buffer;

	public TaskBag() {
		buffer = new LinkedList<Task>();
	}

	public synchronized void clear() {
		buffer.clear();
	}
	
	public synchronized void addNewTask(Task task) {
		buffer.addLast(task);
		notifyAll();
	}

	public synchronized Task getATask() {
		while (buffer.isEmpty()) {
			try {
				wait();
			} catch (Exception ex) {}
		}
		return buffer.removeFirst(); 
	}
	
}
