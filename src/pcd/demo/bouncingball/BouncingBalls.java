package pcd.demo.bouncingball;

import javax.swing.SwingUtilities;

/**
 * Bouncing Ball demo - stress the number of threads
 * 
 * @author aricci
 */
public class BouncingBalls {

    public static void main(String[] args) {
        
        Context ctx = new Context();
        
        BallViewer viewer = new BallViewer(ctx);
        viewer.start();
      
        ControlPanel control = new ControlPanel(ctx);
        SwingUtilities.invokeLater(() -> {
            control.setVisible(true);
        });

    }
}
