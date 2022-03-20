package pcd.lab04.monitors;

public class TestBoundedBuffer {

	public static void main(String[] args){
		//IBoundedBuffer<Integer> buffer = new BoundedBuffer1<Integer>(4);
		IBoundedBuffer<Integer> buffer = new BoundedBuffer2<Integer>(4);

		int nProducers = 1;
		int nConsumers = 1;

		for (int i = 0; i < nProducers; i++){
			new Producer(buffer).start();
		}

		for (int i = 0; i < nConsumers; i++){
			new Consumer(buffer).start();
		}
	}	
}
