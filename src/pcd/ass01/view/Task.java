package pcd.ass01.view;

import java.util.ArrayList;

import pcd.ass01.utils.Body;
import pcd.ass01.utils.Boundary;

public class Task {

	private ArrayList<Body> bodies;
	
	public Task(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
	
	// se serve creo un altro costruttore
	
	public ArrayList<Body> getBodies() {
		return this.bodies;
	}
	
}
