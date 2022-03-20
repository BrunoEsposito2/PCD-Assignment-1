package pcd.lab04.mandel4_concurrent_ext;

/**
 * 
 * Mandelbrot Viewer 
 * 
 * Concurrent version - extended
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
		
		StartSynch synch = new StartSynch();
		Flag stopFlag = new Flag();
		
		new MasterAgent(set, view, synch, stopFlag).start();		
		Controller controller = new Controller(synch, stopFlag);
		view.addListener(controller);
		view.display();
	}

}
