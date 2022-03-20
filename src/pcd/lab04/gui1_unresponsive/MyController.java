package pcd.lab04.gui1_unresponsive;


public class MyController {
	
	public void processEvent(String event) {
		try {
			System.out.println("[Controller] Processing the event "+event+" ...");
		    Thread.sleep(5000);
			System.out.println("[Controller] Processing the event done.");
		} catch (Exception ex) {
		}
	}

}
