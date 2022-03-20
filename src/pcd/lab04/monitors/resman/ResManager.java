package pcd.lab04.monitors.resman;

public interface ResManager {

	/**
	 * Gets access to a resource
	 * 
	 * @return the resource id
	 * @throws InterruptedException
	 */
	int get() throws InterruptedException;
	
	/**
	 * Releases the resource id
	 * @param id
	 */
	void release(int id);

}
