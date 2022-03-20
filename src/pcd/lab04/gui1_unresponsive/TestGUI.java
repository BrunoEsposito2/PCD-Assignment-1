package pcd.lab04.gui1_unresponsive;

public class TestGUI {
  static public void main(String[] args){
	  
	MyController controller = new MyController();
    new MyView(controller).display();
  }
  
}
