package pcd.ass01.seq;

public interface Barrier {

	void hitAndWaitAll() throws InterruptedException;

}
