package pcd.lab04.gui.chrono2_strict;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI/Swing internal component, not designed as a monitor.
 * 
 * @author aricci
 *
 */
public class CounterGUI extends JFrame implements ActionListener {

	private JButton start;
	private JButton stop;
	private JButton reset;
	private JTextField display;
	
	private Controller controller;
	
	public CounterGUI(Controller contr, int initialValue){
		setTitle("Counter GUI");
		setSize(300,100);		
		controller = contr;
		display = new JTextField(5);
		display.setEditable(false);		
		display.setText(""+ initialValue);
		start = new JButton("start");
		stop  = new JButton("stop");
		reset = new JButton("reset");
		stop.setEnabled(false);
		
		Container cp = getContentPane();
		JPanel panel = new JPanel();
		
		Box p0 = new Box(BoxLayout.X_AXIS);
		p0.add(display);
		Box p1 = new Box(BoxLayout.X_AXIS);
		p1.add(start);
		p1.add(stop);
		p1.add(reset);
		Box p2 = new Box(BoxLayout.Y_AXIS);
		p2.add(p0);
		p2.add(Box.createVerticalStrut(10));
		p2.add(p1);
		
		panel.add(p2);
		cp.add(panel);
		
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
		reset.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src==start){	
			controller.notifyStarted();
		} else if (src == stop){
			controller.notifyStopped();
			start.setEnabled(true);
			stop.setEnabled(false);
			reset.setEnabled(true);
		} else if (src == reset){
			controller.notifyReset();
		}
	}
	
	public void setCountingState() {
		SwingUtilities.invokeLater(()-> {
			start.setEnabled(false);
			stop.setEnabled(true);
			reset.setEnabled(false);			
		});
	}

	public void setIdleState() {
		SwingUtilities.invokeLater(()-> {
			start.setEnabled(true);
			stop.setEnabled(false);
			reset.setEnabled(true);			
		});
	}

	public void updateCountValue(int value) {
		SwingUtilities.invokeLater(()-> {
			display.setText("" + value);
		});
	}

	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	this.setVisible(true);
        });
    }
	
	
}
