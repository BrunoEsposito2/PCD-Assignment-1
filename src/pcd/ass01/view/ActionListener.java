package pcd.ass01.view;

import java.util.ArrayList;

import pcd.ass01.conc.Simulator;
import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public interface ActionListener {

	//void started(ArrayList<Body> bodies, double vt, long iter, Boundary bounds);
	
	void started();
	
	void stopped();
}
