package pcd.lab04.mandel3_concurrent;

/**
 * 
 * Mandelbrot Viewer 
 * 
 * Concurrent version - basic
 * 
 * suggested  starting points: 
 * (0,0) diam 4
 * (-0.75,0.1) diam 0.04
 * (-0.75,0.1) diam 0.4
 * (-0.7485,0.0505) diam 0.000004;
 *
 * @author aricci
 *
 */
public class MandelbrotViewer {
	public static void main(String[] args) {
		
		int w = 1200;
		int h = 800;
		int nIter = 10000;		
		
		MandelbrotSet set = new MandelbrotSet(w, h, nIter);
		MandelbrotView view = new MandelbrotView(w, h);
		Controller controller = new Controller(set, view);
		view.addListener(controller);
		view.display();
	}

}
