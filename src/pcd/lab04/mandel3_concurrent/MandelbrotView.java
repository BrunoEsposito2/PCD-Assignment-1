package pcd.lab04.mandel3_concurrent;

/**
 * Class representing the view part of the application.
 * 
 * @author aricci
 *
 */
public class MandelbrotView {

	private ViewFrame frame;

	public MandelbrotView(int w, int h){
		frame = new ViewFrame(w, h);
	}
	
	public void addListener(InputListener l){
		frame.addListener(l);
	}

	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	frame.setVisible(true);
        });
    }

	public void update(MandelbrotSet set){
		frame.updateImage(set.getImage());
	}
	
	public void changeState(final String s){
		frame.updateText(s);
	}
}
	
