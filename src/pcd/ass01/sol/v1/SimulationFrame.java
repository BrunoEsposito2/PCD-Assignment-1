package pcd.ass01.sol.v1;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import javax.swing.*;
import pcd.ass01.sol.common.*;

/**
 * Simulation view
 * @author aricci
 *
 */
public class SimulationFrame extends JFrame {
    
    private VisualiserPanel bodyAreaPanel;
    private JButton startButton, stopButton;
    
    public SimulationFrame(int w, int h, Controller controller, View view){
        setTitle("Bodies Simulation");
        setSize(w, h + 40);
        setResizable(false);

		startButton = new JButton("start");
		stopButton = new JButton("stop");
		JPanel controlPanel = new JPanel();
		controlPanel.add(startButton);
		controlPanel.add(stopButton);

        bodyAreaPanel = new VisualiserPanel(w,h);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);
		cp.add(BorderLayout.NORTH, controlPanel);
		cp.add(BorderLayout.CENTER, bodyAreaPanel);
		setContentPane(cp);		
        
		startButton.setEnabled(true);
		startButton.addActionListener(ev -> {
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
			controller.notifySimulationStarted(view);
		});
		
		stopButton.setEnabled(false);
		stopButton.addActionListener(ev -> {
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
			controller.notifySimulationStopped(view);
		});
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});
    }
    
    public void showUp() {
    	SwingUtilities.invokeLater(() -> {
            setVisible(true);
    	});
    }
     
    public void display(Simulation sim, Semaphore done){
    	bodyAreaPanel.display(sim, done);
	}
        
    public static class VisualiserPanel extends JPanel {
        
    	private Simulation sim;
    	private Semaphore done;
    	
        private long dx;
        private long dy;
        
        public VisualiserPanel(int w, int h){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
        }

        public void paint(Graphics g){
        	Graphics2D g2 = (Graphics2D) g;
    		
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    		          RenderingHints.VALUE_ANTIALIAS_ON);
    		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
    		          RenderingHints.VALUE_RENDER_QUALITY);
    		g2.clearRect(0,0,this.getWidth(),this.getHeight());
	        
    		if (sim != null) {
	    		sim.getBodies().forEach( b -> {
	    	        	Position p = b.getPos();
	    	            double rad = b.getRadius();
	    	            int x0 = (int)(dx + p.getX()*dx);
	    		        int y0 = (int)(dy - p.getY()*dy);
	    		        g2.drawOval(x0,y0, (int)(rad*dx*2), (int)(rad*dy*2));
	    		});		    

	    		String time = String.format("%.2f", sim.getTime());
	    		g2.drawString("Bodies: " + sim.getBodies().size() + " - vt: " + time + " - nIter: " + sim.getNumIter(), 2, 20);

	    		// System.out.println("drawing bodies completed");

	    		done.release();
	    		sim = null;
	    		done = null;
    		}
        }        

        public void display(Simulation sim, Semaphore done) {
        	this.sim = sim;
        	this.done = done;
        	repaint();
        }

    }
    
}
