package pcd.lab04.gui2_mvc_unresponsive;


public class MyController {
	
	private MyModel model;
	public MyController(MyModel model){
		this.model = model;
	}
	
	public void processEvent(String event) {
			try {
				System.out.println("[Controller] Processing the event "+event+" ...");
				Thread.sleep(5000);
				model.update();
				System.out.println("[Controller] Processing the event done.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}			
	}

}
