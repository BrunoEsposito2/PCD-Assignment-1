package pcd.lab04.gui1_unresponsive;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MyView extends JFrame implements ActionListener {

	private MyController controller;
	
	public MyView(MyController controller) {
		super("My View");
		
		this.controller = controller;
		
		setSize(400, 60);
		setResizable(false);
		
		JButton button1 = new JButton("Event #1");
		button1.addActionListener(this);

		JButton button2 = new JButton("Event #2");
		button2.addActionListener(this);
		
		JPanel buttons = new JPanel();
		buttons.add(button1);		
		buttons.add(button2);		
		setLayout(new BorderLayout());
	    add(buttons,BorderLayout.NORTH);
	    		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(-1);
			}
		});
	}
	
	public void actionPerformed(ActionEvent ev) {
		try {
			controller.processEvent(ev.getActionCommand());
		} catch (Exception ex) {
		}
	}
	
	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}
	
	
}
