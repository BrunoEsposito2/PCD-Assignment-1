package pcd.ass01.conc.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public class VisualizerFrame extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VisualizerPanel panel;
	
	private JButton start;
	private JButton stop;
	
	private Controller controller;

    public VisualizerFrame(Controller controller, int w, int h){
        setTitle("Bodies Simulation");
        setSize(w*2,h*2);
        setResizable(false);
        this.controller = controller;
        this.start = new JButton("start");
        this.stop = new JButton("stop");
        this.stop.setEnabled(false);
        panel = new VisualizerPanel(w,h);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(start);
        buttonPanel.add(stop);
        panel.add(buttonPanel);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});
        
        start.addActionListener(this);
        stop.addActionListener(this);
    }
    
    public void setSimulationState() {
		SwingUtilities.invokeLater(()-> {
			start.setEnabled(false);
			stop.setEnabled(true);		
		});
	}

	public void setIdleState() {
		SwingUtilities.invokeLater(()-> {
			start.setEnabled(true);
			stop.setEnabled(false);		
		});
	}
    
    public void display(){
    	try {
        	SwingUtilities.invokeAndWait(() -> {
        		this.setVisible(true);
        	});
    	} catch (Exception ex) {}
    };
    
    public void updateSimulationScene(ArrayList<Body> bodies, double vt, long iter, Boundary bounds) {
    	Thread simulation = new Thread() {
    		public void run() {
    			try {
    				SwingUtilities.invokeAndWait(() -> {
    					panel.display(bodies, vt, iter, bounds);
    					repaint();
    				});
    			} catch (InvocationTargetException | InterruptedException e) {
    				e.printStackTrace();
    			}
    			System.out.println("Finished on " + Thread.currentThread());
    		}
    	};
    	simulation.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object src = e.getSource();
		if (src == start) {
			controller.notifyStarted();
		} else if (src == stop) {
			controller.notifyReset();
			start.setEnabled(true);
			stop.setEnabled(false);
		}
	}    	
}