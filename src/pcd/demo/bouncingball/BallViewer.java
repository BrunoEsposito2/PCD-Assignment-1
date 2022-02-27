package pcd.demo.bouncingball;

import javax.swing.SwingUtilities;

public class BallViewer extends Thread {
    
    private boolean stop;
    private Context context;
    private ViewerFrame frame;
    private static final int FRAMES_PER_SEC = 25;
    
    public BallViewer(Context context){
        stop = false;
        this.context = context ;
        frame = new ViewerFrame(context,620,620);
        SwingUtilities.invokeLater(() -> {
        		frame.setVisible(true);
        });
   }
    
    public void run(){
        while (!stop) {
            long t0 = System.currentTimeMillis();
        	frame.updatePosition(context.getPositions());
            long t1 = System.currentTimeMillis();
        	//log("update pos");
            long dt = (1000 / FRAMES_PER_SEC) - (t1-t0);
            if (dt > 0){
	            try {
	                Thread.sleep(dt);     
	            } catch (Exception ex){
	            }
            }
        }
    }
    
    private void log(String msg){
        System.out.println("[VISUALISER] "+msg);
    }

}
